<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/template/cabecalho.jsp"/>

<form id="login" method="POST" action="<c:url value="/j_security_check" />">
	<table class="login" align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="label">
				Usuario: 		
			</td>
			<td class="field">
				<input type="text" name="j_username" size="15" class="required">
			</td>			
		</tr>
		<tr>
			<td class="label">
				Senha: 		
			</td>
			<td class="field">
				<input type="password" name="j_password" maxlength="20" size="15" 
				class="required">
			</td>			
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type='submit' value='Acessar' />			
			</td>
		</tr>		
	</table>
</form>

<jsp:include page="/template/rodape.jsp"/>