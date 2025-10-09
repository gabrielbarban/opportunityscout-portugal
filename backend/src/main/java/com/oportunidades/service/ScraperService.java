package com.oportunidades.service;

import com.oportunidades.entity.Oportunidade;
import com.oportunidades.repository.OportunidadeRepository;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ScraperService {

    @Inject
    OportunidadeRepository oportunidadeRepository;

    private static final String COMPETE2030_URL = "https://www.compete2030.gov.pt/avisos";

    @Scheduled(cron = "0 0 */12 * * ?")
    @Transactional
    public void executarColetaAutomatica() {
        Log.info("Iniciando coleta automática de oportunidades");
        coletarTodasFontes();
    }

    @Transactional
    public String coletarTodasFontes() {
        int compete = coletarCompete2030();
        String resultado = String.format("Coletadas %d oportunidades do Compete2030", compete);
        Log.info(resultado);
        return resultado;
    }

    @Transactional
    public int coletarPortugal2030() {
        return 0;
    }

    @Transactional
    public int coletarCompete2030() {
        List<Oportunidade> oportunidades = new ArrayList<>();
        try {
            Log.info("Conectando ao Compete2030: " + COMPETE2030_URL);
            
            Document doc = Jsoup.connect(COMPETE2030_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(20000)
                    .get();
            
            Log.info("HTML recebido, tamanho: " + doc.html().length());
            
            String[] possiveisSeletores = {
                "article", "div.card", "div.aviso", ".aviso-item", 
                "div[class*=aviso]", "div[class*=card]", "li", "tr"
            };
            
            Elements items = null;
            for (String seletor : possiveisSeletores) {
                items = doc.select(seletor);
                if (!items.isEmpty()) {
                    Log.info("Encontrados " + items.size() + " elementos com seletor: " + seletor);
                    break;
                }
            }
            
            if (items == null || items.isEmpty()) {
                Log.warn("Nenhum elemento encontrado com seletores conhecidos");
                return 0;
            }

            for (Element item : items) {
                String textoCompleto = item.text();
                
                if (textoCompleto.length() < 20) continue;
                if (!textoCompleto.toLowerCase().contains("sice") && 
                    !textoCompleto.toLowerCase().contains("aviso") &&
                    !textoCompleto.toLowerCase().contains("candidatura")) continue;

                String titulo = extrairTexto(item, "h1, h2, h3, h4, strong, b");
                if (titulo.isEmpty()) {
                    String[] linhas = textoCompleto.split("\n");
                    titulo = linhas[0].trim();
                }
                
                if (titulo.length() < 15 || titulo.length() > 250) continue;

                String link = COMPETE2030_URL + "#" + titulo.hashCode();
                if (oportunidadeRepository.existsByLink(link)) {
                    Log.info("Oportunidade já existe: " + titulo);
                    continue;
                }

                String codigo = extrairCodigo(textoCompleto);
                String dataTexto = extrairData(textoCompleto);
                String descricao = extrairDescricao(item);
                
                Oportunidade op = new Oportunidade();
                op.titulo = titulo;
                op.fonte = Oportunidade.Fonte.COMPETE2030;
                op.entidade = "Compete 2030";
                op.link = link;
                op.codigo = codigo;
                op.categoria = extrairCategoria(textoCompleto);
                op.dataFim = parseData(dataTexto);
                op.descricao = descricao;
                op.tipoApoio = extrairTipoApoio(textoCompleto);
                op.beneficiarios = "PME e Empresas";

                oportunidades.add(op);
                Log.info("Oportunidade coletada: " + titulo);
            }

            oportunidades.forEach(oportunidadeRepository::persist);
            Log.info("Total de oportunidades salvas: " + oportunidades.size());
            return oportunidades.size();

        } catch (Exception e) {
            Log.error("Erro ao coletar Compete2030: " + e.getMessage(), e);
            return 0;
        }
    }

    private String extrairTexto(Element item, String seletores) {
        Elements elementos = item.select(seletores);
        return elementos.isEmpty() ? "" : elementos.first().text();
    }

    private String extrairDescricao(Element item) {
        String texto = item.text();
        if (texto.length() > 500) {
            return texto.substring(0, 500) + "...";
        }
        return texto;
    }

    private String extrairCodigo(String texto) {
        if (texto.contains("MPR-")) {
            int inicio = texto.indexOf("MPR-");
            return texto.substring(inicio, Math.min(inicio + 15, texto.length())).split(" ")[0];
        }
        return null;
    }

    private String extrairData(String texto) {
        String[] palavras = texto.split(" ");
        for (int i = 0; i < palavras.length - 2; i++) {
            String candidato = palavras[i] + " " + palavras[i+1] + " " + palavras[i+2];
            if (candidato.matches(".*\\d{2}/\\d{2}/\\d{4}.*")) {
                return candidato.replaceAll("[^0-9/]", "");
            }
        }
        return null;
    }

    private String extrairCategoria(String texto) {
        if (texto.toLowerCase().contains("internacionalização")) return "Internacionalização";
        if (texto.toLowerCase().contains("inovação")) return "Inovação";
        if (texto.toLowerCase().contains("qualificação")) return "Qualificação";
        if (texto.toLowerCase().contains("investigação")) return "I&D";
        return "Competitividade";
    }

    private String extrairTipoApoio(String texto) {
        if (texto.toLowerCase().contains("fundo perdido")) return "Fundo Perdido";
        if (texto.toLowerCase().contains("cofinanciamento")) return "Cofinanciamento";
        if (texto.toLowerCase().contains("incentivo")) return "Incentivo";
        return "Apoio Financeiro";
    }

    private LocalDate parseData(String dataTexto) {
        if (dataTexto == null || dataTexto.isEmpty()) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataTexto.trim(), formatter);
        } catch (Exception e) {
            return null;
        }
    }
}