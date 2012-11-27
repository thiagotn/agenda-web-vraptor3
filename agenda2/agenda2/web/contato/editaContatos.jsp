<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>

<!-- 
P�gina Edi��o de Contatos

Est� p�gina o usu�rio poder� editar um contato ao localiza-lo na lista de compromissos,
ao t�rmino da altera��o o site ser� redirecionar� para p�gina de lista de contatos caso
ocorra algum erro ser� exibido a informa��o de erro no topo da p�gina lista de contatos.

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

<script type="text/javascript">
    $(function verifica() { 
        var tipoClasse = document.getElementById("tipo").innerHTML;
        if (tipoClasse=="pessoal") {
            document.getElementById("cel").style.display = "inline";
            document.getElementById("emp").style.display = "none";
        } else {
            document.getElementById("emp").style.display = "inline";
            document.getElementById("cel").style.display = "none";
        }
    });
</script>

<span id="tipo" name="classe" style="display:none">${classe}</span>

<body onload="verifica()">
    <div class="tabela"><fmt:message key="titulo.editarcontato"/></div>
    <form class="form" style="height: 300px" action="<c:url value="/contato/alteraContatos.view"/>" method="post">
        <fieldset>
            <input type="hidden" name="classe" size="30" value="${classe}"/>
            <input type="hidden" name="id" size="30" value="${id}"/>
            <p><label for="nome"><fmt:message key="campo.nome"/></label><input type="text" name="nome" value="${nome}" style="width:300px"/></p>
            <p><label for="endereco"><fmt:message key="campo.endereco"/></label><input type="text" name="endereco" value="${endereco}" style="width:300px"/></p>
            <p><label for="telefone"><fmt:message key="campo.telefone"/></label><input type="text" name="telefone" value="${telefone}" style="width:100px"/></p>
            <div id="cel" style="display:inline">
                <p><label for="celular" ><fmt:message key="campo.celular"/></label><input type="text" name="celular" value="${celular}" style="width:100px"/></p>
                <p><label for="data"><fmt:message key="campo.aniversario"/></label><input type="text" name="data" style="width:100px" value="<fmt:formatDate value="${data}" pattern="dd/MM/yyyy"/>"/></p>
            </div>
            <p><label for="email"><fmt:message key="campo.email"/></label><input type="text" name="email" value="${email}" style="width:180px"/></p>
            <div id="emp" style="display:none">
                <p><label for="empresa"><fmt:message key="campo.empresa"/></label><input type="text" name="empresa" value="${empresa}" style="width:250px"/></p>	
            </div>
        </fieldset>
        <p><input type="submit" value="<fmt:message key="botao.enviar"/>" class="btn btn-primary" style="margin-left:95px"/></p>            
    </form>
</body>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
