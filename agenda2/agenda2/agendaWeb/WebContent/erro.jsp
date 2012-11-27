<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/template/cabecalho.jsp"/>

<table class="login" align="center" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<img alt="Alerta" src="<c:url value="/img/alert.png"/>"/>
			Usuário ou senha inválidos!
			<a href="<c:url value="/login.jsp"/>">Acessar</a> 		
		</td>
	</tr>
</table>

<jsp:include page="/template/rodape.jsp"/>