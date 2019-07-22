<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%
	String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html style="height:100%">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link href="<%=ctx%>/res/s/zTreeStyle/zTreeStyle.css" rel="stylesheet"
	type="text/css" />

<script src="<%=ctx%>/res/s/jquery.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.json.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.fix.clone.js" type="text/javascript"></script>

<script src="<%=ctx%>/res/s/jquery.ztree.all-3.5.js" type="text/javascript"></script>
<script src="<%=ctx%>/res/s/jquery.ztree.exhide.min.js" type="text/javascript"></script>
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
</style>
<body
	style="margin:0;border-right:0px solid #333;overflow:hidden;height:100%">
	<h3 style="">菜单 <button onclick="expandTree()" style='float:right;margin-right:10px' title='展开树'>-</button> <button style='float:right;margin-right:10px' onclick="closeTree()" title='收起树'>+</button></h3>
	<div>
        接口名：<input id="keyword" type="text" style='width: 120px;' placeholder="多个使用逗号隔开" onkeyup="filter(this)"/>
    </div>
	<div style="margin:0;border-right:1px solid #abc;overflow:auto;height:94%">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	<input value='${coreName}' type="hidden" id="coreName"/>
	<input value='${version}' type="hidden" id="version"/>
</body>



<script type="text/javascript">
 
 
var setting = {
		data: {
			simpleData: {
				enable: true
			}
		}
	}; 
var zNodes= [   
	
];


$(function(){  
	var coreName = $("#coreName").val();
	var version = $("#version").val();
	  $.ajax({  
        async : false,  
        cache:false,  
        type: 'POST',  
        dataType : "json",  
        url: "txinfo/txlist?coreName="+coreName+"&version="+version,//请求的action路径  
        error: function () {//请求失败处理函数  
            alert("请求失败");  
        },  
        success:function(data){ //请求成功后处理函数。    
           zNodes = data;   //把后台封装好的简单Json格式赋给treeNodes  
        }  
    });  
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
   // zTree = $("#treeDemo").zTree(setting, treeNodes); 
}); 

	//展开树
    function expandTree() {
        var tree = $.fn.zTree.getZTreeObj('treeDemo');
        tree.expandAll(tree);
    }
	
	//收起树：只展开根节点下的一级节点
    function closeTree() {
        var tree = $.fn.zTree.getZTreeObj('treeDemo');
        //获取 zTree 的全部节点数据将节点数据转换为简单 Array 格式
        var nodes = tree.transformToArray(tree.getNodes());
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].level == 0){
                //根节点展开
                tree.expandNode(nodes[i-1],true,true,false)
            }else{
                tree.expandNode(nodes[i-1],false,true,false)
            }
        }
    }
	
	// 检索接口

	//过滤ztree显示数据
	function filter(keyword){
		
		var value = keyword.value;
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		
        //显示隐藏的节点
        nodes = zTree.getNodesByParam("isHidden", true);
        zTree.showNodes(nodes);
        
		// 获取所有的节点
        var roots = zTree.getNodes();
        var hiddenNodes = new Array();
        
        //筛选出要隐藏的接口
		for(index in roots) {
			filterNodes(roots[index], value, hiddenNodes);
		}
        //隐藏节点
        zTree.hideNodes(hiddenNodes);
		
		//展开树
		expandTree();
	};
	
	function filterNodes(node, inputStr, filterResult){
        if(node != null){
			
            //子节点是否有满足的条件的节点
            var childMatch = false;
            
			//遍历子节点数据
            var children = node.children;
            if(children != undefined){
                for(index in children){
                    var isMatch = filterNodes(children[index], inputStr, filterResult) || childMatch;
					//必须要有一个匹配才会显示
					if(isMatch) {
						childMatch = isMatch;
					}
                }
            } else {
				//自身是否符合搜索条件
				childMatch = indexOf(node.name, inputStr);
			}
            
            //自身不满足搜索条件 且其子节点不包含有满足条件的节点
            if(!childMatch){
                filterResult.push(node);
            }
            
            return childMatch;
        }else{
            return true;
        }
    };
	
	function indexOf(name, keyword) {
		if(keyword.indexOf(",") > -1) {
			var str = keyword.split(",");
			for(index in str) {
				var value = $.trim(str[index]);
				// 满足一个条件直接返回
				if(value != null && value != '' && name.indexOf(value) > -1) {
					return true;
				}
			}
		} else {
			return name.indexOf(keyword) > -1;
		}
		return false;
	}
</script>
</html>
