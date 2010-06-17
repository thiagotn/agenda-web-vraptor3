<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/template/cabecalho.jsp"/>

<form id="form" action="/agendaWeb/contato/altera"  method="post">
<input type="hidden" name="contato.id" value="${contato.id}"/>
	<table align="center">
		<tr>
			<td class="label" align="right">
				<span class="obrigatorio">*&nbsp;</span>Nome:
			</td>
			<td class="field">
				<input type="text" name="contato.nome" value="${contato.nome}" class="required" />
			</td>
		</tr>
		<tr>
			<td class="label" align="right">
				<span class="obrigatorio">*&nbsp;</span>Email:
			</td>
			<td class="field">
				<input type="text" name="contato.email" value="${contato.email}" class="required email" />
			</td>
		</tr>
		<tr>
			<td class="label" align="right">
				<span class="obrigatorio">*&nbsp;</span>Endereço
			</td>
			<td class="field">
				<input type="text" name="contato.endereco" value="${contato.endereco}" class="required" />
			</td>
		</tr>
		<tr>
			<td class="label" align="right">
				<span class="obrigatorio">*&nbsp;</span>Data Nascimento
			</td>
			<td class="field">
				<input type="text" name="contato.dataNascimento" value="<fmt:formatDate value="${contato.dataNascimento.time}" pattern="dd/MM/yyyy" />" class="required date" />
			</td>
		</tr>
		<tr>
			<td class="label" align="right">
				<span class="obrigatorio">*&nbsp;</span>CPF
			</td>
			<td class="field">
				<input type="text" name="contato.cpf" value="${contato.cpf}" class="required cpf" />
			</td>
		</tr>		
		<tr>
			<td align="center"><input type="submit" value="Enviar"/></td>
			<td align="center"><input type="button" value="Cancelar" onclick="javascript:document.location='/agendaWeb/contato/lista'" /></td>
		</tr>	
	</table>
</form>

<jsp:include page="/template/rodape.jsp"/>