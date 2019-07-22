<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%
	String ctx = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=ctx%>/res/s/jquery.js" type="text/javascript"></script>
<link rel="stylesheet"
	href="<%=ctx%>/res/s/jquery-jsonview/dist/jquery.jsonview.css">
<script type="text/javascript"
	src="<%=ctx%>/res/s/jquery-jsonview/dist/jquery.jsonview.js"></script>
<script src="<%=ctx%>/res/s/doc.js" type="text/javascript"
	charset="utf-8"></script>

<title>doc</title>

<style type="text/css">
.div-req {
	float: left;
	width: 49%;
	height: 800px;
	border: 1px solid #000
}

.div-resp {
	float: left;
	width: 49%;
	margin-left: 5px;
	height: 800px;
	border: 1px solid #000;
	height: 800px;
}
</style>
</head>

<body>
	<div class="div-req">
		<h4>
			请求参数
			<button id="req-collapse-btn">折叠</button>
			<button id="req-expand-btn">展开</button>
		</h4>

		<br />
		<div id="req_json"></div>
	</div>

	<div class="div-resp">
		<h4>
			返回参数
			<button id="resp-collapse-btn">折叠</button>
			<button id="resp-expand-btn">展开</button>
		</h4>
		<br />
		<div id="resp_json"></div>
	</div>
	<input type="hidden" id="req" value='${req}'>
	<input type="hidden" id="resp" value='${resp}'>
</body>


</html>