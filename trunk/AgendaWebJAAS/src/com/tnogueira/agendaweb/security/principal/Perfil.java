package com.tnogueira.agendaweb.security.principal;

import java.security.Principal;

public class Perfil implements Principal 
{
	private String name;
	
	public Perfil(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
