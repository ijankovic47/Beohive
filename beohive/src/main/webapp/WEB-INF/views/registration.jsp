<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registracija</title>
</head>
<body>	
	<h2>Registracija: </h2>
	<form:form action="register" method="POST" commandName="newPartner" >
		<div>
			Name <form:input path="name" /> 
		</div>
		<div>
			Prefix: <form:password path="prefix" />
		</div>
		<div>
			Maksimalan broj operatera: <form:input path="maxNoOp" /> 
		</div>
		<div>
			Status: <form:input path="status" />
		</div>
		
		<input type="submit" value="Registracija"/>

	</form:form>
</body>
</html>