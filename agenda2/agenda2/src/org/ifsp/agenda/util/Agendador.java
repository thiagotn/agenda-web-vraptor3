/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifsp.agenda.util;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.CompromissoInexistenteException;
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.jdbc.dao.UsuarioDAO;
import org.ifsp.agenda.mail.Mensageiro;
import org.ifsp.agenda.mail.Mensagem;
import org.ifsp.agenda.modelo.Aviso;
import org.ifsp.agenda.modelo.Usuario;

public class Agendador extends TimerTask {
    /**
     *
     * @author Ton
     *
     *Esta Classe extends a classe TimerTask é será utilizada para enviar email ao
     * usuário que será executado pelo ServletListener do Tomcat.
     * Ela criará uma lista de avisos que é uma classe que terá todos os atributos 
     * necessários para que um email seja enviado. Ela armazena todos os usuários
     * e seus compromissos e executa o método MaillerCompromisso da Classe CompromissoDAO.
     * Ele roda o MaillerCompromisso até que não exista nenhum compromisso com status seja 1.
     * 
     */
    
    @Override
    public void run() {
        //Cria a lista de avisos
        List<Aviso> avisos = null;
        //instancia CompromissoDAO para executar operações no banco de dados
        CompromissoDAO compromissoDAO = null;
        UsuarioDAO usuarioDAO = null;
        //Cria a lista de usuários
        List<Usuario> usuarios = null;
        try {
            //Adiciona todos os compromissos a lista avisos
            compromissoDAO = new CompromissoDAO();
            avisos = compromissoDAO.MaillerCompromisso();            
        } catch (SQLException ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CompromissoInexistenteException ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AgendaDAOException ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //Cria lista de usuário
            usuarioDAO = new UsuarioDAO();
            usuarios = usuarioDAO.getLista();
        } catch (SQLException | AgendaDAOException ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //Se lista de avisos for maior que zero todos os compromisso são
            //enviados para cada usuário
            if (avisos.size() > 0) {
                for (Usuario u : usuarios) {
                    for (Aviso a : avisos) {
                        u.setSenhaEmail(a.getSenhaGmail());                        
                        enviarEmailNovoUsuario(a, u);
                    }
                }
            }
        } catch (AuthenticationFailedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void enviarEmailNovoUsuario(Aviso aviso, Usuario usuario) throws AuthenticationFailedException {
        try {
            //Metodos para criar o assunto, a mensagem e enviar e-mail para o usuário
            Mensageiro mensageiro = new Mensageiro();
            String msg = "";
            msg += String.format("Olá %s,", aviso.getUsuarioNome());
            msg += String.format("você tem um compromisso com %s, ", aviso.getCompromissoContato());
            msg += String.format("às %s dia %s ", aviso.getCompromissoHora(), aviso.getCompromissoData());
            msg += String.format("no(a) %s, sobre %s", aviso.getCompromissoLocal(), aviso.getCompromissoDescricao());
            msg += "Web Agenda - Aviso de Compromissos";
            Mensagem mensagem = new Mensagem();
            mensagem.setPara(usuario.getEmail());
            mensagem.setAssunto("Olá " + usuario.getNome() + " - Você tem um aviso de compromisso com " + aviso.getCompromissoContato());
            mensagem.setMensagem(msg);
            mensageiro.enviarMensagem(mensagem, usuario);
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Ocorreu um erro de altenticação" + ex);
        }
    }
}
