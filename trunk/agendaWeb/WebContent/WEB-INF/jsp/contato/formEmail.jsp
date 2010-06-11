<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/template/cabecalho.jsp"/>

<form id="form" action="/agendaWeb/contato/reenvioSenha"  method="post">
	<table align="center">
		<tr>
			<td class="label" align="right">
				<span class="obrigatorio">*&nbsp;</span>Email:
			</td>
			<td class="field">
				<input type="text" name="email" id="email" class="required email" />
			</td>
		</tr>
		<tr>
			<td align="right"><input type="submit" value="Enviar"/></td>
			<td align="center"><input type="button" value="Cancelar" 
				onclick="javascript:document.location='/agendaWeb/contato/lista'" /></td>
		</tr>	
	</table>
</form>

<jsp:include page="/template/rodape.jsp"/>