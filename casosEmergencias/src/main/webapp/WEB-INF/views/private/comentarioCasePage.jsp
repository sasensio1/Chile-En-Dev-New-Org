<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %> 

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Emergencias App</title>		
		
		<link rel="icon" type="image/png" href="../resources/images/favicon.png">
			
		<link href="../resources/css/cabecera.css" rel="stylesheet" />
		<link href="../resources/css/body.css" rel="stylesheet" />	
		<link href="../resources/css/styles.css" rel="stylesheet" />	
	
		<script src="../resources/js/jquery-1.12.3.js" lang=""></script>
		<script src="../resources/js/header.js" lang=""></script>
		<script src="../resources/js/utils.js" lang=""></script>
	</head>
	
	<body onload="initHeader(); checkUpdates();">
		<script type="text/javascript">var objetoSeleccionado='<s:message code="entidadCaso_title_label_comentario_caso"/>';</script>
		<jsp:include page="cabeceraPage.jsp"/>
		
		<form:form name="formComentarioCaso" action="saveComentarioCaso" modelAttribute="caseComment" method="POST">
			<form:hidden path="caseid"/>
			<h2><s:message code="comentarioCase_label_caso" arguments="${numeroCaso}"/></h2>
			<div class="botoneraListado">
				<div>
				<h4><s:message code="comentarioCase_label_titulo"/></h4>
				<ul>
					<li><input id="Guardar" type="submit" name="Guardar" value='<s:message code="comentarioCase_label_button_guardar"/>'/></li>
					<li><input id="Cancelar" type="button" name="Cancelar" value='<s:message code="comentarioCase_label_button_cancelar"/>' onclick="cancelCreatedCase('${sfid}')"/></li>
				</ul>
				</div>
			</div>
			<div class="divEntidad">
				<div class="subtittlePrincipalAltaEntidad">
					<div class="subtittlePrincipalAltaEntidadDivInfo">
						<label class="divLabel"><s:message code="comentarioCase_label_detalle_caso_title"/></label>
					</div>
					<div class="subtittlePrincipalAltaEntidadDivOblig">
						<div class="tittleObligatorio"></div>
							<label><s:message code="entidadCasoAlta_table_title_label_infoobligatorio"/></label>
					</div>
				</div>
			</div>
			<div id="detalleCaso" class="divEntidad" >
				<div  >
					<table style="margin-top:3%;margin-left:2%;" >				
						<tr>
							<td class="divLabel">
								<label><s:message code="comentarioCase_label_detalle_caso_asunto"/></label>
							</td>
							<td>
								<label>${asunto}</label>
							</td>					
						</tr>
						<tr>
							<td class="divLabel">
								<label><s:message code="comentarioCase_label_detalle_caso_descripcion"/></label>
							</td>
							<td>
								<label>${description}</label>
							</td>
						</tr>	
					</table>
				</div>		
			</div>	
			<div class="divEntidad">
				<div class="subtitleAltaEntidad">
					<div>
						<label class="divLabel"><s:message code="comentarioCase_label_detalle_comentario_title"/></label>
					</div>
				</div>
			</div>
			<div id="datosEmergencia" class="divEntidad" >
				<div >	
					<table style="margin-top:3%;">
						<tr>
							<td class="divLabel">
								<label><s:message code="comentarioCase_label_detalle_comentario_comentario"/></label>
							</td>				
							<td>
								<form:textarea path="comment" class="requiredTextArea"   rows="9" cols="65"/>
							</td>
						</tr>
					</table>
				</div>				
			</div>
			
			<div id="divEntidadCasosComments" class="divEntidad">
					<div class="subtitleAltaEntidad">
						<div>
							<label class="divLabel"><s:message code="comentarioCase_label_comentarios_caso_title" /></label>
						</div>
					</div>
					<div id="tablaCasosComments">
						<table class="basicTable">
							<tr>
							    <th><s:message code="comentarioCase_column_comentarios_caso_publica" /></th>
							    <th><s:message code="comentarioCase_column_comentarios_caso_comentario" /></th>
							</tr>
							<c:choose>
								<c:when test="${not empty caseComment.listaOldComment}">
									<c:forEach items="${caseComment.listaOldComment}" var="coment">
										<tr>
											<td>
												<c:if test="${coment.ispublished}">
												    <label><input type="checkbox" id="checkbox" value="true" checked="checked" disabled/>
													</label> 					
												</c:if> 
												<c:if test="${coment.ispublished == false}">
													<label><input type="checkbox" id="checkbox" value="true" disabled/></label>	
												</c:if>
											</td>
											<td>
												<b><s:message code="entidadCaso_texto_label_comentarios_comentario_creado" arguments="${coment.createdbyid}, ${coment.createddate}"/>
												<c:if test="${coment.lastmodifiedbyid != null}">
												 | <s:message code="entidadCaso_texto_label_comentarios_comentario_modificado" arguments="${coment.lastmodifiedbyid}, ${coment.lastmodifieddate}"/>
												</c:if>
											</b> 
											<br>${coment.comment}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="6" class="tablaVacia">
											<s:message code="entidades_empty_case_table" />
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</table>
					</div>
				</div>
		</form:form>
  	</body>
</html>