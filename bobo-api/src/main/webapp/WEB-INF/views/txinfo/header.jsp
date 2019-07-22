<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%
	String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html style="height:100%">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="<%=ctx%>/res/s/jquery.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.json.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.fix.clone.js" type="text/javascript"></script>
</head>

<body style="background:#55aa55">
	<h2 style="color:white">${coreName} 
		≤‚ ‘∆ΩÃ® 
		<select id="versionSelect" onchange="toVersion()">
			<option value="0">ALL</option>
		</select>
	</h2>
	<div style="float:right"></div>


<input type="hidden" id="versions" value='${versions}' />	
<input type="hidden" id="coreName" value='${coreName}' />	
<script type="text/javascript">
	var s = $('#versions').val();
	var versions = JSON.parse(s);
	 $("#versionSelect option").remove();
	for (var int = 0; int < versions.length; int++)
	{
		var item = versions[int];
		$("#versionSelect").append('<option value=' + item + ' >' + item + '</option>');
	}
	
	function toVersion(obj){
	    var coreName = $('#coreName').val();
	    var version =$('#versionSelect option:selected') .val();
	    window.parent.frames["leftframe"].location.href='left?coreName='+coreName+"&version="+version
	}
</script>	
	
</body>
	
</html>

