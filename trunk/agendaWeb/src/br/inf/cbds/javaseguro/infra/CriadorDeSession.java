package br.inf.cbds.javaseguro.infra;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class CriadorDeSession implements ComponentFactory<Session> {

	private SessionFactory factory;
	private Session session;

	public CriadorDeSession(SessionFactory factory){
		this.factory = factory;
	}
	
	public Session getInstance() {
		return this.session;
	}
	
	@PostConstruct
	public void abre(){
		this.session = factory.openSession();
		System.out.println("Abrindo Conexão com ao banco de dados...........");
	}
	
	@PreDestroy
	public void fecha(){
		this.session.close();
		System.out.println("Fechando Conexão com ao banco de dados...........");
	}
	
}
