package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class CompromissoDuplicadoException extends AgendaDAOException {

    private static final long serialVersionUID = 1L;

    /**
     *Está classe extende as Classe AgendaDAOException para verificar o compromissos duplicados
     */
    public CompromissoDuplicadoException() {
        super("Erro, Compromisso Duplicado! Escola outra data ou horário");
    }
}
