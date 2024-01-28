package com.ru.jdevrestapi.security;

import com.ru.jdevrestapi.ApplicationContextLoad;
import com.ru.jdevrestapi.model.Usuario;
import com.ru.jdevrestapi.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class JWTTokenAutenticacaoService {


    /* TEMPO DE VALIDADE DO TOKEN EM MILISEGUNDOS (2 DIAS)*/
    private static final long EXPIRATION_TIME = 172800000;

    /* UMA SENHA UNICA PARA COMPOR A AUTENTICAÇÃO */
    private static final String SECRET = "SenhaSeguraESecreta";

    /* Prefixo padrão de token */
    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    /* Gerando o token de autenticação adicionado ao cabeçalho e resposta Http */
    public void addAuthentication(HttpServletResponse response, String username) throws IOException {
        /* Montagem do Token */
        String JWT = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512,SECRET).compact();

        String token = TOKEN_PREFIX+" "+JWT;

        /* ADICIONA O CABEÇALHO http*/
        response.addHeader(HEADER_STRING,token);

        /* Atualiza token no banco de dados */
        ApplicationContextLoad.getApplicationContext()
                .getBean(UsuarioRepository.class).atualizaTokenUser(JWT,username);

        liberacaoCors(response);

        /* Escreve token como resposta no corpo http */
        response.getWriter().write("{\"Authorization\":\""+token+"\"}");
    }

    /* Retorna o usuário validado com token ou caso não seja valido retorna null */
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        /* Pega o token enviado no cabeçalho http */
        String token = request.getHeader(HEADER_STRING);
        try{
            if (token != null) {
                String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
                /* Faz a validação do token do usuario na requisição */
                String user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(tokenLimpo)
                        .getBody().getSubject();
                if (user != null) {
                    Usuario usuario = ApplicationContextLoad.getApplicationContext()
                            .getBean(UsuarioRepository.class).findUserByLogin(user);
                    if (usuario != null) {
                        if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
                            return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
                        }
                    }
                }
            }//Fim da condição
        }catch(ExpiredJwtException ex){
            String msg="Seu TOKEN esta expirado, faca login ou informe um novo TOKEN para autenticacao";
            try {
                response.getOutputStream().println(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        liberacaoCors(response);
        return null; /* Não autorizado */

    }

    private void liberacaoCors(HttpServletResponse response) {
        if (response.getHeader("Access-Control-Allow-Origin")==null){
            response.addHeader("Access-Control-Allow-Origin","*");
        }

        if (response.getHeader("Access-Control-Allow-Headers")==null){
            response.addHeader("Access-Control-Allow-Headers","*");
        }

        if (response.getHeader("Access-Control-Request-Headers")==null){
            response.addHeader("Access-Control-Request-Headers","*");
        }

        if (response.getHeader("Access-Control-Allow-Methods")==null){
            response.addHeader("Access-Control-Allow-Methods","*");
        }
    }
}
