<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body onload="btnClick('#showIstorija')">

<div class="main-container">

<div style="margin-left: 100px;float: left;">

<c:if test="${cenovnikKreiran}"><p style="font-size: small; color: green; font-style: italic;">Cenovnik uspesno izmenjen !</p></c:if>

<h1>Aktuelni cenovnik</h1>
<div id="osnovniPaket" style="margin-bottom: 20px;">
<table class="opr-tables">
<tr><th>Osnovni paket</th><th>Cena</th><th>Od</th></tr>
<c:if test="${osnovniPaket!=null}">
<tr><td>${osnovniPaket[0]}</td><td>${osnovniPaket[1]}</td><td>${osnovniPaket[2]}</td></tr>
</c:if>
</table>
</div>

<div style="margin-bottom: 20px;">
<table class="opr-tables">
<tr><th>Doplata</th><th>Cena</th><th>Od</th><th>Promo</th></tr>
<c:forEach items="${doplatniPaketi}" var="dp">
<tr><td>${dp[0]}</td><td>${dp[1]}</td><td>${dp[2]}</td><td>${dp[3]}</td></tr>
</c:forEach>
</table>
</div>

<div style="margin-bottom: 20px;">
<table class="opr-tables">
<tr><th>Kombinacije</th><th>Cena</th><th>Od</th></tr>
<c:forEach items="${kombinacije}" var="ko" varStatus="i">
<tr><td>${ko[0]}</td><td>${ko[1]}</td><td>${ko[2]}</td></tr>
</c:forEach>
</table>
</div>
</div>
<button id="showIstorija">Prikazi/sakrij istoriju</button>
<a href="${pageContext.request.contextPath}/cenovnik/paketi?id=${id}"><button>Izmeni cenovnik</button></a>

<div id="istorija" style="float:right; margin-right: 100px;">

<h1>Promene</h1>

<div style="margin-bottom: 20px">
<table class="opr-tables">
<tr><th>Osnovni paketi</th><th>Cena</th><th>Kreator</th><th>Od</th><th>Do</th></tr>
<c:forEach items="${istorijaO}" var="ist">
<tr>
<td>${ist[0]}</td><td>${ist[1]}</td><td>${ist[2]}</td><td>${ist[3]}</td><td>${ist[4]}</td>
</tr>
</c:forEach>
</table>
</div>


<div style="margin-bottom: 20px">
<table class="opr-tables">
<tr><th>Doplata</th><th>Cena</th><th>Kreator</th><th>Od</th><th>Do</th><th>Promo</th></tr>
<c:forEach items="${istorijaD}" var="ist">
<tr>
<td>${ist[0]}</td><td>${ist[1]}</td><td>${ist[2]}</td><td>${ist[3]}</td><td>${ist[4]}</td><td>${ist[5]}</td>
</tr>
</c:forEach>
</table>
</div>

<div style="margin-bottom: 20px">
<table class="opr-tables">
<tr><th>Kombinacije</th><th>Cena</th><th>Kreator</th><th>Od</th><th>Do</th></tr>
<c:forEach items="${istorijaK}" var="ist">
<tr>
<td>${ist[0]}</td><td>${ist[1]}</td><td>${ist[2]}</td><td>${ist[3]}</td><td>${ist[4]}</td>
</tr>
</c:forEach>
</table>
</div>


</div>
<div style="clear: both;"></div>
</div>


</body>
</html>