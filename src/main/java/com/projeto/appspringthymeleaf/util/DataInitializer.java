package com.projeto.appspringthymeleaf.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.projeto.appspringthymeleaf.enums.TipoTelefoneEnum;
import com.projeto.appspringthymeleaf.model.TelefoneModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.service.UsuarioService;

@Component
public class DataInitializer implements InitializingBean{

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void afterPropertiesSet() throws Exception {
        UsuarioModel usuario = new UsuarioModel();
        List<TelefoneModel> telefones = new ArrayList<>();

        usuario = new UsuarioModel(null, "João", "j@j.com", 25, LocalDate.now());

        telefones.clear();
        telefones.add( new TelefoneModel(null, "(11) 11111-1111", TipoTelefoneEnum.CELULAR, usuario) );
        telefones.add( new TelefoneModel(null, "(11) 22222-2222", TipoTelefoneEnum.COMERCIAL, usuario) );
        usuario.setTelefones(telefones);

        usuarioService.save(usuario);

        usuario = new UsuarioModel(null, "Maria", "m@m.com", 30, LocalDate.now());

        telefones.clear();
        telefones.add( new TelefoneModel(null, "(11) 33333-3333", TipoTelefoneEnum.RESIDENCIAL, usuario) );
        telefones.add( new TelefoneModel(null, "(11) 44444-4444", TipoTelefoneEnum.CELULAR, usuario) );
        usuario.setTelefones(telefones);

        usuarioService.save(usuario);
    }
    
}
