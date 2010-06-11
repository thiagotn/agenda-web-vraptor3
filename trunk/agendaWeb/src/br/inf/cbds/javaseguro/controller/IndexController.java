package br.inf.cbds.javaseguro.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.inf.cbds.javaseguro.dao.ContatoDao;
import br.inf.cbds.javaseguro.modelo.Contato;

@Resource
public class IndexController {

	private ContatoDao dao;
	private Result result;
	private HttpSession session;
	
	public IndexController(Result result, ContatoDao dao, HttpSession session){
		this.result = result;
		this.dao = dao;
		this.session = session;
	}
	
	@Path("/")
	public String index(){
		
		return "Bem vindo!";
	}
	
	
	public void buscar(String nome){
		
		List<Contato> contatos = dao.busca(nome);

		result.include("contatos", contatos);
		result.redirectTo(IndexController.class).index();
	}
	
	public void logout(){
		this.session.invalidate();
		result.redirectTo(IndexController.class).index();
	}
	
}
