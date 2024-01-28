package com.ru.jdevrestapi.controller;

import com.ru.jdevrestapi.ObjetoErro;
import com.ru.jdevrestapi.model.Usuario;
import com.ru.jdevrestapi.repository.UsuarioRepository;
import com.ru.jdevrestapi.service.ServiceEnviaEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RequestMapping(value = "/recuperar")
public class RecuperaController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServiceEnviaEmail serviceEnviaEmail;

    @ResponseBody
    @PostMapping(value = "/")
    public ResponseEntity<ObjetoErro> recuperar(@RequestBody Usuario usuario) throws Exception {
        ObjetoErro objetoErro = new ObjetoErro();
        Usuario user = usuarioRepository.findUserByLogin(usuario.getLogin());
        if (user == null) {
            objetoErro.setCode("404");/*Não encontrado*/
            objetoErro.setError("Usuário não encontrado");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String senhaNova = dateFormat.format(Calendar.getInstance().getTime());

            usuarioRepository.updateSenha(new BCryptPasswordEncoder().encode(senhaNova), user.getId());

            serviceEnviaEmail.enviarEmail("Recuperação de Senha.", user.getLogin(), "Sua nova senha é : " + senhaNova);
            /*Rotina de envio de Email aula 09:54*/
            objetoErro.setCode("200");
            objetoErro.setError("Acesso enviado para seu e-mail.");
        }

        return new ResponseEntity<ObjetoErro>(objetoErro, HttpStatus.OK);
    }
}
