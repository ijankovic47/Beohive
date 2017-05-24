<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>	
</head>


<input type="hidden" id="ctx-path" value="${contextPath}"/>

<div class="main-container">

<div id="pretraga" style="padding: 20px;">
<h1 style="margin-top: 0px;">Pretraga</h1>
<form action="${contextPath}/administracija/pretragaSnMac" >
Serijski broj / MAC adresa<input type="text" name="sn"/>

<input type="submit" value="Trazi"/> 
</form>

<form action="${contextPath}/administracija/pretragaUid" >
Uid korisnika<input type="text" name="uid"/>

<input type="submit" value="Trazi"/> 
</form>
</div>
<hr>
<c:choose>
<c:when test="${korisnik!=null}">
<div id="korisnik" style="padding: 20px;">
<h1 style="margin-top: 0px;">Korisnik</h1>
<table class="opr-tables">
<tr><th>Id</th><th>Uid</th><th>Ime</th><th>Prezime</th><th>Partner</th><th>Uredjaj</th><th>Registrovan</th><th>Poslednja promena</th><th>Operater</th><th>Status</th><th></th><th></th></tr>
<tr><td>${korisnik.id}</td><td>${korisnik.uid}</td><td>${korisnik.ime}</td><td>${korisnik.prezime}</td>
<td>${korisnik.partner.name}</td><td>${korisnik.uredjaj.sn}</td><td>${korisnik.created_at}</td><td>${korisnik.updated_at}</td>
<td>${korisnik.operater.ime}(${korisnik.operater.partner.prefix})</td><td>${korisnik.status}</td>
<c:if test="${korisnik.status.equals('SUSPENDED')==false}">
<td><a href="${pageContext.request.contextPath}/administracija/actDeact?idUr=${korisnik.uredjaj.id}&idKor=${korisnik.id}"><button>Aktiviraj/Deaktiviraj</button></a></td>
<td><a href="${pageContext.request.contextPath}/administracija/stop?korId=${korisnik.id}"><button style="color: red;">Stopiraj</button></a></td>
</c:if>
</tr></table>
</div>
<c:if test="${korisnik.status.equals('SUSPENDED')==false}">
<hr>
<div id="uredjaj" style="overflow: hidden; padding: 20px;'">

<h1 style="margin-top: 0px;">Uredjaj</h1>

<div id="uredjajNow" style="float: left;">
<c:if test="${zamena}"><p style="color: green; font-style: italic;">Uredjaj uspesno zamenjen !</p></c:if>
<c:if test="${actDeact}"><p style="color: green; font-style: italic;">Status korisnika promenjen !</p></c:if>
<table>
<tr><th>Serijski broj</th><th>MAC adresa</th><th>Model</th><th>Status</th></tr>
<tr><td>${korisnik.uredjaj.sn}</td><td>${korisnik.uredjaj.macAdresa}</td><td>${korisnik.uredjaj.model.ime}</td>
<c:choose>
<c:when test="${korisnik.uredjaj.status==0}">
<td>Neaktivan</td>
</c:when>
<c:otherwise><td>Aktivan</td>
</c:otherwise>
</c:choose>
</tr>
</table>
</div>
<div id="uredjajChange" style="float: right; margin-right: 700px;">
<c:if test="${uredjaj}"><p style="color: red; font-style: italic;">Niste odabrali uredjaj za zamenu !</p></c:if>
<c:choose>
<c:when test="${uredjaji.size()>0}">
<form action="${pageContext.request.contextPath}/administracija/zamenaUredjaja">

<select name="uredjaj">
<option value="${0}" selected="selected">--Uredjaji za zamenu--</option>
<c:forEach items="${uredjaji}" var="uredjaj">

<option value="${uredjaj.id}"> ${uredjaj.sn} </option>

</c:forEach>
</select>

<input type="hidden" value="${korisnik.id}" name="korId"/>
<input type="submit" value="Izvrsi zamenu"/>

</form>

</c:when>
<c:otherwise>
<h1>Nema slobodnih uredjaja na raspolaganju !</h1>
</c:otherwise>
</c:choose>
</div>
</div>
<c:if test="${korisnik.status.equals('ACTIVE')}">
<hr>
<div id="paketi" style="padding: 20px;">
<h1 style="margin-top: 0px;">Osnovni paket</h1>
<ul>
<c:forEach items="${osnovniPaket}" var="op">
<li>${op}</li>
</c:forEach>
</ul>

<h1 style="margin-top: 0px;">Doplatni paketi</h1>
<ul>
<c:forEach items="${doplatniPaketi}" var="dp" varStatus="i">
<c:set var="a" value="false"></c:set>
<c:forEach items="${doplatniPaketiKorisnika}" var="dpk">
<c:if test="${dpk.equals(dp)}">
<c:set var="a" value="true"></c:set>
</c:if>
</c:forEach>

<c:choose>
<c:when test="${a}">
<a href="${pageContext.request.contextPath}/administracija/packageAddRemove?korId=${korisnik.id}&package=${dp}"><button class="doplatniPaketi" style="background-color: #66FF6B;">${dp}</button></a>
</c:when>
<c:otherwise>
<a href="${pageContext.request.contextPath}/administracija/packageAddRemove?korId=${korisnik.id}&package=${dp}"><button class="doplatniPaketi" style="background-color:#FF3838;">${dp}</button></a>
</c:otherwise>
</c:choose>

</c:forEach>
</ul>
</div>
</c:if>
</c:if>

<c:if test="${sinhronizacija==false}">
<a href="${pageContext.request.contextPath}/administracija/sinhronizuj?korId=${korisnik.id}"><button>Sinhronizuj sa platformom</button></a>
</c:if>
</c:when>

<c:otherwise>
<h1>Korisnik nije pronadjen !</h1>
</c:otherwise>
</c:choose>

</div>
