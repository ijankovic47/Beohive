<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	  <!-- Uzima context path i smešta ga u lokalnu promenljivu -->

<c:choose>
<c:when test="${errors>0 || dupliPrefiks==true}"><body></c:when>         
<c:otherwise><body onload="btnClick('#mod-add-btn')"></c:otherwise> 
</c:choose>

<div class="main-container main-table-container">
<h1>Modeli uredjaja</h1>
<input type="hidden" id="ctx-path" value="${contextPath}"/>

<c:if test="${errors>0}"><p style="font-size: small; color: red; font-style: italic;">Registracija modela nije uspela!</p></c:if>
<c:if test="${registracija}"><p style="font-size: small; color: green; font-style: italic;">Registracija modela uspesno izvrsena!</p></c:if>
<c:if test="${dupliPrefiks==true}"><p style="font-size: small; color: red; font-style: italic;">Registracija modela nije uspela! ${poruka} !</p></c:if>

<table class="opr-tables">
	<tr class="opr-th-labels"><th>Id</th><th width="150">Naziv</th><th>Naziv na platformi</th>
		<th colspan="2"><button type="button" id="mod-add-btn">+</button></th><th></th></tr>
	<form:form action="${pageContext.request.contextPath}/modeli/insertModel" commandName="model">
	<tr id="mod-add-tr">	
		<td></td>
		<td><form:input path="ime"/> <p style="font-size: small; color: red; font-style: italic;"><form:errors path="ime"/></p></td>
		<td><form:input path="imeNaPlatformi"/><p style="font-size: small; color: red; font-style: italic;"><form:errors path="imeNaPlatformi"/></p></td>
		<td><input type="submit" value="Registruj" /></td>
		<td></td>
	</tr>	
	</form:form>
	<c:forEach items="${modeli}" var="model">
	<c:choose>
	<c:when test="${model.id==noviModel}">
	<tr style="background-color: silver;">
	</c:when>
	<c:otherwise>
	<tr>
	</c:otherwise>
	</c:choose>
		
		<td>${model.id}</td>
		<td>${model.ime}</td>
		<td>${model.imeNaPlatformi}</td>
		<td><button type="button" id="edit-${model.id}" class="mod-edit-btn" title="${model.id}">Izmeni</button></td>
		<td><button type="button" id="del-${model.id}" class="mod-del-btn" title="${model.id}">Obrisi</button></td>
		</tr>
	</c:forEach>
	<tr id="mod-edit-tr" style="display:none;">
		<td id="edit-id"></td>
		<td><input type="text" name="ime" value="" id="edit-ime"/></td>
		<td><input type="text" name="imeNaPlatformi" value="" id="edit-imeNaPlatformi"/></td>
		<td><button type="button" id="edit-save-btn">Sacuvaj</button></td>
		<td><button type="button" class="edit-cancel-btn">Odustani</button></td>
	</tr>

</table>

</div>