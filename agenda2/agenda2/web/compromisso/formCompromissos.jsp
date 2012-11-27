<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>

<!-- 

Página Inclusão de Compromissos

Está página o usuário poderá cadastrar um comprimissoao término da inclusão o site será redirecionará
para página de lista de compromissos, caso ocorra algum erro será exibido a informação de erro no topo
da página lista de compromissos. Esta página usa tags jstl para carregar a lista de contatos do banco de
dados e carrega um lista com datas de de hoje a 30 dias e os horários. Ocorrerá um erro se a lista de 
contatos estevir vazia.

-->

<%@ include  file="../WEB-INF/jspf/cabecalho.jspf" %>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="<c:url value="/template/css/main.css" />" />        
<link href="<c:url value="/template/css/bootstrap-responsive.css"/>" rel="stylesheet">        
<link href="<c:url value="/template/css/bootstrap.css"/>" rel="stylesheet">

<script type="text/javascript" language="javascript" src="<c:url value="/template/js/ajax.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/template/js/jquery.js"/>"></script>

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

<div class="tabela"><fmt:message key="titulo.adicionarcompromissos"/></div>

<form class="form" style="height: 310px" action="<c:url value="/compromisso/adicionaCompromissos.view"/>"  method="post">
    <fieldset>
        <jsp:useBean id="data" class="java.util.Date"/>  
        <p><label for="data"><fmt:message key="campo.data"/></label>
        <select name="data">  
            <c:forEach  items="${datas}" var="dia">  
                <option value="${dia}">
                    ${dia}
                </option>  
            </c:forEach>  
        </select>
        </p>
        <p><label for="hora"><fmt:message key="campo.hora"/></label>
        <select id="hora" name="hora">
            <c:forEach  var="hora" begin="0" end="23">                              
                <c:choose>
                    <c:when test="${hora<10}">
                        <option value="0${hora}:00">0${hora}:00</option> 
                    </c:when>
                    <c:otherwise>
                        <option value="${hora}:00">${hora}:00</option> 
                    </c:otherwise>
                </c:choose>                             
            </c:forEach>                         
        </select>
        </p>
        <p><label for="contato"><fmt:message key="campo.contato"/></label>
        <select style="width:300px" id="contato" name="contato">
            <c:forEach  items="${contatos}" var="contato">  
                <option value="${contato.nome}">${contato.nome}</option>  
            </c:forEach>  
        </select>
        </p>
        <p><label for="local"><fmt:message key="campo.local"/></label>
        <input type="text" name="local" value="<fmt:message key="texto.digiteaqui"/>" style="width:210px"/>
        </p>                
        <p><label for="descricao"><fmt:message key="campo.descricao"/></label>
        <textarea  style="height:83px;width:395px" name="descricao" rows="4"><fmt:message key="texto.digiteaqui"/></textarea>
        </p>
    </fieldset>
    <p><input type="submit" value="<fmt:message key="botao.enviar"/>" class="btn btn-primary" style="margin-left:95px"/></p>        
</form>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
