package com.ru.jdevrestapi.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class ServiceEnviaEmail {
    private String userName = "seu.email@gmail.com";
    private String senha = "Sua senha";

    public void enviarEmail(String assunto, String emailDestino, String mensagem) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "*"); //Autorização
        properties.put("mail.smtp.auth", "true"); //Autorização
        properties.put("mail.smtp.starttls", "true");//Autenticação
        properties.put("mail.smtp.host", "smtp.gmail.com");//Servidor google
        properties.put("mail.smtp.port", "465");//Porta servidor
        properties.put("mail.smtp.socketFactory.port", "465");//Porta conectada por socket
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//Classe de conexão socket

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, senha);
            }
        });
        Address[] toUser = InternetAddress.parse(emailDestino);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName)); //Quem está enviando
        message.setRecipients(Message.RecipientType.TO, toUser);// Para quem vai o Email
        message.setSubject(assunto);//Assunto do email
        message.setText(mensagem);
        Transport.send(message);
    }
}
