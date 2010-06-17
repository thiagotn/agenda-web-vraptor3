<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/template/cabecalho.jsp"/>

<table id="resultado" class="display" border="0" cellpadding="0" cellspacing="0" width="100px" height="50px"> 
	<thead>
		<tr class="cabecalhoTabela">
			<th width="200px">Nome</th>
			<th width="200px">Email</th>
			<th width="300px">Endereço</th>
			<th width="100px">DataNascimento</th>
			<th width="120px">Ações</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${contatoList}" var="contato"	>
			<tr class="itemsTabela">
				<td>${contato.nome}</td> 
				<td>${contato.email}</td> 
				<td>${contato.endereco}</td> 
				<td><fmt:formatDate value="${contato.dataNascimento.time}" pattern="dd/MM/yyyy" /></td>
				<td align="center">
					<a href="<c:url value="/contato/edita?id=${contato.id}"/>">
						<img alt="Editar" src="<c:url value="/img/pencil.png"/>"/>
					</a>
					<a href="javascript:confirmaRemover('<c:url value="/contato/remove?id=${contato.id}"/>')">
						<img alt="Editar" src="<c:url value="/img/delete.png"/>"/>
					</a>
				</td>			
			</tr>	 
		</c:forEach>
	</tbody>
</table>

<jsp:include page="/template/rodape.jsp"/>	