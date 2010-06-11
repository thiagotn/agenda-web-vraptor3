package br.inf.cbds.javaseguro.mail;

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

public class Mensageiro 
{
	private static final Logger log = Logger.getLogger(Mensageiro.class);
	private static Properties applicationProperties = new Properties();
	
	static
	{
		try
		{
			applicationProperties.load(Mensageiro.class.getResourceAsStream("/mail.properties"));
		}
		catch (Exception e) 
		{
			log.error("problemas ao obter configurações de email: "+e.getMessage(), e);
		}
	}
	
	private Session buildMailSession() 
	{
		Properties mailProps = System.getProperties();   
        mailProps.put("mail.smtp.host", applicationProperties.getProperty("mail.smtp.host"));   
        mailProps.put("mail.smtp.port", applicationProperties.getProperty("mail.smtp.port"));
        mailProps.put("mail.smtp.auth", "true");   
        mailProps.put("mail.mime.charset", "ISO-8859-1");   
        
        mailProps.put("mail.smtp.socketFactory.port", 465); //mesma porta para o socket  
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        mailProps.put("mail.smtp.socketFactory.fallback", "false");

        Authenticator auth = new SingleAuthenticator();   
        Session session = Session.getDefaultInstance(mailProps, auth); 

        return session;
	}	
	
	private MimeMessage buildMessage(Mensagem mensagem) throws Exception
	{
		Session session = buildMailSession();
		MimeMessage message = new MimeMessage(session);  
		message.setFrom(new InternetAddress(applicationProperties.getProperty("mail.smtp.usuario").toString(),mensagem.getDe()));
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
	
	public boolean enviarMensagem(Mensagem mensagem)
	{
		try
		{
			final MimeMessage message = buildMessage(mensagem);
			
			new Thread(new Runnable()
			{
				public void run() 
				{
					try 
					{
						Transport.send(message);
					}
			        catch (Exception e) 
			        {
			        	log.error("problemas ao enviar mensagem: "+e.getMessage(), e);			        
					}					
				}
			}).start();
            
            log.info(String.format("Enviando mensagem para: %s assunto: %s", mensagem.getPara(), mensagem.getAssunto()));
            return true;
		}
		catch (Exception e) 
	    {
	      	log.error("problemas ao enviar mensagem: "+e.getMessage(), e);
	      	return false;
	    }
	}
	
    public class SingleAuthenticator extends Authenticator 
    {   
        public PasswordAuthentication getPasswordAuthentication() 
        {
        	String usuario = applicationProperties.getProperty("mail.smtp.usuario");
        	String senha = applicationProperties.getProperty("mail.smtp.senha");        	
            return new PasswordAuthentication(usuario, senha);   
        }   
    }
    
    public String carregaTemplate(String fonte)
    {
    	try 
    	{
    		InputStream input = getClass().getResourceAsStream(fonte);
    		byte[] buffer = new byte[input.available()];
    		input.read(buffer);
	    	
    		return new String(buffer);
		} 
    	catch (Exception e) 
    	{
    		log.error("problemas ao carregar template: "+e.getMessage(), e);
    		return "";
		}	
    }
}