<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/pages/taglibs.jsp"%>
<h3>Umawianie wizyty - wybór lekarza</h3>

<form method="POST" action="${pageContext.request.contextPath}/visit/patient/toReserve">
<select name="doctorId">
	<c:forEach items="${allDoctorsList }" var="doctors">
		<option value="${doctors.id }">${doctors.firstName } ${doctors.lastName }</option>
	</c:forEach>
</select>

<input type="submit" value="Wybierz" class="navigateButton" />
</form>