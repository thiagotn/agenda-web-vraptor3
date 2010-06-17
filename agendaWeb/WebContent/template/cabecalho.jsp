<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<link href="<c:url value="/css/agendaWeb.css"/>" rel="stylesheet" type="text/css" />
<link href="<c:url value="/css/dataTable.css"/>" rel="stylesheet" type="text/css" />
<link href="<c:url value="/css/redmond/jquery-ui-1.8.2.custom.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="<c:url value="/js/agendaWeb.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/jquery-1.3.2.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/jquery.dataTables.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/jquery.validate.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/jquery.maskedinput-1.2.2.min.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/js/jquery.alphanumeric.min.js"/>"></script>
<script type="text/javascript"> 
	$(document).ready(function(){	
 
		$('#resultado').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers"
		});

		// Paginação do Index
		$('#resultado2').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers"
		});

		$("#data").mask("99/99/9999");
		$("#cpf").mask("999.999.999-99");

		$("#email").alphanumeric({allow:"@."});

		$("#login").validate({
			rules: {
				"j_username": { required: true },
				"j_password": { required: true }
			},
			messages: {
				"j_username": { required: "Informe o Login" },
				"j_password": { required: "Informe a Senha" }
			}	
		}); // Validando o novo formulário.
		
		$("#cadastro").validate({
			rules: {
				"nome": { required: true }
			},
			messages: {
				"nome": { required: "Informe o nome" }	
			}	
		}); // Validando o novo formulário.
 
		$("#form").validate({
			rules: {
				"contato.nome": { required: true },
				"contato.email": { required: true, email: true },
				"contato.endereco": {required: true},
				"contato.dataNascimento": { required: true, date: true},
				"contato.cpf": {required: true, cpf:true}
		     },
		     messages: {
		    	"contato.nome": { required: 'Informe o nome'},
		    	"contato.email": { 
			    		required: 'Informe o T&iacute;tulo',
			    		required: 'Informe um Email válido'
				    		},
		    	"contato.endereco": { required: 'Informe o Endereco'},
		    	"contato.dataNascimento": { 
			    							required: 'Informe a Data',
			    							required: 'Informe uma Data válida'
				    					  },
				"contato.cpf": {
				    			 required: 'Informe o CPF',
				    			 required: 'Informe um CPF válido'				
				    			}    					  
		     }
		});
		
	});	
</script> 
</head>
<body>
<center>
	<H1>Sistema AgendaWeb!</H1>
	<a href="<c:url value="/" />">Home</a>&nbsp;||&nbsp;<a href="<c:url value="/contato/lista" />">Visualizar</a>&nbsp;||&nbsp;<a href="<c:url value="/contato/form" />">Cadastrar</a>&nbsp;
	||&nbsp;

	Olá
	<% if(request.getRemoteUser() != null) {%>
		<%=request.getRemoteUser() %>
		<a href="<c:url value="/index/logout" />">Sair</a>
	<%} else {%>
		Visitante&nbsp;
		<a href="<c:url value="/contato/lista" />">Acessar</a>
	<%}%>
	
	<% if(request.getRemoteUser() == null) {%>
	||&nbsp;
	<a href="<c:url value="/contato/formEmail"/>">Esqueceu a senha?</a>
	<% } %>
	
	<p>
		<c:if test="${errors ne null or empty errors}">
		<span class="error">
			<c:forEach items="${errors}" var="error" >
					${error.category} - ${error.message}<br />
			</c:forEach>
		</span>
		</c:if>
	</p>
</center>