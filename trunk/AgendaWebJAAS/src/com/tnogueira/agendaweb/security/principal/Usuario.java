package com.tnogueira.agendaweb.security.principal;

import java.security.Principal;
import java.util.Set;

public class Usuario implements Principal
{
	private String name;
	
	private Set<Perfil> perfis;
	
	public Usuario(String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(Set<Perfil> perfis) {
		this.perfis = perfis;
	}
}
