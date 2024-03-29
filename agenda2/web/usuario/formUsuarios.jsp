<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>
<!-- 

P�gina Inclus�o de Usu�rios

Est� p�gina o usu�rio poder� cadastrar um contato t�rmino da inser��o o site ser� redirecionar�
para p�gina de lista de contatos, caso ocorra algum erro ser� exibido a informa��o de erro no topo
da p�gina lista de contatos. Nela encontra-se um c�digo javascript para alterar os campos relacionados
ao tipo de contato. Ao adiconar um usu�rio o usu�rio admin somente entrar� com os dados mais importantes
a senha do Gmail ser� alterada pelo usu�rio cadastrado clicando em minhas configura��es. Um email ser�
enviado para usu�rio para que ele use o site, informando o ip e o login e senha para que ele utilize a
Agenda.

-->


<%@ include  file="../WEB-INF/jspf/cabecalho.jspf" %>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="<c:url value="/template/css/main.css" />" />        
<link href="<c:url value="/template/css/bootstrap-responsive.css"/>" rel="stylesheet">        
<link href="<c:url value="/template/css/bootstrap.css"/>" rel="stylesheet">
<script src="<c:url value="/template/js/jquery_min.js"/>"></script>

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

<div class="tabela"><fmt:message key="titulo.adicionausuarios"/></div>
<form class="form" style="height: 295px" name="form" action="<c:url value="/usuario/adicionaUsuarios.view"/>"  method="post">
    <fieldset name="caixas">
        <p><label for="login"><fmt:message key="campo.login"/></label><input type="text" id="login" name="login"></p>
        <p><label for="nome"><fmt:message key="campo.nome"/></label><input type="text" id="nome" name="nome" style="width:300px"></p>
        <p><label for="email"><fmt:message key="campo.email"/></label><input type="text" id="email" name="email" style="width:300px"></p>
        <p><label for="senha"><fmt:message key="campo.senha"/></label><input type="password" id="senha" name="senha" value="null" style="width:192px"></p>
        <p><label for="alerta"><fmt:message key="campo.alertar"/></label>
            <select id="alerta" name="alerta">
                <option value="1"><fmt:message key="campo.1dia"/></option>  
                <option value="2"><fmt:message key="campo.2dias"/></option> 
                <option value="1"><fmt:message key="campo.3dias"/></option>
                <option value="1"><fmt:message key="campo.4dias"/></option>  
        </p></select>
        <p><label for="senha"><fmt:message key="campo.senhagmail"/></label><input type="password" id="gmail" name="gmail" value="null" style="width:192px"></p>        
        <p><input type="submit" value="<fmt:message key="botao.enviar"/>" class="btn btn-primary" style="margin-left:95px"/></p>        
    </fieldset>  
</form>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
