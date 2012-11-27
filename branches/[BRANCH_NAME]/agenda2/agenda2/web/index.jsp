<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>

<%@ include  file="../WEB-INF/jspf/cabecalho.jspf"%>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="<c:url value='/template/css/bootstrap.css'/>" rel="stylesheet"/>
<link href="<c:url value='/template/css/bootstrap-responsive.css'/>" rel="stylesheet"/>


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

<div style="margin-top: 50px;margin-left: -200px;float: left;width:350px">
    <img src="<c:url value='template/img/cal.png'/>" name="figura"></img>
</div>

<div class="index">
    <div class="modal-header"  style="width: 700px; float: right">
        <p align=""><fmt:message key="site.saudacao1"/></p>
        <br/>
        <p><fmt:message key="site.saudacao2"/><br/><br/><fmt:message key="site.saudacao3"/></p>
    </div>    
    <br/>

    <display:table name="contatos" export="true" requestURI="" sort="list" pagesize="3" id="contato" class="simple">
        <display:setProperty name="paging.banner.items_name"><fmt:message key="paging.banner.items_name"/></display:setProperty> 
        <display:setProperty name="paging.banner.item_name"><fmt:message key="paging.banner.item_name"/></display:setProperty> 
        <display:setProperty name="paging.banner.no_items_found"><fmt:message key="paging.banner.no_items_found"/></display:setProperty> 
        <display:setProperty name="paging.banner.one_item_found"><fmt:message key="paging.banner.one_item_found"/></display:setProperty>         
        <display:setProperty name="paging.banner.items_found"><fmt:message key="paging.banner.some_items_found"/></display:setProperty>    
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

        <display:column property="id" title="Id" sortable="true" headerClass="sortable" />
        <display:column property="nome" title="Nome" sortable="true" headerClass="sortable" />
        <display:column property="email" title="Email" sortable="true" headerClass="sortable" />

        <display:setProperty name="export.csv" value="true"/>
        <display:setProperty name="export.excel" value="true"/>
        <display:setProperty name="export.xml" value="true"/>
        <display:setProperty name="export.pdf" value="true"/>
        <display:setProperty name="export.rtf" value="true"/>
        <display:setProperty name="export.csv.filename" value="contatos.csv"  />
        <display:setProperty name="export.excel.filename" value="contatos.xls"  />
        <display:setProperty name="export.xml.filename" value="contatos.xml"  />
        <display:setProperty name="export.pdf.filename" value="contatos.pdf"  />
        <display:setProperty name="export.rtf.filename" value="contatos.rtf"  />
    </display:table>
</div>
<%@ include file="../WEB-INF/jspf/rodape.jspf" %>