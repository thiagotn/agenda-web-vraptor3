package com.tnogueira.agendaweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.tnogueira.agendaweb.modelo.Contato;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ContatoDao {

	private static final Logger log = Logger.getLogger(ContatoDao.class);
	
	private final Session session; 
	
	public ContatoDao(Session session){
		this.session = session;
	}
	
	public void salva(Contato contato) {
		Transaction tx = session.beginTransaction();
		session.save(contato);
		tx.commit();
		log.info("Contato " + contato.getNome() + " salvo com sucesso!");
	}
	
	@SuppressWarnings("unchecked")
	public List<Contato> listaTudo(){
		return this.session.createCriteria(Contato.class).list();
	}
	
	public Contato carrega(Integer id) {
		return (Contato) this.session.load(Contato.class, id);
	}
	
	public void atualiza(Contato contato) {
		Transaction tx = session.beginTransaction();
		session.update(contato);
		tx.commit();
		log.info("Contato " + contato.getNome() + " atualizado com sucesso!");
	}
	
	public void remove(Contato contato) {
		Transaction tx = session.beginTransaction();
		this.session.delete(contato);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Contato> busca(String nome) {
		System.out.println(nome);
		return session.createCriteria(Contato.class)
			.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE))
			.list();
	}

	public void recarrega(Contato contato) {
		session.refresh(contato);	
	}
}	