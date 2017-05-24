<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!-- <html> -->
<!-- <head> -->
<!-- 	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
<%-- 	<meta name="_csrf" content="${_csrf.token}"/> --%>
<%-- 	<meta name="_csrf_header" content="${_csrf.headerName}"/> --%>
<%-- 	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	 --%>
<%-- 	<link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/table-layout.css" /> --%>
<!-- <title>Login</title> -->
<!-- </head> -->
<!-- <body> -->
	<div class="main-login-container">	
	 <form id="opr-login-form" action="login" method="post">            
               <c:if test="${not empty error}">
				    <div class="opr-login-err">
				    	<c:out value="${error}"/>
				    </div>
				</c:if>
		<table>
			<tr>
<!-- 				<td><label for="username">Korisničko ime</label></td> -->
				<td><input type="text" id="username" name="username" placeholder="Korisničko ime ili E-Mail"/></td>
			</tr>
			<tr>
<!-- 				<td><label for="password">lozinka</label></td> -->
				<td><input type="password" id="password" name="password" placeholder="Lozinka"></td>
			</tr>
			<tr>
				<td colspan="2"><button type="submit" class="btn">Prijavi se</button></td>
			</tr>			
        </table>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>	
	</div>	