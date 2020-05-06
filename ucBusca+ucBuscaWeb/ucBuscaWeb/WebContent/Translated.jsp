<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	
	<c:choose>
		<c:when test="${session.found == true}">
			<h3>Search results</h3>
			<c:forEach items="${result}" var="item">
			
				<c:forEach items="${translated}" var="item2">
					<c:out value="${item2}"/><br>
				</c:forEach>
				
				<c:forEach items="${item}" var="item1">
					<c:out value="${item1}"/><br>
				</c:forEach>				
				<br><br>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<p>No results found!</p>
		</c:otherwise>
	</c:choose>
	
</body>
</html>