<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	
<c:set var="partId"><sec:authentication property='principal.partner.id'/></c:set>
<div class="main-table-container">
<input type="hidden" id="ctx-path" value="${contextPath}"/>

	<sec:authorize access="hasRole('BEOTEL')">
	<div class="partner-select">
		<span>Prikaži sve operatere partnera: </span>
		<select id="opr-partn-select">
			<option disabled selected> -- partner -- </option>
			<c:forEach items="${partneri}" var="part">
				<option value="${part.prefix}"><c:out value="${part.name}"/> </option>
			</c:forEach>
		</select>
	</div>
	</sec:authorize>

	<table class="opr-tables">
		<tr class="opr-th-labels">
			<th colspan="10"><h3><c:out value="${prefix}"/> Operateri</h3></th>
			<th><button type="button" id="opr-add-btn" title="Novi Operater" class="add-btn">+</button></th>
		</tr>
		<tr class="opr-th-labels">
			<th class="id-th">ID</th><th class="common-th">Ime</th><th class="common-th">Prezime</th><th class="common-th">Korisničko ime</th>
			<th class="common-th">Lozinka</th><th class="email-th">E-mail</th><th class="common-th">Partner</th><th class="common-th">Status</th>
			<th class="common-th">Uloga</th><th colspan="2" class="button-th"></th>
		</tr>
	<!--		forma za registraciju novog operatera -->
		<form:form action="${contextPath}/operater/registruj" method="POST" commandName="opr" id="opr-reg-form">	
		<tr id="opr-add-tr" class="highlight">
			<td></td>
			<td><form:input path="ime"/></td>
			<td><form:input path="prezime"/></td>
			<td><form:input path="username"/></td>
			<td><form:password path="password"/></td>
			<td><form:input path="email" class="email-inp"/></td>
			<td><sec:authorize access="hasRole('OPERATER')"><sec:authentication property="principal.partner.name"/>				
				<form:hidden path="partner.id" id="opr-add-select" value="${partId}"/>
			</sec:authorize>
				<sec:authorize access="hasRole('BEOTEL')">
			<form:select path="partner.id" id="opr-add-select">
				 <option disabled selected> -- partner -- </option>
				<c:forEach items="${partneri}" var="part">
					<form:option value="${part.id}"><c:out value="${part.name}"/></form:option>
				</c:forEach>
			</form:select>
			</sec:authorize></td>
			<td><form:select path="active">
				<form:option value="0">Neaktivan</form:option>
				<form:option value="1">Aktivan</form:option>
			</form:select></td>			
			<td><sec:authorize access="hasRole('BEOTEL')"><select id="opr-role-select">
				<option selected value="ROLE_OPERATER">Operater</option>
				<option value="ROLE_BEOTEL">Beotel</option>
			</select></sec:authorize>
				<sec:authorize access="hasRole('OPERATER')"><input type="hidden" value="ROLE_OPERATER" id="opr-role-select">  Operater</sec:authorize>
			</td>
			<td colspan="2"><button type="button" id="opr-reg-btn">Registruj</button></td>
		</tr>

		</form:form>
		
		<c:forEach items="${operateri}" var="opr"><tr>
			<td><c:out value="${opr.id}"/></td>
			<td><c:out value="${opr.ime}"/></td>
			<td><c:out value="${opr.prezime}"/></td>
			<td><c:out value="${opr.username}"/></td>
			<td>*******<sec:authorize access="hasRole('BEOTEL')">
				<input type="image" src="${contextPath}/resources/img/edit.png" class="password-edit" title="${opr.id}"> 
				</sec:authorize></td>
			<td><c:out value="${opr.email}"/></td>
			<td><c:out value="${opr.partner.name}"/></td>
			<td><c:choose>
    				<c:when test="${opr.active=='1'}">aktivan</c:when>    
    				<c:otherwise>neaktivan</c:otherwise>
				</c:choose></td>
			<td><c:out value="${opr.role}"/></td>	
			<td><button type="button" id="edit-${opr.id}" class="opr-edit-btn" title="${opr.id}">Edit</button></td>
			<td><button type="button" id="opr-del-btn"  class="opr-del-btn" title="${opr.id}">Del</button></td>
		</tr></c:forEach>		
	</table>	
</div>	
	<!-- 	UPDATE ROW -->
	
	<form id="upd-form-row">
	<table>
		<tr id="opr-edit-tr" class="upd-row">
			<td><input type="text" name="id" id="edit-id" size="6" disabled="disabled" class="id-inp"/></td>
			<td><input type="text" name="ime" id="edit-ime" size="20"/></td>
			<td><input type="text" name="prezime" id="edit-prezime" /></td>
			<td><input type="text" name="username" id="edit-username" /></td>
			<td><input type="password" name="password" id="edit-password" readonly="readonly"/></td>
			<td><input type="text" name="email" id="edit-email" class="email-inp"/></td>
			<td><sec:authorize access="hasRole('BEOTEL')"><select name="partner" id="edit-partner">
				<option disabled selected> -- partner -- </option>
				<c:forEach items="${partneri}" var="part">
					<option value="${part.id}"><c:out value="${part.name}"/></option>
				</c:forEach>
			</select></sec:authorize>
			<sec:authorize access="hasRole('OPERATER')"><input type="hidden" value="" name="partner" id="edit-partner"/></sec:authorize>
			</td>
			<td><select name="active" id="edit-active">
				<option value="0">Neaktivan</option>
				<option value="1">Aktivan</option>
				</select></td>
			<td></td>	
			<td><button type="button" id="edit-update-btn" class="upd-btn">Update</button></td>
			<td><button type="button" class="edit-cancel-btn">Cancel</button></td>
		</tr>
	</table></form>
	
	<div class="del-dialog">
		<p>Da li ste sigurni da želite da obriš¡ete ovog operatera?</p>
		<p>
			<button type="button" id="del-btn-yes">Da</button>
			<button type="button" id="del-btn-no">Ne</button>
		</p>
	</div>
	
	<div class="opr-chpasswd-box">
		<div><input type="image" src="${contextPath}/resources/img/close.png" class="close-icon-box" /></div>
		<form id="passwd-ch-frm">
		<span>Promena lozinke</span>
		<div class="opr-chpasswd-msg"></div>
		<div><input type="password" name="password" placeholder="Nova lozinka"></div>
		<div><input type="password" name="retypePasswd" placeholder="Ponovite lozinku"></div>
		<div><button type="button" id="passwd-save-btn">Sačuvaj</button> </div>
		</form>
	</div>