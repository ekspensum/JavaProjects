<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kantor walutowy - logowanie</title>
</head>
<body bgcolor="gray">

<form method="POST" name="logowanie" action="http://localhost:8080/Kantor/logowanie">
<p>Login:	<input type="text" name="login" /></p>
<p>Hasło:	<input type="text" name="haslo" /></p>
<p><input type="submit" value="Zaloguj"></p>
</form>
<br/>
<b>${komunikat }</b>
<br/>

</body>
</html>