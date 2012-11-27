
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>
<%@include file="../WEB-INF/jspf/cabecalho.jspf"%>
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
<fmt:message key="erro.pagina"/> 
<%@ include file="../WEB-INF/jspf/rodape.jspf" %>