package com.tnogueira.agendaweb.modelo;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Past;

import br.com.caelum.stella.hibernate.validator.CPF;

@Entity
@Table(name="contato")
public class Contato {

	@Id @GeneratedValue
	private Integer id;

    @NotNull @NotEmpty @Length(min=5,max=50)
	private String nome;
	
    @NotNull @NotEmpty @Email
	private String email;

    @NotNull @NotEmpty 
	private String endereco;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
	private Calendar dataNascimento;
    
    @CPF(formatted=true) @NotNull @NotEmpty
    private String cpf;

    public Contato() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getDataNascimento() {
		return this.dataNascimento;
	}

	public void setDataNascimento(Calendar dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return this.endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}