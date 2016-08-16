<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div id="cabeceraEnel">
	<div id="divImagenEnel">
		<img src="../resources/images/logo.png" id="logoEnel" />
	</div>
	<div id="divNoImagenEnel">
		<a href="../logout"><s:message code="cabeceraPage_href_exit"/></a>
	</div>
</div>
<div id="menuCabecera">
	<div id="posicionamiento">
		<div style="float:left;">
		</div>
		<div style="float:left;">
			<label id="lblMenu"></label>
		</div>
	</div>
	<div id="desplegable">
		<div class="menuListado" onclick="javascript:cambiarLabel('<s:message code="cabeceraPage_label_case"/>');window.location.href='homeCasos'">
			<a href="homeCasos" ><s:message code="cabeceraPage_list_case"/></a>
		</div>
		<div class="menuListado" onclick="javascript:cambiarLabel('<s:message code="cabeceraPage_list_contact"/>');window.location.href='homeContacts'">
			<a href="homeContacts"><s:message code="cabeceraPage_list_contact"/></a>
		</div>
		<div class="menuListado" onclick="javascript:cambiarLabel('<s:message code="cabeceraPage_list_suministro"/>');window.location.href='homeSuministrosInicio'">
			<a href="homeSuministros"><s:message code="cabeceraPage_list_suministro"/></a>
		</div>
		<div class="menuListado" onclick="javascript:cambiarLabel('<s:message code="cabeceraPage_list_direccion"/>');window.location.href='homeDirecciones'">
			<a href="homeDirecciones"><s:message code="cabeceraPage_list_direccion"/></a>
		</div>
		<div class="menuListado" onclick="javascript:cambiarLabel('<s:message code="cabeceraPage_list_cuentas"/>');window.location.href='homeCuentas'">
			<a href="homeCuentas" ><s:message code="cabeceraPage_list_cuentas"/></a>
		</div>
	</div>
	<div id="divBotonDesplegable">			
		<input type="button" id="botonDesplegable" name="botonDesplegable" style="align:right;">
	</div>
	<div id="divTabs">
		&nbsp;
	</div>
</div>