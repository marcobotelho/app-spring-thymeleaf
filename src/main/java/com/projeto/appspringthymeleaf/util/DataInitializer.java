package com.projeto.appspringthymeleaf.util;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;
import com.projeto.appspringthymeleaf.model.ClienteModel;
import com.projeto.appspringthymeleaf.model.MunicipioModel;
import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.ClienteRepository;
import com.projeto.appspringthymeleaf.repository.MunicipioRepository;
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

        @Autowired
        private MunicipioRepository municipioRepository;

        @Override
        public void afterPropertiesSet() throws Exception {
                /* Criando os perfis */
                PerfilModel perfilAdmin = perfilRepository.findByNome("ROLE_ADMIN").get();
                if (perfilAdmin == null) {
                        perfilAdmin = new PerfilModel(null, "ROLE_ADMIN", "Administrador");
                        perfilAdmin = perfilRepository.save(perfilAdmin);
                }

                PerfilModel perfilUser = perfilRepository.findByNome("ROLE_USER").get();
                if (perfilUser == null) {
                        perfilUser = new PerfilModel(null, "ROLE_USER", "Usuário");
                        perfilUser = perfilRepository.save(perfilUser);
                }

                /* Criando os usuários */
                UsuarioModel usuario1 = usuarioRepository.findByEmail("admin@email.com").get();
                if (usuario1 == null) {
                        usuario1 = new UsuarioModel("Admin", "admin@email.com", passwordEncoder.encode("123"));
                        usuario1.setPerfis(List.of(perfilAdmin, perfilUser));
                        usuarioRepository.save(usuario1);
                }

                UsuarioModel usuario2 = usuarioRepository.findByEmail("user@email.com").get();
                if (usuario2 == null) {
                        usuario2 = new UsuarioModel("User", "user@email.com", passwordEncoder.encode("123"));
                        usuario2.setPerfis(List.of(perfilUser));
                        usuarioRepository.save(usuario2);
                }

                UsuarioModel usuario3 = usuarioRepository.findByEmail("marcobotelhoabreu@gmail.com").get();
                if (usuario3 == null) {
                        usuario3 = new UsuarioModel("Marco Botelho", "marcobotelhoabreu@gmail.com",
                                        passwordEncoder.encode("123"));
                        usuario3.setPerfis(List.of(perfilAdmin, perfilUser));
                        usuarioRepository.save(usuario3);
                }

                /* Criando estados e municipios dos arquivos CSV */
                if (municipioRepository.count() < 5000) {
                        csvServiceSql.lerCsvEInserirDados();
                }

                /* Criando os clientes */
                MunicipioModel municipio = municipioRepository.findById(3550308L).get(); // São Paulo
                ClienteModel cliente1 = clienteRepository.findByEmail("j@j.com").get();
                if (cliente1 == null) {
                        cliente1 = new ClienteModel("João", "j@j.com", 25, LocalDate.now(), "11111-111",
                                        "Rua 1 casa 1", "Centro", municipio);
                        cliente1.setTelefones(List.of(
                                        new TelefoneModel("(11) 11111-1111", TipoTelefoneEnum.CELULAR, cliente1),
                                        new TelefoneModel("(11) 22222-2222", TipoTelefoneEnum.COMERCIAL, cliente1)));
                        clienteRepository.save(cliente1);
                }
                ClienteModel cliente2 = clienteRepository.findByEmail("m@m.com").get();
                if (cliente2 == null) {
                        cliente2 = new ClienteModel("Maria", "m@m.com", 30, LocalDate.now(), "22222-222",
                                        "Rua 2 casa 2", "Centro", municipio);
                        cliente2.setTelefones(List.of(
                                        new TelefoneModel("(11) 33333-3333", TipoTelefoneEnum.RESIDENCIAL, cliente2),
                                        new TelefoneModel("(11) 44444-4444", TipoTelefoneEnum.CELULAR, cliente2)));
                        clienteRepository.save(cliente2);
                }
        }
}
