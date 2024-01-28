package com.ru.jdevrestapi.service;

import com.ru.jdevrestapi.model.Usuario;
import com.ru.jdevrestapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* Consultar no banco o usuario */
        Usuario usuario = usuarioRepository.findUserByLogin(username);

        if (usuario == null){
            throw new UsernameNotFoundException("Usuário não foi encontrado");
        }
        return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
    }

    public void insereAcessoPadrao(Long id) {
        /* Feito assim porque não estava deixando inserir direto com ROLES duplicados para usuarios diferentes */
        String constraint = usuarioRepository.consultaConstraintRole();
        if(constraint != null){
            jdbcTemplate.execute("alter table usuario_role DROP CONSTRAINT " + constraint);
        }
        usuarioRepository.insereAcessoRolePadrao(id);
    }
}
