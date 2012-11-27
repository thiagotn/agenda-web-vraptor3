<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>

<!-- 

P�gina Lista de compromissos

Est� p�gina possui funcionalidades �teis ao usu�rio, nela ser� exibida uma lista de com todos os
compromisso cadastrados em uma tabela din�mica como os principais campos. Ao clicar nos nomes de seus
t�tulos os dados s�o organizados. Um bot�o de excluir e excluir poder�o ser utilizados para excluir ou
alterar os compromissos, al�m de poder export�-los para outros tipos de dados tais com PDF, CSV, XLS,
XML. Ainda nesta p�gina o usu�rio pode filtrar os dados dados da tabela, caso o valor a ser filtrado n�o
n�o exista no banco de dados, o site listar� todos os dados.

-->

<%@ include  file="../WEB-INF/jspf/cabecalho.jspf" %>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

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

<div class="tabela"><fmt:message key="titulo.listacompromissos"/></div>

<form class="form" action="<c:url value="/compromisso/localizaCompromissos.view"/>"  method="post">
    <div>
        <legend style="display: inline">
            <fmt:message key="label.pesquisarpor"/>
            <select name="por" style="width:100px">
                <option value="data"><fmt:message key="data"/> </option>
                <option value="hora"><fmt:message key="hora"/> </option>
                <option value="local"><fmt:message key="local"/> </option>
            </select>
            <input name="oque" type="text" style="width:240px">
            <div style="float: right; position: relative; right:45px ;top: 2px; width: 45px;">
                <input type="submit" value="<fmt:message key="botao.localizar"/>" style="position: absolute; top:inherit" class="btn btn-primary" />
            </div>
        </legend>
    </div>
</form>


<display:table requestURI="/compromisso/listaCompromissos.view" name="compromisso" export="true" sort="list" pagesize="10" id="compromisso" excludedParams="*" class="simple">

    <display:setProperty name="paging.banner.items_name"><fmt:message key="paging.banner.items_name"/></display:setProperty> 
    <display:setProperty name="paging.banner.item_name"><fmt:message key="paging.banner.item_name"/></display:setProperty> 
    <display:setProperty name="paging.banner.no_items_found"><fmt:message key="paging.banner.no_items_found"/></display:setProperty> 
    <display:setProperty name="paging.banner.one_item_found"><fmt:message key="paging.banner.one_item_found"/></display:setProperty>         
    <display:setProperty name="paging.banner.full"><fmt:message key="paging.banner.full"/></display:setProperty>
    <display:setProperty name="paging.banner.first"><fmt:message key="paging.banner.first"/></display:setProperty>
    <display:setProperty name="paging.banner.last"><fmt:message key="paging.banner.last"/></display:setProperty>
    <display:setProperty name="paging.banner.onepage"><fmt:message key="paging.banner.onepage"/></display:setProperty>
    <display:setProperty name="paging.banner.page.selected"><fmt:message key="paging.banner.page.selected"/></display:setProperty>
    <display:setProperty name="paging.banner.page.link"><fmt:message key="paging.banner.page.link"/></display:setProperty>
    <display:setProperty name="paging.banner.page.separator"><fmt:message key="paging.banner.page.separator"/></display:setProperty>
    <display:setProperty name="save.excel.banner"><fmt:message key="save.excel.banner"/></display:setProperty>
    <display:setProperty name="export.banner"><fmt:message key="export.banner"/></display:setProperty>
    <display:setProperty name="export.banner"><fmt:message key="export.banner"/></display:setProperty>
    <display:setProperty name="basic.msg.empty_list"><fmt:message key="basic.msg.empty_list"/></display:setProperty>
    <display:setProperty name="basic.msg.empty_list_row"><fmt:message key="basic.msg.empty_list_row"/></display:setProperty>
    <display:setProperty name="basic.msg.empty_list"><fmt:message key="error.msg.invalid_page"/></display:setProperty>    
    <display:setProperty name="paging.banner.some_items_found"><fmt:message key="paging.banner.some_items_found"/></display:setProperty>      

    <display:column property="data" titleKey="coluna.data"  sortable="true" headerClass="sortable" />
    <display:column property="hora" titleKey="coluna.hora"  sortable="true"  headerClass="sortable" />
    <display:column property="local" titleKey="coluna.local" sortable="true" maxWords="2" headerClass="sortable" />
    <display:column property="descricao" titleKey="coluna.descricao" sortable="true" maxWords="2" headerClass="sortable" />
    <display:column property="contato" titleKey="coluna.contato" sortable="true" maxWords="2" headerClass="sortable" />

    <display:column media="html" titleKey="coluna.editar">
        <a href="<c:url value="/compromisso/editaCompromissos.view?id=${compromisso.id}"/>">
            <img src="<c:url value="/template/img/add.png"/>" alt="Editar" width="20px" height="20px"/>
        </a>
    </display:column>
    <display:column media="html" titleKey="coluna.deletar">
        <a href="<c:url value="/compromisso/removeCompromissos.view?id=${compromisso.id}"/>">
            <img src="<c:url value="/template/img/delete.png" />" alt="Remover" width="20px" height="20px"/>
        </a>
    </display:column>
    <display:setProperty name="export.csv" value="true" />
    <display:setProperty name="export.excel" value="true" />
    <display:setProperty name="export.xml" value="true" />
    <display:setProperty name="export.pdf" value="true" />
    <display:setProperty name="export.rtf" value="true" />
    <display:setProperty name="export.csv.filename" value="compromissos.csv"  />
    <display:setProperty name="export.excel.filename" value="compromissos.xls"  />
    <display:setProperty name="export.xml.filename" value="compromissos.xml"  />
    <display:setProperty name="export.pdf.filename" value="compromissos.pdf"  />
    <display:setProperty name="export.rtf.filename" value="compromissos.rtf"  />
</display:table>
<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
