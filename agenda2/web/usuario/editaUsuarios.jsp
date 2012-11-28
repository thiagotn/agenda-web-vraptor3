<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>
<!-- 
Minhas Configura��es

Est� p�gina o usu�rio poder� editar um usu�rio ao localiza-lo na lista de usu�rios,
ao t�rmino da altera��o o site ser� redirecionar� para p�gina de lista de usu�rios caso
ocorra algum erro ser� exibido a informa��o de erro no topo da p�gina lista de usu�rios.
Est� p�gina ser� exibida como os dados dos usu�rios. N�o clique em enviar se voc� n�o redigitou
a sua senha de login, caso n�o digitada, o site ser� redicionada para p�gina de Lista Contatos
e exibir� um erro em seu topo. Se o usu�rio for admin ser� exibida a lista de usu�rios para que
ele edite, exclua e visualize os dados dos us�rios cadastrados.

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

<div class="tabela"><fmt:message key="titulo.editarusuario"/></div>
<form class="form" style="height: 250px" action="<c:url value="/usuario/alteraUsuarios.view"/>"  method="post">
    <fieldset>
        <input type="hidden" id="id" name="id" value="${id}">
        <input type="hidden" id="login" name="login" value="${login}">
        <p><label for="nome"><fmt:message key="campo.nome"/></label><input type="text" id="nome" name="nome" value="${nome}" style="width:300px"></p>
        <p><label for="email"><fmt:message key="campo.email"/></label><input type="text" id="email" name="email" value="${email}" style="width:300px"></p>
        <p><label for="senha"><fmt:message key="campo.senha"/></label><input type="password" id="senha" name="senha" value="<c:out value="null"/>" style="width:192px;"><span style="color: red"><fmt:message key="label.digitanovasenha"/></span></p>            
        <p><label for="alerta"><fmt:message key="campo.alertar"/></label></p>
        <select id="alerta" name="alerta">
            <option value="1"><fmt:message key="campo.1dia"/></option>  
            <option value="2"><fmt:message key="campo.2dias"/></option> 
            <option value="1"><fmt:message key="campo.3dias"/></option>
            <option value="1"><fmt:message key="campo.4dias"/></option>  
        </select>        
        <p><label for="senha"><fmt:message key="campo.senhagmail"/></label><input type="password" id="gmail" name="gmail" value="<c:out value="null"/>" style="width:192px"><span style="color: red"><fmt:message key="label.digitanovasenha"/></span></p>   
        <p><input type="submit" value="<fmt:message key="botao.enviar"/>" class="btn btn-primary" style="margin-left:95px"/></p>
    </fieldset>           
</form>
<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
