package com.projeto.appspringthymeleaf.util;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;
import com.projeto.appspringthymeleaf.model.ClienteModel;
import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.ClienteRepository;
import com.projeto.appspringthymeleaf.repository.PerfilRepository;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;
import com.projeto.appspringthymeleaf.service.CsvServiceSql;

@Component
public class DataInitializer implements InitializingBean {

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private PerfilRepository perfilRepository;

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CsvServiceSql csvServiceSql;

        @Override
        public void afterPropertiesSet() throws Exception {
                /* Criando os perfis */
                PerfilModel perfilAdmin = new PerfilModel("ROLE_ADMIN", "Administrador");
                perfilAdmin = perfilRepository.save(perfilAdmin);
                PerfilModel perfilUser = new PerfilModel("ROLE_USER", "Usuário");
                perfilUser = perfilRepository.save(perfilUser);

                /* Criando os usuários */
                UsuarioModel usuario1 = new UsuarioModel("Admin", "admin@email.com", passwordEncoder.encode("123"));
                usuario1.setPerfis(List.of(perfilAdmin, perfilUser));
                usuarioRepository.save(usuario1);

                UsuarioModel usuario2 = new UsuarioModel("User", "user@email.com", passwordEncoder.encode("123"));
                usuario2.setPerfis(List.of(perfilUser));
                usuarioRepository.save(usuario2);

                /* Criando os clientes */
                ClienteModel cliente1 = new ClienteModel("João", "j@j.com", 25,
                                LocalDate.now(), "11111-111", "Rua 1 casa 1", "Centro", "São Paulo", "SP");
                cliente1.setTelefones(List.of(new TelefoneModel("(11) 11111-1111", TipoTelefoneEnum.CELULAR, cliente1),
                                new TelefoneModel("(11) 22222-2222", TipoTelefoneEnum.COMERCIAL, cliente1)));
                clienteRepository.save(cliente1);

                ClienteModel cliente2 = new ClienteModel("Maria", "m@m.com", 30,
                                LocalDate.now(), "22222-222", "Rua 2 casa 2", "Centro", "São Paulo", "SP");
                cliente2.setTelefones(List.of(
                                new TelefoneModel("(11) 33333-3333", TipoTelefoneEnum.RESIDENCIAL, cliente2),
                                new TelefoneModel("(11) 44444-4444", TipoTelefoneEnum.CELULAR, cliente2)));
                clienteRepository.save(cliente2);

                /* Criando estados e municipios dos arquivos CSV */
                csvServiceSql.lerCsvEInserirDados();

        }
}
