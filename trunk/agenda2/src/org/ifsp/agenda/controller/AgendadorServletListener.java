/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ifsp.agenda.controller;

import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.ifsp.agenda.util.Agendador;

/**
 *
 * @author Ton
 */
public class AgendadorServletListener implements ServletContextListener {

    /*
     *
     * Este servlet é um agendador que ficará executando a tarefa avisos para
     * enviar enviar e-mails sobre compromissos aos usuários cadastrados no sistema
     * Ele usa a Classe Timer para avisar ao servidor Tomcat através do recurso
     * ServletContextListener (recurso nativo do Tomcat o que faz com que o servidor fique
     * escutando o seu contéudo até que a tarefa seja parada).
     * Ele instancia a Classe Agendador que extende a Classe TimerTask.
     * Ele configura o Delay para que o TimerTask seja executado.
     */
    
    private static final long DELAY = 60 * 1000;
    private Timer avisos;

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        avisos = new Timer();
        avisos.schedule(new Agendador(), 0, DELAY);
        System.out.println("Serviço de Aviso de Compromissos - INICIADO");
    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        avisos.cancel();
        System.out.println("Serviço de Aviso de Compromissos - PARADO");
    }
}
