<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>添加报税</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico">
    <link rel="stylesheet" href="${ctx}/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${ctx}/css/font-awesome.min.css" />
    <link rel="stylesheet" href="${ctx}/bjjoy/css/font-awesome.min.css" />
    <link href="${ctx}/bjjoy/css/animate.css" rel="stylesheet">
    <link href="${ctx}/bjjoy/css/style.css" rel="stylesheet">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
          	<div>
				<form action="#" class="dropzone well dz-clickable" id="dropzone">
					<div class="dz-default dz-message">
					<span>
					<span class="bigger-150 bolder">
					<i class="ace-icon fa fa-caret-right red"></i> Drop files</span> to upload 				
					<span class="smaller-80 grey">(or click)</span> <br> 				
					<i class="upload-icon ace-icon fa fa-cloud-upload blue fa-3x"></i></span></div>
				</form>
			</div>
        </div>

    </div>
    <!-- 全局js -->
    <#include "${ctx}/common.ftl">
    <script src="${ctx}/js/dropzone.min.js"></script>
    <script type="text/javascript">
    	$("#dropzone").dropzone({
        url: "/api/v1/dtax/uploadEtax", //必须填写
        method:"post",  //也可用put
        paramName:"file", //默认为file
        maxFiles:1,//一次性上传的文件数量上限
        maxFilesize: 30, //MB
        acceptedFiles: ".zip,.rar", //上传的类型
        uploadMultiple:false,
        dictMaxFilesExceeded: "您最多只能上传1个文件！",
        dictResponseError: '文件上传失败!',
        dictInvalidFileType: "你不能上传该类型文件,文件类型只能是*.zip,*.rar。",
        dictFallbackMessage:"浏览器不受支持",
        dictFileTooBig:"文件过大上传文件最大支持.",
        init:function(){
            this.on("addedfile", function(file) { 
            //上传文件时触发的事件
            });
            this.on("queuecomplete",function(file) {
                //上传完成后触发的方法
            });
            this.on("complete",function(data){
                //当文件上传成功或失败之后发生。
                if(data.status=="success"){
                	var d=JSON.parse(data.xhr.responseText);
                	console.log(d);
                	if(d.result=="00000000"){
                		layer.msg(data.name+"上传成功!", {time: 2000},function(){
	   						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	   						parent.layer.close(index);
	   					});
                	}else{
                		if(d.data=="Unable to construct record instance"){
                			layer.msg("文件异常！请将Excel打开另存为，然后将另存为文件再次上传！", {time: 5000},function(){
	   						});
                		}else{
                			layer.msg(d.data, {time: 2000},function(){
	   						});
                		}
                	}
                	
                }else{
                	layer.msg("服务异常，请重试！", {time: 2000},function(){
	   					});
                }
                console.log(data) 
            });
        }

    });
    </script>

</body>

</html>
