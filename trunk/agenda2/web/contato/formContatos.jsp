<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>
<!-- 

Página Inclusão de Contatos

Está página o usuário poderá cadastrar um contato término da inserção o site será redirecionará
para página de lista de contatos, caso ocorra algum erro será exibido a informação de erro no topo
da página lista de contatos. Nela encontra-se um código javascript para alterar os campos relacionados
ao tipo de contato.

-->

<%@ include  file="../WEB-INF/jspf/cabecalho.jspf" %>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="<c:url value="/template/js/jquery_min.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/template/css/main.css" />" />        
<link href="<c:url value="/template/css/bootstrap-responsive.css"/>" rel="stylesheet">        
<link href="<c:url value="/template/css/bootstrap.css"/>" rel="stylesheet">


<script type="text/javascript">
    
    function alimentarform() {
        var tipo = document.getElementById("tipo");
        if (tipo.value=="pessoal") {
            document.getElementById("cel").style.display = "inline";
            document.getElementById("emp").style.display = "none";
            document.getElementById("form").style.height = "315px";
        } else {
            document.getElementById("emp").style.display = "inline";
            document.getElementById("cel").style.display = "none";
            document.getElementById("form").style.height = "280px";
        }
    }
</script>

<c:choose>
    <c:when test="${sessionScope.lingua eq 'en_US'}">
        <fmt:setLocale value="en_US" />
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="pt_BR" /> 
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="org.ifsp.agenda.lingua.messages"/>

<title><fmt:message key="site.titulo"/></title>

<div class="tabela"><fmt:message key="titulo.adicionarcontatos"/></div>
<form  class="form" id="form"style="height: 340px" action="<c:url value="/contato/adicionaContatos.view"/>"  method="post">
    <fieldset>
        <p><label><fmt:message key="campo.contato"/></label></p>
        <select style="width:165px" id="tipo" name="tipo" onChange="alimentarform();">
            <option value="pessoal"><fmt:message key="tipo.contatopessoal"/></option>
            <option value="profissional"><fmt:message key="tipo.contatoprofissional"/></option>
        </select>
    </fieldset>	
    <fieldset>
        <p><label for="nome"><fmt:message key="campo.nome"/></label><input type="text" id="nome" name="nome" style="width:300px"></p>
        <p><label for="endereco"><fmt:message key="campo.endereco"/></label><input type="text" id="endereco" name="endereco" style="width:300px"></p>
        <p><label for="telefone"><fmt:message key="campo.telefone"/></label><input type="text" id="telefone" name="telefone" style="width:90px"></p>
        <div id="cel" style="display:inline">
            <p><label for="celular" ><fmt:message key="campo.celular"/></label><input type="text" id="celular" name="celular" style="width:100px"></p>
            <p><label for="data"><fmt:message key="campo.aniversario"/></label><input type="text" id="data" name="data" style="width:100px"></p>
        </div>
        <p><label for="email"><fmt:message key="campo.email"/></label><input type="text" id="email" name="email" style="width:180px"></p>
        <div id="emp" style="display:none">
            <p><label for="empresa"><fmt:message key="campo.empresa"/></label><input type="text" id="empresa" name="empresa" style="width:250px"></p>	
        </div>
    <p><input type="submit" value="<fmt:message key="botao.enviar"/>" class="btn btn-primary" style="margin-left:95px"/></p>          
    </fieldset>         
</form>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
