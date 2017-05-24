<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	
	
<title>Partners</title>

<style>
.opr-tables {
	border: 1px solid #343434;
	border-collapse: collapse;
}

.opr-tables td {
	padding: 6px 12px;
	border: 1px solid #343434;
}

#par-edit-tr {
	display: none;
}
#par-add-tr{
background-color: silver;
}
</style>

</head>

<c:choose>
<c:when test="${errors>0 || poruka==false}"><body></c:when>         
<c:otherwise><body onload="btnClick('#par-add-btn')"></c:otherwise> 
</c:choose>
<div class="main-container">

<div id="wrapper" style="margin:auto;margin-left:100px;overflow: hidden;">
<div id="tabela" style="float:left;">
<input type="hidden" id="ctx-path" value="${contextPath}"/>
	<h1>Partneri</h1>
	
	<c:if test="${errors>0}"><p style="font-size: small; color: red; font-style: italic;">Registracija nije uspela!</p></c:if>
	<c:if test="${register}"><p style="font-size: small; color: green; font-style: italic;">Registracija uspesno izvrsena!</p></c:if>
	<c:if test="${poruka==false && prefixName==1}"><p style="font-size: small; color: red; font-style: italic;">Registracija nije uspela! Prefiks partnera mora biti jedinstven !</p></c:if>
	<c:if test="${poruka==false && prefixName==2}"><p style="font-size: small; color: red; font-style: italic;">Registracija nije uspela! Ime partnera mora biti jedinstveno !</p></c:if>
	<table class="opr-tables">
		<tr>
			<th>Id</th>
			<th>Prefiks</th>
			<th>Ime</th>
			<th>Maks. aktivnih op.</th>
			<th>Status</th>
			<th><button type="button" id="par-add-btn">+</button></th>
			<th></th>
			<th></th>

		</tr>

		<form:form action="${contextPath}/partner/register" method="POST" commandName="newPartner">
			<tr id="par-add-tr">

				<td></td>

				<td><form:input path="prefix"/> <p style="font-size: small; color: red; font-style: italic;"><form:errors path="prefix"/></p></td>

				<td><form:input path="name"/><p style="font-size: small; color: red; font-style: italic;"><form:errors path="name"/></p></td>

				<td><form:select path="maxNoOp">
						<form:option value="5" label="5" />
						<form:option value="10" label="10" />
						<form:option value="15" label="15" />
						<form:option value="20" label="20" />
					</form:select></td>

				<td><form:select path="status">
						<form:option value="1" label="Aktivan" />
						<form:option value="0" label="Neaktivan" />
					</form:select></td>
                    
				<td><input type="submit" value="Registruj" /></td>
				<td></td>
				<td></td>
			</tr>
		</form:form>


			<c:forEach items="${partners}" var="partner">
               <c:choose>
               <c:when test="${partner.id==noviPartner}">
               <tr style="background-color: silver;">
               </c:when>
               <c:otherwise>
               <tr>
               </c:otherwise>
               </c:choose>
				
					<td>${partner.id}</td>
					<td>${partner.prefix}</td>
					<td>${partner.name}</td>
					<td>${partner.maxNoOp}</td>
					<td><c:choose>
							<c:when test="${partner.status==0}">Neaktivan</c:when>
							<c:otherwise>Aktivan</c:otherwise>
						</c:choose></td>
					
					<td><button type="button" id="edit-${partner.id}" class="par-edit-btn" title="${partner.id}">Izmeni</button></td>
					<td><button type="button" id="edit-${partner.id}" class="par-showOp-btn" title="${partner.id}">Prikazi operatere</button></td>
					<td><a href="${pageContext.request.contextPath}/cenovnik/paketiPrikaz?id=${partner.id}"><button>Cenovnik</button></a></td>
              
				</tr>

			</c:forEach>
			
			
			<tr id="par-edit-tr" class="upd-row">
			<td id="edit-id"></td>
			<td id="edit-prefix"></td>
			<td><input type="text" name="name" value="" id="edit-name" onchange="validateLength(this,5,30)"/></td>
			<td><select name="maxNoOp" id="edit-maxNoOp">
				<option value="5">5</option>
				<option value="10">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
				</select></td>
			<td><select name="status" id="edit-status">
				<option value="0">Neaktivan</option>
				<option value="1">Aktivan</option>
				</select></td>
			<td><button type="button" id="edit-update-btn">Sacuvaj</button></td>
			<td><button type="button" class="edit-cancel-btn">Odustani</button></td>
		</tr>
	</table>
	</div>
	<div id="list" style="float:right;margin-right: 100px;"></div>
	
		</div>
		
		</div>
</body>
</html>