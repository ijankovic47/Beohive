<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	

	<sec:authorize access="hasRole('OPERATER')">
		<c:set var="uri"><sec:authentication property="principal.partner.id"/></c:set>
	</sec:authorize>
	<div class="main-manu-wrap nav">
		<ul>
			<li><a href="${contextPath}/">Početna</a></li>
			<li><a href="${contextPath}/operater/${uri}">Operateri</a></li>
	       <sec:authorize access="hasRole('BEOTEL')">
				<li><a href="${contextPath}/partner">Partneri</a></li>
			</sec:authorize>
			<li><a href="${contextPath}/korisnici">Korisnici</a>
				<ul class="sub-nav">
					<li><a href="${contextPath}/korisnici">Novi Korisnik</a></li>
					<li><a href="${contextPath}/administracija">Administracija</a></li>
				</ul>
			</li>
			<sec:authorize access="hasRole('BEOTEL')">
				<li><a href="${contextPath}/modeli">Modeli</a></li>
			</sec:authorize>
			<li><a href="${contextPath}/uredjaj">Uređaji</a></li>			
		</ul>
	</div>	
	
	<div class="distributor-details">
		<sec:authorize access="isAuthenticated()">
		distributer: <span><strong><sec:authentication property="principal.partner.name"/></strong></span>
		</sec:authorize>
	</div>
	
	<div class="user-details-wrap usr">		
	<sec:authorize access="isAuthenticated()">
	 <ul>
	 	<li>operater: <strong><sec:authentication property="principal.ime"/> <sec:authentication property="principal.prezime"/></strong></li>
	 	<li>
	 		<form action="${contextPath}/logout" method="post">
			<button type="submit" id="btn-logout">Log out</button>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		</form>
	 	</li>	 	
	 </ul>
	</sec:authorize>		
	</div>
