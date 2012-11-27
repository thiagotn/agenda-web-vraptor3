package org.ifsp.agenda.mail;

import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 * 
 * Esta classe é usada para enviar emails para os contatos e usuário do site,
 * ele buscar algumas propridades no arquivo /mail.properties para configurar 
 * o smtp e a porta do servidor de email pelo metodo buildMailSession que autentica
 * o usuário com o método SimpleAuth e monta toda messagem o método buildMessage e
 * envia o email com o método enviarMensagem.
 * 
 * 
 */
public class Mensageiro {

    private static final Logger log = Logger.getLogger(Mensageiro.class);
    private static final Properties applicationProperties = new Properties();

    static {
        try {
            applicationProperties.load(Mensageiro.class.getResourceAsStream("/mail.properties"));
        } catch (Exception e) {
            log.error("problemas ao obter configurações de email: " + e.getMessage(), e);
        }
    }

    /**
     *
     * Monta o as propriedades da para que o email seja enviado, tais como o host,
     * a porta do servidor smtp e autentica o usuário e senha com um atenticador.
     * 
     * @param usuario
     * @return session
     */
    public Session buildMailSession(final Usuario usuario) {
        Properties mailProps = System.getProperties();
        mailProps.put("mail.smtp.host", applicationProperties.getProperty("mail.smtp.host"));
        mailProps.put("mail.smtp.port", applicationProperties.getProperty("mail.smtp.port"));
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.mime.charset", "ISO-8859-1");
        mailProps.put("mail.smtp.socketFactory.port", 465); //mesma porta para o socket  
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProps.put("mail.smtp.socketFactory.fallback", "false");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario.getEmail(), usuario.getSenhaEmail());
            }
        };
        Session session = Session.getDefaultInstance(mailProps, auth);
        return session;
    }


       /**
     *
     * Este método constrói a mensagem de email, definindo de quem é o email,
     * para que vai, o assunto e o tipo da mensagem.
     * 
     * @param mensagem 
     * @param usuario
     * @return session
     */ 
    
    private MimeMessage buildMessage(Mensagem mensagem, Usuario usuario) throws Exception {

        Session session = buildMailSession(usuario);
        MimeMessage message = new MimeMessage(session);
        //message.setFrom(new InternetAddress(applicationProperties.getProperty("mail.smtp.usuario").toString()));
        message.setFrom(new InternetAddress(usuario.getEmail()));
        message.setRecipients(Message.RecipientType.TO, mensagem.getPara());
        message.setSubject(mensagem.getAssunto());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(mensagem.getMensagem());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        message.setContent(mensagem.getMensagem(), "text/plain");

        return message;
    }

    /**
     *
     * Este método executa o envio do email e envia informações ao log,
     * lança um erro casso ocorra algum erro.
     * 
     * @param mensagem
     * @param usuario
     * @return
     */
    public boolean enviarMensagem(Mensagem mensagem, Usuario usuario) {
        try {

            final MimeMessage message = buildMessage(mensagem, usuario);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (Exception e) {
                        log.error("problemas ao enviar mensagem: " + e.getMessage(), e);
                    }
                }
            }).start();

            log.info(String.format("Enviando mensagem para: %s assunto: %s", mensagem.getPara(), mensagem.getAssunto()));
            return true;
        } catch (Exception e) {
            log.error("problemas ao enviar mensagem: " + e.getMessage(), e);
            return false;
        }
    }

        /**
     *
     * Calsse para autenticar um nome de usuário e senha que será
     * usada para autenticar o email e os retorna com o método
     * getPasswordAuthentication.
     */
    
    class SimpleAuth extends Authenticator {

        public String username;
        public String password;

        public SimpleAuth(String user, String pwd) {
            this.username = user;
            this.password = pwd;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    /**
     * Este método será usado para carregar um modelo de email
     * caso seja necessário utilizar um corpo de texto muito extenso.
     * @param fonte
     * @return
     */
    public String carregaTemplate(String fonte) {
        try {
            InputStream input = getClass().getResourceAsStream(fonte);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);

            return new String(buffer);
        } catch (Exception e) {
            log.error("problemas ao carregar template: " + e.getMessage(), e);
            return "";
        }
    }
}
