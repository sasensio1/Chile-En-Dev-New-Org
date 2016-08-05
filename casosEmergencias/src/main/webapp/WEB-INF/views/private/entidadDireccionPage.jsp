<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %> 


<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Emergencias App</title>		
	<link href="../resources/css/styles.css" rel="stylesheet">
	<link href="../resources/css/cabecera.css" rel="stylesheet" />
	<link href="../resources/css/body.css" rel="stylesheet" />	

	<script src="../resources/js/jquery-1.12.3.js" lang=""></script>
	<script src="../resources/js/entidadPages.js" lang=""></script>
	</head>
	<body>
		<script type="text/javascript">var objetoSeleccionado='<s:message code="cabeceraPage_list_direccion"/>';</script>
		<jsp:include page="cabeceraPage.jsp"/>
		<form:form name="formEntidadDireccion" action="actualizarDireccion" modelAttribute="direccion" method="POST">
			<form:hidden path="sfid"/>
	
			
			<div class="divCabeceraEntidad">
				<div class="divTituloEntidad">
						<input id="arrowDetalleDireccion" type="image" src="../resources/images/Arrowdown.PNG"  height="15" onclick="showHideCabeceras('detalleDireccion','arrowDetalleDireccion'); return false;"/>
						<b><label><s:message code="entidadDireccion_title_label_detalle_direccion"/></label></b>
				</div>
			</div>			
			<div id="detalleDireccion" class="divEntidad">
			<div>
			<br/>
			</div>
				<div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_region"/></label>
					</div>
					<div>
						<label>${direccion.region}</label>
					</div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_comuna"/></label>
					</div>
					<div>
						<label>${direccion.comuna}</label>
					</div>
				</div>
				<div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_tipoDeCalle"/></label>
					</div>
					<div>
						<label>${direccion.tipoCalle}</label>
					</div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_calle"/></label>
					</div>
					<div>
						<label>${direccion.calle}</label>
					</div>
				</div>
				<div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_numero"/></label>
					</div>
					<div>
						<label>${direccion.numero}</label>
					</div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_departamento"/></label>
					</div>
					<div>
						<label>${direccion.departamento}</label>
					</div>
				</div>
				<div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_codigoDeDireccion"/></label>
					</div>
					<div>
						<label>${direccion.name}</label>
					</div>
					<div class="divLabel">
						<label><s:message code="entidadDireccion_table_label_direccionConcatenada"/></label>
					</div>
					<div>
						<label>${direccion.direccionConcatenada}</label>
					</div>
				</div>
			</div>	
		</form:form>
		 					    
		<script type="text/javascript">
			function showHideCabeceras(idDiv,idArrow){
 				var div =document.getElementById(idDiv);
 				var arrow = document.getElementById(idArrow); 				
 					if(div.style.visibility=='')  {
 						div.style.visibility='hidden';
						arrow.src="../resources/images/Arrowright.PNG";
 					}else{
						div.style.visibility='';
 						arrow.src="../resources/images/Arrowdown.PNG";
 					}					
			}
		</script> 		
	</body>
</html>