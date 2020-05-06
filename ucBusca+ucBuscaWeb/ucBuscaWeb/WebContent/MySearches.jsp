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
		<c:when test="${session.logged==true}">
			<c:choose>
				<c:when test="${session.historic == true}">
					<h3>Historic of searches</h3>
					<c:forEach items="${mySearches}" var="item">
						<c:out value="${item}"/><br>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<p>No searches found!</p>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<h1 style="color:black;margin:20px;text-decoration:none;font-family:calibri;">Access forbidden</h1>
		</c:otherwise>
	</c:choose>
</body>
</html>