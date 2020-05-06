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
	<s:form action="Login" method="post" style="font-family:calibri;">
		<div style="padding:100px;width:20%;margin:auto;">
			<h3>Login</h3>
			<s:text name="Username:" />
			<s:textfield name="username" /><br><br>
			<s:text name="Password:" />
			<s:textfield name="password" /><br><br>
			<s:submit style="width:230px;"/>
		</div>
	</s:form>
</body>
</html>