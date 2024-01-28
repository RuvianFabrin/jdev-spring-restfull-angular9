package com.ru.jdevrestapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ru.jdevrestapi.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Estabelece o nosso gerenciador de Token */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    /* Obrigamos a autenticar a url */
    protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
        /* Obriga a autenticar a url */
        super(new AntPathRequestMatcher(url));

        /* Gerenciador de autenticação */
        setAuthenticationManager(authenticationManager);
    }

    /* Retorna o usuario ao processar a autenticação */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        /* Está pegando o token para validar */
        Usuario user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), Usuario.class);

        /* Retorna o usuario login, senha e acessos */
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
    }
}
