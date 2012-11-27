<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include  file="../WEB-INF/jspf/cabecalho.jspf" %>
  
<c:choose>
    <c:when test="${sessionScope.lingua eq 'en_US'}">
        <fmt:setLocale value="en_US" />
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="pt_BR" /> 
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="org.ifsp.agenda.lingua.messages"/>

<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<link href="<c:url value="/template/css/bootstrap.css" />" rel="stylesheet"/>
<link href="<c:url value="/template/css/bootstrap-responsive.css"/>" rel="stylesheet"/>
<script src="/templete/js/jquery_min.js"></script>

<div class="imagem">
    <img src="<c:url value="/template/img/cal.png"/>"  name="figura"</img>
</div>

<div class="centro">
    <form action="login.view" method="post">
        <input type="hidden" name="acao" value="login"/>
        <fieldset>
            <p><label for="nome"><fmt:message key="campo.login"/> </label><input type="text" id="login" name="login"></p>
            <p><label for="endereco"><fmt:message key="campo.senha"/> </label><input type="password" id="senha" name="senha"></p>
            <input type="submit" value="<fmt:message key='botao.login'/>" class="btn btn-primary" style="margin-left: 67px; width: 164px;text-align: center" />
        </fieldset>
    </form>
</div>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
