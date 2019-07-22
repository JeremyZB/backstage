<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%
	String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html style="height: 100%">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link href="<%=ctx%>/res/s/zTreeStyle/zTreeStyle.css" rel="stylesheet"
	type="text/css" />

<script src="<%=ctx%>/res/s/jquery.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.json.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.fix.clone.js" type="text/javascript"></script>

<script src="<%=ctx%>/res/s/jquery.ztree.all-3.5.js"
	type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.tmpl.js" type="text/javascript"
	charset="utf-8"></script>
<script src="<%=ctx%>/res/s/jsformat.js" type="text/javascript"
	charset="utf-8"></script>
<script src="<%=ctx%>/res/s/apiform.js?v=20171022" type="text/javascript"
	charset="utf-8"></script>
</head>
<style>
ul {
	-moz-user-select: none;
	list-style: none;
	padding: 0px;
	margin: 0px;
}

ul li {
	line-height: 24px;
	padding-left: 2px;
}

ul a {
	text-decoration: none;
	text-decoration: none;
	font-weight: normal;
	color: #333333;
}

.mhead {
	color: white;
	background: #333;
}

h3 {
	font-size: 13px;
	color: white;
	margin: 0;
	padding-left: 5px;
	line-height: 24px;
	background: #333;
}

.tableborder {
	border-collapse: collapse;
	border: 1px solid black;
}

.tableborder td {
	border: 1px solid #000000;
	text-align: center;
}

.handletd a {
	padding: 5px
}

.margin5 {
	margin: 5px
}
</style>
<body
	style="margin: 0; border-right: 0px solid #333; overflow: auto; height: 100%">
	<h3 style="">txtest菜单</h3>


	<form method="post" action="txcall.do">

		<div class="" style="width: 78%; overflow: auto;">
			<div class="hh">输入参数HEAD</div>
			<table>
				<tr>
					<td width="600">
						<table>
							<tbody id="headparams">

							</tbody>
						</table>
					</td>
				</tr>
			</table>
			<div class="hh">输入参数BODY</div>
			<div id="bodyparamsdiv">
				<table>
					<tr>
						<td width="600">
							<table>
								<tbody id="simpleparams">

								</tbody>
							</table>
						</td>
					</tr>
				</table>
			</div>
			<div style="text-align: center" class="margin5">
				<input type="submit" id="submitform" value="提交">
				<input type="button" id="docButton" value="文档">
			</div>
		</div>
		<div class=""
			style="position: fixed; right: 10px; z-index: 0; top: 30px; bottom: 0; overflow: auto; width: 20%;">
			<textarea id="result"
				style="width: 100%; height: 600px; box-sizing: border-box;"></textarea>
		</div>
	</form>
</body>

<script type="text/javascript">
$(document).ready(function() {
	$("#docButton").click(function() {
		var cls = $('#cls').val();
		var method = $('#method').val();
		var coreName = $('#coreName').val();
		window.open('doc?cls='+cls+'&method='+method+"&coreName="+coreName);
	});
	
	$(".javaBeanListInput").ready(function() {
		// 获取所有的input
		var input = $(".javaBeanListInput");
		for(index in input) {
			var data = input[index];
			var name = data["name"];
			if(name != null) {
				var idx = name.indexOf('<');
				if (idx > -1) {
					name = name.substring(0,idx);
				}
				data["name"] = name;
			}
		}
	});
})
</script>

<script id="javaBeanListTmpl" type="text/x-jquery-tmpl">
<div class="margin5">入参名称:\${key}</div>
<table class="tableborder">
	<thead>
		<tr>
			{{each(i,filed) fileds[0]}}
			<td>\${i}</td> {{/each}}
			<td style="width: 50px;text-ali">操作</td>
		</tr>
	</thead>
	<tbody class="javaBeanList" key="\${key}">
		<tr>
			{{each(i,filed) fileds[0]}}
			<td><input type="text" name="\${i}"
				class="javaBeanListInput"></td> 
            {{/each}}
			<td class="handletd"><a href="javascript:;" class="addrow" htype="1">+</a> <a
				href="javascript:;" class="delrow" htype="1">-</a></td>
		</tr>
	</tbody>
</table>							
</script>

<script id="javaBeanTmpl" type="text/x-jquery-tmpl">
<div class="margin5">入参名称:\${key}</div>
<table class="tableborder">
	<tbody class="javaBean" key="\${key}">
        {{each(i,filed) fileds}}
            <tr>
                {{if i==0}}
			    <td rowspan="\${len}">\${key}</td>
                {{/if}}
                <td>\${filed}</td>
                <td><input type="text" name="\${filed}" class="javaBeanListInput"></td>
		    </tr>
		{{/each}}
	</tbody>
</table>								
</script>

<script id="linearArrayTmpl" type="text/x-jquery-tmpl">
<div class="margin5">入参名称:\${key}</div>
<table class="tableborder">
	<tbody class="linearArray" key="\${key}">
            <tr>
			    <td>\${key}</td>
                <td><input type="text" name="\${key}" class="javaBeanListInput"></td>
<td class="handletd"><a href="javascript:;" class="addrow" htype="2">+</a> <a
				href="javascript:;" class="delrow" htype="2">-</a></td>
		    </tr>
	</tbody>
</table>								
</script>

<script id="mapTmpl" type="text/x-jquery-tmpl">
<div class="margin5">入参名称:\${key}</div>
<table class="tableborder">
	<tbody class="map" key="\${key}">
            <tr class="mapTr">
			    <td><input type="text"  class="mapKey"></td>
                <td><input type="text"  class="mapValue"></td>
<td class="handletd"><a href="javascript:;" class="addrow" htype="3">+</a> <a
				href="javascript:;" class="delrow" htype="3">-</a></td>
		    </tr>
	</tbody>
</table>								
</script>
<input type="hidden" value='${inputs}' id="inputs" />
<input type="hidden" value='${cls}' id="cls" />
<input type="hidden" value='${method}' id="method" />
<input type="hidden" value='${coreName}' id="coreName" />
</html>
