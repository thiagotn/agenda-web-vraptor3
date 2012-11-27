<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>

<!-- 

Página Lista de Contatos

Está página possui funcionalidades úteis ao usuário, nela será exibida uma lista de com todos os
contatos cadastrados em uma tabela dinâmica como os principais campos. Ao clicar nos nomes de seus
títulos os dados são organizados. Um botão de excluir e excluir poderão ser utilizados para excluir ou
alterar os contatos, além de poder exportá-los para outros tipos de dados tais com PDF, CSV, XLS,
XML. Ainda nesta página o usuário pode filtrar os dados dados da tabela, caso o valor a ser filtrado não
não exista no banco de dados, o site listará todos os dados.

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

<div class="tabela"><fmt:message key="titulo.listacontatos"/></div>

<form class="form" action="<c:url value="/contato/localizaContatos.view"/>"  method="post">
    <div>
        <legend style="display: inline">
            <fmt:message key="label.pesquisarpor"/>
            <select name="por" style="width:100px">
                <option value="nome"><fmt:message key="nome"/></option>
                <option value="data_empresa"><fmt:message key="aniversario"/></option>
                <option value="data_empresa"><fmt:message key="empresa"/></option>
            </select>
            <input name="oque" type="text" style="width:240px">
            <div style="float: right; position: relative; right:45px ;top: 2px; width: 45px;">
                <input type="submit" value="<fmt:message key="botao.localizar"/>" style="position: absolute; top:inherit" class="btn btn-primary" />
            </div>
        </legend>
    </div>
</form>

<display:table requestURI="/contato/listaContatos.view" name="contatos" export="true" sort="list" pagesize="10" id="contato" excludedParams="*" class="simple">    

    <display:setProperty name="basic.msg.empty_list"><fmt:message key="basic.msg.empty_list"/></display:setProperty>
    <display:setProperty name="basic.msg.empty_list_row"><fmt:message key="basic.msg.empty_list_row"/></display:setProperty>
    <display:setProperty name="error.msg.invalid_page"><fmt:message key="error.msg.invalid_page"/></display:setProperty>
    <display:setProperty name="export.banner"><fmt:message key="export.banner"/></display:setProperty>
    <display:setProperty name="export.banner.sepchar"><fmt:message key="export.banner.sepchar"/></display:setProperty>
    <display:setProperty name="paging.banner.item_name"><fmt:message key="paging.banner.item_name"/></display:setProperty>
    <display:setProperty name="paging.banner.items_name"><fmt:message key="paging.banner.items_name"/></display:setProperty>
    <display:setProperty name="paging.banner.no_items_found"><fmt:message key="paging.banner.no_items_found"/></display:setProperty>
    <display:setProperty name="paging.banner.one_item_found"><fmt:message key="paging.banner.one_item_found"/></display:setProperty>
    <display:setProperty name="paging.banner.all_items_found"><fmt:message key="paging.banner.all_items_found"/></display:setProperty>
    <display:setProperty name="paging.banner.some_items_found"><fmt:message key="paging.banner.some_items_found"/></display:setProperty>
    <display:setProperty name="paging.banner.full"><fmt:message key="paging.banner.full"/></display:setProperty>
    <display:setProperty name="paging.banner.first"><fmt:message key="paging.banner.first"/></display:setProperty>
    <display:setProperty name="paging.banner.last"><fmt:message key="paging.banner.last"/></display:setProperty>
    <display:setProperty name="paging.banner.onepage"><fmt:message key="paging.banner.onepage"/></display:setProperty>
    <display:setProperty name="paging.banner.page.selected"><fmt:message key="paging.banner.page.selected"/></display:setProperty>
    <display:setProperty name="paging.banner.page.link"><fmt:message key="paging.banner.page.link"/></display:setProperty>
    <display:setProperty name="paging.banner.page.separator"><fmt:message key="paging.banner.page.separator"/></display:setProperty>
    <display:setProperty name="pagination.sort.param"><fmt:message key="pagination.sort.param"/></display:setProperty>
    <display:setProperty name="pagination.sortdirection.param"><fmt:message key="pagination.sortdirection.param"/></display:setProperty>
    <display:setProperty name="pagination.pagenumber.param"><fmt:message key="pagination.pagenumber.param"/></display:setProperty>
    <display:setProperty name="pagination.searchid.param"><fmt:message key="pagination.searchid.param"/></display:setProperty>
    <display:setProperty name="pagination.sort.asc.value"><fmt:message key="pagination.sort.asc.value"/></display:setProperty>
    <display:setProperty name="pagination.sort.desc.value"><fmt:message key="pagination.sort.desc.value"/></display:setProperty>
    <display:setProperty name="pagination.sort.skippagenumber"><fmt:message key="pagination.sort.skippagenumber"/></display:setProperty>
    <display:setProperty name="save.excel.banner"><fmt:message key="save.excel.banner"/></display:setProperty>
    <display:setProperty name="save.excel.filename"><fmt:message key="save.excel.filename"/></display:setProperty>  

    <display:column property="nome" titleKey="coluna.nome" sortable="true" maxWords="2" headerClass="sortable" />
    <display:column property="email" titleKey="coluna.email" sortable="true"  headerClass="sortable" />
    <display:column property="endereco" titleKey="coluna.endereco" sortable="true" maxWords="2" headerClass="sortable" />
    <display:column property="telefone" titleKey="coluna.telefone" sortable="true" headerClass="sortable" />

    <display:column media="html" titleKey="coluna.editar" >
        <a href="<c:url value="/contato/editaContatos.view?id=${contato.id}"/>">
            <img src="<c:url value="/template/img/add.png"/>" alt="Editar" width="20px" height="20px"/>
        </a>
    </display:column>
    <display:column media="html" titleKey="coluna.deletar" >
        <a href="<c:url value="/contato/removeContatos.view?id=${contato.id}"/>">
            <img src="<c:url value="/template/img/delete.png" />" alt="Remover" width="20px" height="20px"/>
        </a>

    </display:column>
    <display:setProperty name="export.csv" value="true" />
    <display:setProperty name="export.excel" value="true" />
    <display:setProperty name="export.xml" value="true" />
    <display:setProperty name="export.pdf" value="true" />
    <display:setProperty name="export.rtf" value="true" />
    <display:setProperty name="export.csv.filename" value="contatos.csv"  />
    <display:setProperty name="export.excel.filename" value="contatos.xls"  />
    <display:setProperty name="export.xml.filename" value="contatos.xml"  />
    <display:setProperty name="export.pdf.filename" value="contatos.pdf"  />
    <display:setProperty name="export.rtf.filename" value="contatos.rtf"  />
</display:table>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>

