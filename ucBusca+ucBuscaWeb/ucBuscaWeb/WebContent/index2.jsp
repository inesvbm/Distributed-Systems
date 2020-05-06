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
	<div class="btn-group" style="float:right">
		<c:choose>
			<c:when test="${session.logged==true}">
				<s:form action="Search" method="post" style="float:left;padding-right:30px;">
					<s:textfield name="Search" />
					<s:submit value="Search" style="font-family:calibri;"/>
				</s:form>
				
			  	<a href="<s:url action="MySearches"/>" class="btn btn-default" style="color:black;margin:20px;text-decoration:none;font-family:calibri;"><strong>Historic of searches</strong></a>		
				
				<c:choose>
					<c:when test="${session.admin==true}">
						<a href="<s:url action="AdminPriv" />" class="btn btn-default" style="color:black;margin:20px;text-decoration:none;font-family:calibri;"><strong>ADMIN privileges</strong></a>
			  			<a href="<s:url action="URLindex" />" class="btn btn-default" style="color:black;margin:20px;text-decoration:none;font-family:calibri;"><strong>URL index</strong></a>
					</c:when>
				</c:choose>
				
				<a href="<s:url action="Notifications"/>" class="btn btn-default" style="color:black;margin:20px;text-decoration:none;font-family:calibri;"><strong>Notifications</strong></a>
				
				<c:choose>
					<c:when test="${session.logged == true}">
						<a href="<s:url action="Logout"/>" class="btn btn-default" style="color:black;margin:20px;text-decoration:none;font-family:calibri;"><strong>Logout</strong></a>
					</c:when>
					<c:otherwise>
						<a href="<s:url action="Login"/>" class="btn btn-default" style="color:black;margin:20px;text-decoration:none;font-family:calibri;"><strong>Login</strong></a>
					</c:otherwise>
				</c:choose>				
			</c:when>
			<c:otherwise>
				<h1 style="color:black;margin:20px;text-decoration:none;font-family:calibri;">Access forbidden</h1>
			</c:otherwise>
		</c:choose>
	</div><br>
	
	<br><img src="image/uc.jpg" style="width:100%"><br>
</body>
</html>