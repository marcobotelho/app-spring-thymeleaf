package com.projeto.appspringthymeleaf.util;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;
import com.projeto.appspringthymeleaf.model.RoleModel;
import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.service.RoleService;
import com.projeto.appspringthymeleaf.service.UsuarioService;

@Component
public class DataInitializer implements InitializingBean {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {

        RoleModel roleAdmin = new RoleModel("ROLE_ADMIN");
        roleService.save(roleAdmin);

        RoleModel roleUser = new RoleModel("ROLE_USER");
        roleService.save(roleUser);

        UsuarioModel usuario1 = new UsuarioModel("João", "j@j.com", passwordEncoder.encode("123456"), 25,
                LocalDate.now());
        usuario1.setRoles(List.of(roleAdmin, roleUser));
        usuario1.setTelefones(List.of(new TelefoneModel("(11) 11111-1111", TipoTelefoneEnum.CELULAR, usuario1),
                new TelefoneModel("(11) 22222-2222", TipoTelefoneEnum.COMERCIAL, usuario1)));
        usuarioService.save(usuario1);

        UsuarioModel usuario2 = new UsuarioModel("Maria", "m@m.com", passwordEncoder.encode("123456"), 30,
                LocalDate.now());
        usuario2.setRoles(List.of(roleUser));
        usuario2.setTelefones(List.of(new TelefoneModel("(11) 33333-3333", TipoTelefoneEnum.RESIDENCIAL, usuario2),
                new TelefoneModel("(11) 44444-4444", TipoTelefoneEnum.CELULAR, usuario2)));
        usuarioService.save(usuario2);
    }
}
