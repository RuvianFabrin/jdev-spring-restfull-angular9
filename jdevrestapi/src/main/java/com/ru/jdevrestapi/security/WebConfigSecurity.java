package com.ru.jdevrestapi.security;

import com.ru.jdevrestapi.service.ImplementacaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/* Mapeia URL, enderecos, autoriza ou bloqueia acesso a URL */
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    /* Configura as solicitações de acesso po Http*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* Ativando a proteção contra usuários que não estão validados por token */
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                /* Ativando a permissão para acesso a tela inicial do sistema */
                .disable().authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/index", "/recuperar/**").permitAll()

                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                /* Redireciona após o User deslogar do sistema */
                .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

                /* Mapeia URL de Logout e invalida o Usuário */
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

                /* Filtra requisições de login para autenticação */
                .and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                /* Filtra demais requisições para verificar a presença do TOKEN JWT no HEADER HTTP */
                .addFilterBefore(new JwtApiAutenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(implementacaoUserDetailsService)
                /* Padrão de codificação de senha*/
               .passwordEncoder(new BCryptPasswordEncoder());
    }
}
