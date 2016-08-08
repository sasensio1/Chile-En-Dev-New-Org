<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="resources/css/styles.css" rel="stylesheet">
		<title>Emergencias App Reset Password Page</title>
	</head>

	<body>
		<header>
			<section id="logo-section">
				<img src="resources/images/enel-logo-arbol.jpg" width="150" /><br/>
				<img src="resources/images/Enel-logo-new.jpg" width="200" />
				<p id="login-text"><s:message code="reset_label_title"/></p>
			</section>
		</header>
		<div id="box-login">
		  	<form name="resetForm" id="login-user" action="sendEmail" method="POST">
			    <table id="login-user-table">
			    	<tr>
        				<td colspan="2" align="center">
						 	<h4 style="font-weight:bold;color:red;"><s:message code="${userView.mensajeError}" text=""/></h4>
						 	<h4 style="font-weight:bold;color:green;"><s:message code="${userView.mensaje}" text=""/> <s:message code="${userView.email}" text=""/></h4>		
        				</td>
       				</tr>
					<tr>
						<td align="right"><label for="userName"><s:message code="reset_form_label_user"/>:</label></td>
						<td><input type="text" name="userName" value="" class=""></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input name="submit" type="submit" class="" value="<s:message code="login_form_button_submit"/>"/></td>
					</tr>
				</table>
			 </form>
		 </div>
	</body>
</html>