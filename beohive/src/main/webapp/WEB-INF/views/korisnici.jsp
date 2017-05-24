<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="pid"><sec:authentication property='principal.partner.id'/></c:set>

<div class="main-container main-table-container">
	<form:form modelAttribute="korisnik" action="${contextPath}/korisnici/dodaj" method="POST">
	<table class="sbc-regisr-table">
		<form:hidden path="status" value="ACTIVE"/>
		<tr class="opr-th-labels">
			<th colspan="3">Kreiranje novog korisnika</th>
		</tr>
		<tr>
			<td>Ime</td><td><form:input path="ime"/></td>
			<td rowspan="4"> Doplatni paketi:
				<div class="sbc-paketi-box">
				<c:if test="${empty paketi}" >
					<input type="hidden" name="paketi" value=""> 
					<div class="error-msg">Partner nema paketa!</div>
				</c:if>
					<c:forEach items="${paketi}" var="p" varStatus="i">					
							<c:choose>
							  <c:when test="${p.tip eq 'o'.charAt(0)}">
							   	<input type="hidden" value="${p.paketi}" name="paketi"/>
							  </c:when>
							  <c:when test="${p.tip eq 'd'.charAt(0)}"><p> 
							     	<button type="button" class="sbc-paket-choose-btn"><c:out value="${p.paketi}"/></button>
							     	<input type="hidden" name="paketi" class="sbc-paket-choose-inp" value="${p.paketi}" disabled="disabled">							     	
							  </p></c:when>
							</c:choose>				
					</c:forEach>
				</div>
			</td>
		</tr>
		<tr>
			<td>Prezime</td><td><form:input path="prezime"/></td>
		</tr>
		<tr>
			<td>Uređaj</td><td><form:select path="uredjaj.id">
				<option disabled selected value> -- select an option -- </option>
				<c:if test="${empty uredjaji}">				
					<option disabled value class="select-red-opt">Nema na stanju</option>
				</c:if>
				<c:forEach var="dev" items="${uredjaji}">
					<form:option value="${dev.id}">${dev.sn}</form:option>
				</c:forEach>
			</form:select></td>
		</tr>
		<tr>
			<td colspan="2"> </td>
		</tr>
		<tr>
			<td colspan="3" style="text-align:center;"><button type="submit" class="sbc-reg-btn wide-btn">Registruj</button> </td>
		</tr>
	</table>
	</form:form>
	<c:if test="${response != 'null'}">
		${response}
	</c:if>
</div>