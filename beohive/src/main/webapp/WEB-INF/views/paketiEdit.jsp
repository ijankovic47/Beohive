<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

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
<c:when test="${dolazak==2}">
<c:set var="action" value="createCenovnik"/>
<c:set var="dugme" value="Sacuvaj"/>
<body onload="f()">
</c:when>
<c:otherwise><c:set var="action" value="createKombinacije"/>
<c:set var="dugme" value="Dalje"/>
<body>
</c:otherwise>

</c:choose>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<div class="main-container">		<!--  POČETAK GLAVNOG KONTEJNERA -->

<div id="wrapper" style="overflow: hidden;">


<form:form action="${action}" commandName="pc">

<div style="float: left; margin-left: 100px; margin-right: 20px;'" id="form">
<p style="color: red;">${poruka}</p>
<table class="opr-tables">
<tr><th>Paketi</th><th>Iskljuceno</th><th>Osnovni</th><th>Doplatni</th><th>Promo</th><th>Cena</th></tr>


<c:forEach items="${paketi}" var="paket" varStatus="i" begin="0">
<tr><td id="name${i.index}">${paket}</td><td><form:radiobutton name="tip${i.index}" value="I" path="tipovi[${i.index}]" checked="checked"/></td><td><form:radiobutton name="tip${i.index}" value="O" path="tipovi[${i.index}]"/></td>
<td><form:radiobutton name="tip${i.index}" value="D" path="tipovi[${i.index}]"/></td>
<c:choose><c:when test="${paket=='Arena234 Package' || paket=='Pink XXX Package'}"><td><form:radiobutton name="tip${i.index}" value="P" path="tipovi[${i.index}]"/></td></c:when>
<c:otherwise><td></td></c:otherwise>
</c:choose>

<td><c:choose>
<c:when test="${pc.cene[i.index]==null}"><form:input type="number" name="cena${i.index}" id="cena${i.index}" min="0" path="cene[${i.index}]" disabled="true"/></c:when>
<c:otherwise><form:input type="number" name="cena${i.index}" id="cena${i.index}" min="0" path="cene[${i.index}]"/></c:otherwise>
</c:choose></td>
</tr>
</c:forEach>

<tr><td>Cena osnovnog paketa</td><td><form:input type="number" name="opc" min="0" path="cenaOsn"/></td></tr>
</table>

<form:input type="hidden" value="${partnerId}" path="partnerId"/>
</div>


<c:if test="${dolazak==2}">

<div id="prviKorak" style="float: left;">

<div id="osnovniPaket" style="margin-bottom: 20px; margin-left: 100px;">
<table class="opr-tables">
<tr><th>Osnovni paket</th><th>Cena</th></tr>
<c:if test="${osnovniPaket!=null}">
<tr><td>${osnovniPaket[0]}</td><td>${osnovniPaket[1]}</td></tr>
</c:if>
</table>
</div>

<div style="margin-bottom: 20px; margin-left: 100px;">
<table class="opr-tables">
<tr><th>Doplata</th><th>Cena</th><th>Promo</th></tr>
<c:forEach items="${doplatniPaket}" var="dp">
<tr><td>${dp[0]}</td><td>${dp[1]}</td><td>${dp[3]}</td></tr>
</c:forEach>
</table>
</div>

</div>
</c:if>

<c:choose>
<c:when test="${dolazak==1}">
<a href="${pageContext.request.contextPath}/cenovnik/paketiPrikaz?id=${id}"><button type="button">Nazad</button></a>
</c:when>
<c:otherwise>
<a href="${pageContext.request.contextPath}/cenovnik/paketi?id=${id}"><button type="button">Nazad</button></a>
</c:otherwise>
</c:choose>


<div style="float:right; margin-right: 700px;'">
<c:if test="${kombinacije.size()>0}">

<table class="opr-tables">
<tr><th>Kombinacija</th><th>Cena</th></tr>
<c:forEach items="${kombinacije}" var="kombinacija" varStatus="i">
<c:set var="cena" value="${0}"></c:set>

<c:forEach items="${pc.kombinacijeIme}" var="ime" varStatus="j">
<c:if test="${kombinacija.equals(ime)}"><c:set var="cena" value="${pc.kombinacije[j.index]}"></c:set></c:if>
</c:forEach>

<tr><td><form:input path="kombinacijeIme[${i.index}]" value="${kombinacija}" readonly="true" size="80"/></td><td><form:input path="kombinacije[${i.index}]" type="number" min="0" value="${cena}"/></td></tr>
</c:forEach>
</table>
</c:if>


<c:if test="${kombinacije.size()==0}"><p>Nema kombinacija</p></c:if>
</div>
<input type="submit" value="${dugme}"/>
</form:form>



</div> 	<!--  KRAJ GLAVNOG KONTEJNERA -->
</div>

</body>
</html>