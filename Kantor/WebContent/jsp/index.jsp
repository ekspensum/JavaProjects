<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kantor walutowy</title>
</head>
<body bgcolor="gray">
<!-- <a href="http://localhost:8080/Kantor/logowanie">Zaloguj</a> -->
<br/>
<p>Witamy w kantorze walutowym</p>
<p>Aktualne kursy walut:</p>
<br>
<b>USD kupno: <fmt:formatNumber pattern="#0.0000" value="${1 / kurs.pln_usd * mnoznik.dolarBid }"  minFractionDigits="4" maxFractionDigits="4" /></b><br/>
<b>USD sprzedaż: <fmt:formatNumber pattern="#0.0000" value="${1 / kurs.pln_usd * mnoznik.dolarAsk }"  minFractionDigits="4" maxFractionDigits="4" /> </b><br/>
<br>
<b>EUR kupno: <fmt:formatNumber pattern="#0.0000" value="${1 / kurs.pln_eur * mnoznik.euroBid }"  minFractionDigits="4" maxFractionDigits="4" /></b><br/>
<b>EUR sprzedaż: <fmt:formatNumber pattern="#0.0000" value="${1 / kurs.pln_eur * mnoznik.euroAsk }"  minFractionDigits="4" maxFractionDigits="4" /> </b><br/>
<br>
<b>CHF kupno: <fmt:formatNumber pattern="#0.0000" value="${1 / kurs.pln_chf * mnoznik.frankBid }"  minFractionDigits="4" maxFractionDigits="4" /></b><br/>
<b>CHF sprzedaż: <fmt:formatNumber pattern="#0.0000" value="${1 / kurs.pln_chf * mnoznik.frankAsk }"  minFractionDigits="4" maxFractionDigits="4" /> </b><br/>
<br>
<br>
</body>
</html>