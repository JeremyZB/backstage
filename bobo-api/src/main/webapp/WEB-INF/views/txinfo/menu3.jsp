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
	<h3 style="">�˵� <button onclick="expandTree()" style='float:right;margin-right:10px' title='չ����'>-</button> <button style='float:right;margin-right:10px' onclick="closeTree()" title='������'>+</button></h3>
	<div>
        �ӿ�����<input id="keyword" type="text" style='width: 120px;' placeholder="���ʹ�ö��Ÿ���" onkeyup="filter(this)"/>
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
        url: "txinfo/txlist?coreName="+coreName+"&version="+version,//�����action·��  
        error: function () {//����ʧ�ܴ�����  
            alert("����ʧ��");  
        },  
        success:function(data){ //����ɹ���������    
           zNodes = data;   //�Ѻ�̨��װ�õļ�Json��ʽ����treeNodes  
        }  
    });  
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
   // zTree = $("#treeDemo").zTree(setting, treeNodes); 
}); 

	//չ����
    function expandTree() {
        var tree = $.fn.zTree.getZTreeObj('treeDemo');
        tree.expandAll(tree);
    }
	
	//��������ֻչ�����ڵ��µ�һ���ڵ�
    function closeTree() {
        var tree = $.fn.zTree.getZTreeObj('treeDemo');
        //��ȡ zTree ��ȫ���ڵ����ݽ��ڵ�����ת��Ϊ�� Array ��ʽ
        var nodes = tree.transformToArray(tree.getNodes());
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].level == 0){
                //���ڵ�չ��
                tree.expandNode(nodes[i-1],true,true,false)
            }else{
                tree.expandNode(nodes[i-1],false,true,false)
            }
        }
    }
	
	// �����ӿ�

	//����ztree��ʾ����
	function filter(keyword){
		
		var value = keyword.value;
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		
        //��ʾ���صĽڵ�
        nodes = zTree.getNodesByParam("isHidden", true);
        zTree.showNodes(nodes);
        
		// ��ȡ���еĽڵ�
        var roots = zTree.getNodes();
        var hiddenNodes = new Array();
        
        //ɸѡ��Ҫ���صĽӿ�
		for(index in roots) {
			filterNodes(roots[index], value, hiddenNodes);
		}
        //���ؽڵ�
        zTree.hideNodes(hiddenNodes);
		
		//չ����
		expandTree();
	};
	
	function filterNodes(node, inputStr, filterResult){
        if(node != null){
			
            //�ӽڵ��Ƿ�������������Ľڵ�
            var childMatch = false;
            
			//�����ӽڵ�����
            var children = node.children;
            if(children != undefined){
                for(index in children){
                    var isMatch = filterNodes(children[index], inputStr, filterResult) || childMatch;
					//����Ҫ��һ��ƥ��Ż���ʾ
					if(isMatch) {
						childMatch = isMatch;
					}
                }
            } else {
				//�����Ƿ������������
				childMatch = indexOf(node.name, inputStr);
			}
            
            //���������������� �����ӽڵ㲻���������������Ľڵ�
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
				// ����һ������ֱ�ӷ���
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
