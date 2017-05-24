<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	
<c:set var="part_id"><sec:authentication property="principal.partner.id"/></c:set>  <!-- Postavljam ID Partnera kao globalnu promenljivu na strani. -->
<c:choose>
<c:when test="${errors>0 || dupliPodaci==true || poruka!=null}"><body></c:when>         
<c:otherwise><body onload="btnClick('#ur-add-btn')"></c:otherwise> 
</c:choose>

<div class="main-container">
	<input type="hidden" id="ctx-path" value="${contextPath}"/>
	
	<div id="wrapper" style="margin:auto;margin-left:100px;">
	<h1>Uredjaji</h1>
	
	
	<p>Filter za pretragu</p>
	
	<form action="${contextPath}/uredjaj/filter">
	<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	
	<sec:authorize access="hasRole('BEOTEL')">
	<select name="pid">
		<option value="${0}">-- partner --</option>
		<c:forEach items="${partneri}" var="partner">
		<option value="${partner.id}">${partner.name}</option>
		</c:forEach>
	</select>
	</sec:authorize>
	
	<sec:authorize access="hasRole('OPERATER')">		<!-- Za rolu "OPERATER" uzmi vrednost njegovog Partnera-->	
		<input type="hidden" name="pid" value="${part_id}">
	</sec:authorize>
	
	<select name="mid">
	<option value="${0}">-- model --</option>
	<c:forEach items="${modeli}" var="model">
	<option value="${model.id}">${model.ime}</option>
	</c:forEach>
	</select>
	
	<input type="submit" value="Prikazi" />
	</form>
	
	
	<c:if test="${registracija}"><p style="font-size: small; color: green; font-style: italic;">Registracija uredjaja uspesno izvrsena!</p></c:if>
		<c:if test="${dupliPodaci==true && snMac==1}"><p style="font-size: small; color: red; font-style: italic;">Registracija nije uspela! Serijski broj novog uredjaja (${iter}) nije jedinstven !</p></c:if>
	<c:if test="${dupliPodaci==true && snMac==2}"><p style="font-size: small; color: red; font-style: italic;">Registracija nije uspela! Mac adresa novog uredjaja (${iter}) nije jedinstvena !</p></c:if>
	<c:if test="${poruka!=null}"><p style="font-size: small; color: red; font-style: italic;"> ${poruka} </p></c:if>
	
	<table class="opr-tables" style="margin-top: 30px;">
	<tr>
		<th>Id</th><th class="email-th">Serijiski broj</th><th class="email-th">MAC Adresa</th><th class="email-th">Model</th><th>Partner</th><th class="email-th">Status</th>
		<sec:authorize access="hasRole('BEOTEL')"><th colspan="2"><button type="button" id="ur-add-btn">+</button></th></sec:authorize>	
	</tr>
	
	
	<sec:authorize access="hasRole('BEOTEL')"> <!-- FORMA ZA UNOS NOVIH UREDJAJA	 -->
	<tr id="forma">
	<form:form action="${contextPath}/uredjaj/add?${_csrf.parameterName}=${_csrf.token}" commandName="uredjaj" enctype="multipart/form-data" method="POST">
	<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<td></td>
	<td colspan="2">
		Unesite excel fajl
		<input type="file" name="file"/><br><a href="${contextPath}/uredjaj/download">Preuzmi templejt</a>
	</td>
	<td><form:select id="opr-add-select" path="model.id">
	<option disabled selected> -- model -- </option>
				<c:forEach items="${modeli}" var="model">
					<form:option value="${model.id}"><c:out value="${model.ime}"/></form:option>
				</c:forEach>
			</form:select><p style="font-size: small; color: red; font-style: italic;"><form:errors path="model"/></p></td>
		<td>
		<form:select id="opr-add-select" path="partner.id">
		<option disabled selected>-- partner --</option>
				<c:forEach items="${partneri}" var="part">
					<form:option value="${part.id}"><c:out value="${part.name}"/></form:option>
				</c:forEach>
			</form:select><p style="font-size: small; color: red; font-style: italic;"><form:errors path="partner"/></p>	
		</td>	
			<td>
			<form:select path="status">
						<form:option value="${1}">Aktivan</form:option>
						<form:option value="${0}">Neaktivan</form:option>
					</form:select>
				</td>
				<td>	
			<button type="submit" id="_opr-reg-btn">Registruj</button></td>
			<td></td>
	</form:form>
	</tr>			<!--  KRAJ FORME ZA UNOS UREĐAJA -->
	</sec:authorize>
	
	<c:forEach items="${uredjaji}" var="uredjaj">	
	<tr><td>${uredjaj.id}</td><td>${uredjaj.sn}</td><td>${uredjaj.macAdresa}</td><td>${uredjaj.model.ime}</td><td>${uredjaj.partner.name}</td>
	<td><c:choose>
		<c:when test="${uredjaj.status==0}">Neaktivan</c:when>
		<c:otherwise>Aktivan</c:otherwise>
	</c:choose></td>
	<sec:authorize access="hasRole('BEOTEL')"> 
	<td><button type="button" id="edit1-${uredjaj.id}" class="ur-edit-btn" title="${uredjaj.id}">Izmeni</button></td>
	<td><button type="button" id="edit-${uredjaj.id}" class="ur-del-btn" title="${uredjaj.id}">Obrisi</button></td></sec:authorize></tr>
	</c:forEach>	
	<tr id="ur-edit-tr" class="upd-row" style="display:none;">
			<td id="edit-id"></td>
			<td><input type="text" name="name" value="" id="edit-sn"/></td>
			<td><input type="text" name="mac" value="" id="edit-mac"/></td>
			<td><select name="model" id="edit-model">
				<option disabled selected> -- model -- </option>
				<c:forEach items="${modeli}" var="model">
					<option value="${model.id}"><c:out value="${model.ime}"/></option>
				</c:forEach>
			</select></td>
			<td><select name="partner" id="edit-partner">
				<option disabled selected> -- partner -- </option>
				<c:forEach items="${partneri}" var="partner">
					<option value="${partner.id}">${partner.name}</option>
				</c:forEach>
			</select></td>
			<td><select name="status" id="edit-status">
						<option value="1">Aktivan</option>
						<option value="0">Neaktivan</option>
					</select></td>			
			<td><button type="button" id="edit-update-btn">Sacuvaj</button></td>
			<td><button type="button" class="edit-cancel-btn">Odustani</button></td>			
		</tr>
	</table>
	</div>

	
	</div>
