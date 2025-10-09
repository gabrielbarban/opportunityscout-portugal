package com.oportunidades.config;

import com.oportunidades.entity.Usuario;
import com.oportunidades.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StartupBean {

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        Log.info("Iniciando aplicação...");

        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.email = "admin@oportunidades.com";
            admin.password = BcryptUtil.bcryptHash("admin123");
            admin.nome = "Administrador";
            admin.role = "ADMIN";
            usuarioRepository.persist(admin);
            
            Log.info("Usuário admin criado: admin@oportunidades.com / admin123");
        }
    }
}