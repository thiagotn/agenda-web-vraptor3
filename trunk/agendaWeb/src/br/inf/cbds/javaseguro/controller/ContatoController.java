package br.inf.cbds.javaseguro.controller;

import java.util.List;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Hibernate;
import br.com.caelum.vraptor.view.Results;
import br.inf.cbds.javaseguro.dao.ContatoDao;
import br.inf.cbds.javaseguro.mail.Mensageiro;
import br.inf.cbds.javaseguro.mail.Mensagem;
import br.inf.cbds.javaseguro.modelo.Contato;

@Resource
public class ContatoController {

	private Result result;
	private ContatoDao dao;
	private Validator validator;
	
	public ContatoController(Result result,ContatoDao dao, Validator validator){
		this.result = result;
		this.dao = dao;
		this.validator = validator;
	}
	
	public void form(){
		
	}
	
	public void adiciona(Contato contato){
		validator.addAll(Hibernate.validate(contato));
		validator.onErrorUse(Results.page()).of(ContatoController.class).form();
		
		dao.salva(contato);
		result.redirectTo(ContatoController.class).lista();
	}
	
	public List<Contato> lista(){

		return dao.listaTudo();
	}
	
	public void altera(Contato contato){
		validator.addAll(Hibernate.validate(contato));
		validator.onErrorUse(Results.page()).of(ContatoController.class).edita(contato.getId());

		dao.atualiza(contato);
		result.redirectTo(ContatoController.class).lista();
	}
	
	public Contato edita(Integer id){
		return dao.carrega(id);
	}
	
	public void remove(Integer id){
		Contato contato  = dao.carrega(id);
		dao.remove(contato);
		result.redirectTo(ContatoController.class).lista();
	}

	public void formEmail(){
		
	}
	
	public void reenvioSenha(String email){
		
		String senha = "123456";
		
		Mensageiro mensageiro = new Mensageiro();
		String template = mensageiro.carregaTemplate("/contatos.mail");
		String mensagemTxt = String.format(template,email,senha);
		Mensagem mensagem = new Mensagem();
		mensagem.setDe("AgendaWeb");
		mensagem.setPara(email);
		mensagem.setAssunto("AgendaWeb - : Reenvio de senha;");
		mensagem.setMensagem(mensagemTxt);
		mensageiro.enviarMensagem(mensagem);
	}
}
