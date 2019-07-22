<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Insert title here</title>
</head>
<frameset rows="72,*" cols="*" frameborder="no" border="0" noresize>
    <frame name="frmMain" src="header?coreName=${coreName}" height="100" scrolling="no">
    <frameset rows="*" cols="280,*" framespacing="0" frameborder="no" border="1" bordercolor="#000000">
        <frame name="leftframe" src="left?coreName=${coreName}" width="280">
        <frame name="frmDetail" src="white.jsp">
    </frameset>
</frameset>
<noframes>
    <body topmargin="1" bgcolor="#FFFFFF" text="#000000">
    </body>
</noframes>


</html>