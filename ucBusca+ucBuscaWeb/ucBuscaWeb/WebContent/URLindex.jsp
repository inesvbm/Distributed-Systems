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
	<s:form action="URLindex" method="post" style="font-family:calibri;">
		<c:choose>
			<c:when test="${session.logged==true}">
				<div style="padding:100px;width:20%;margin:auto;">
					<h3>Index URL</h3>
					<s:text name="Url:" />
					<s:textfield name="url" style="width:250px;"/><br><br>
					<s:submit style="width:250px;"/>
				</div>
			</c:when>
			<c:otherwise>
				<h1 style="color:black;margin:20px;text-decoration:none;font-family:calibri;">Access forbidden</h1>
			</c:otherwise>
		</c:choose>
	</s:form>
</body>
</html>