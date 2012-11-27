<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<%@ page session = "true" language="java" contentType="text/html" pageEncoding="ISO-8859-1" %>
<!-- 
Página Edição dos Compromissos

Está página o usuário poderá editar um compromisso ao localiza-lo na lista de compromissos,
ao término da alteração o site será redirecionará para página de lista de compromissos, caso
ocorra algum erro será exibido a informação de erro no topo da página lista de compromissos.

-->

<%@ include  file="../WEB-INF/jspf/cabecalho.jspf" %>

<script type="text/javascript">
    function preencheHora() { 
        for (i=0; i<24; i++)
        {   
            if (i<10){
                document.form.hora.options[i] = new Option("0"+i+":00","0"+i+":00");
            }else{
                document.form.hora.options[i] = new Option(i+":00",i+":00");
            }
        }
    }
    
    function preencheData() { 
        for (i=0; i<30; i++) {               
            var mydate = new Date();
            mydate.setDate(mydate.getDate()+i)
            var year=mydate.getYear()
            if (year < 1000) year+=1900
            var day=mydate.getDay()
            var month=mydate.getMonth()+1;
            var daym=mydate.getDate()
            if (daym<10) daym="0"+daym
            data=(daym+"/"+month+"/"+year);
            document.form.data.options[i] = new Option(data,data);
        }
    }
</script> 


<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="<c:url value="/template/css/main.css" />" />        
<link href="<c:url value="/template/css/bootstrap-responsive.css"/>" rel="stylesheet">        
<link href="<c:url value="/template/css/bootstrap.css"/>" rel="stylesheet">
<script src="<c:url value="/template/js/jquery_min.js"/>"></script>
<script type="text/javascript"></script>

<c:choose>
    <c:when test="${sessionScope.lingua eq 'en_US'}">
        <fmt:setLocale value="en_US" />
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="pt_BR" /> 
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="org.ifsp.agenda.lingua.messages"/>

<div class="tabela"><fmt:message key="titulo.editarcompromisso"/></div>
<form class="form" style="height: 310px" name="form" action="<c:url value="/compromisso/alteraCompromissos.view"/>"  method="post">    
    <input type="hidden" name="id" size="30" value="${id}"/>   
    <fieldset>
        <p><label for="data"><fmt:message key="campo.data"/> </label>
            <select id="data" name="data" onfocus="preencheData()">  
                <option value="${data}">${data}</option>  
            </select>
        </p>
        <p><label for="hora"><fmt:message key="campo.hora"/> </label>
            <select id="hora" name="hora" onfocus="preencheHora()">
                <option value="${hora}">${hora}</option> 
            </select>
        </p>
        <p><label for="contato"><fmt:message key="campo.contato"/> </label>
            <select id="contato" name="contato" style="width:300px">
                <c:forEach  items="${contatos}" var="contato">  
                    <option value="${contato.nome}">${contato.nome}</option>  
                </c:forEach>  
                <option value="${contato}">${contato}</option>   
            </select>
        </p>
        <p><label for="local"><fmt:message key="campo.local"/> </label>
            <input type="text" name="local" value="${local}" style="width:210px"/>
        </p>                
        <p><label for="descricao"><fmt:message key="campo.descricao"/> </label>
            <textarea  style="height:83px;width:395px" name="descricao" rows="4">${descricao}</textarea>
        </p>
    </fieldset> 
    <p><input type="submit" value="<fmt:message key="botao.enviar"/> " class="btn btn-primary" style="margin-left:95px"/></p>
    
</form>

<%@ include file="../WEB-INF/jspf/rodape.jspf" %>
