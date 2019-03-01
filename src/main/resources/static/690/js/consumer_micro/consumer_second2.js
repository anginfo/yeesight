var XW_CODE,time;
var params;
function toPercent(point) {
	var str ="-"
		if(point != "-"){
	    str = Number(point * 100).toFixed(1);
	    str += "%";
		}
	    return str;
		
}
function toPercent2(point) {
    var str = Number(point * 100).toFixed(0);
    str += "%";
    return str;
}
function toPercent3(point) {
    var str = Number(point).toFixed(1);
    return str;
}
$(function () {

    GetRequest();

    //获取url将小微主小微名称时间写入页面
    function GetRequest() {
        var url = location.search; //获取url中"?"符后的字串

        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for(var i = 0; i < strs.length; i ++) {
                theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
            }
        }
        XW_NAME = theRequest['XW_NAME'];
        XWZ = theRequest['XWZ'];
        XW_NAME = decodeURI(XW_NAME);
        XWZ = decodeURI(XWZ);
        XW_CODE = theRequest['XW_CODE'];
        time = theRequest['time'];
        params="xwCode::"+XW_CODE+";;time::"+time;
        $(".content_left_table").html('');
        //根据有无'year'字段显示不同表格
        if(theRequest['year']){
            year=  theRequest['year']
            $("#tit").html(year+"年承接差");
            getDateByCommonInterface('690_yhxw_yj_009', params, setYearTableData);
        }else{
            month = theRequest['month']
            $("#tit").html(month+"月承接差");
            getDateByCommonInterface('690_yhxw_yj_010', params, setMonthTableData);
        }
    }
    //小于零变成红色
    function getColor(input){
        if(parseFloat(input)<0){
            return " color:red;"
        }
        return "";
    }
    //月份表格
    function setMonthTableData(data){
        var arr=data["690_yhxw_010"];

        var html='<ul><li style="width:20%">纵横匹配</li><li style="width:20%">目标</li><li style="width:20%">承接</li><li style="width:20%">实际</li><li style="width:20%">差</li></ul>';
        html+='<ul class="axis">纵轴</ul>';
        $.each(arr,function(index,item){
        	
            if(index<3){
            	if(item.PROJECT == '首位度'){
            		html+='<ul>';
                    html+='<li style="width:20%">'+item.PROJECT+'</li>';
                    html+='<li style="width:20%">'+toPercent3(AddZero(item.MB))+'</li>';
                    html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+toPercent3(AddZero(item.YS))+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent3(AddZero(item.DIFF)))+'">'+toPercent3(AddZero(item.SJ))+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent3(AddZero(item.DIFF)))+'">'+toPercent3(AddZero(item.DIFF))+'</li>';
                    html+='</ul>';
            	}else if(item.PROJECT == '收入增幅'){
            		html+='<ul>';
                    html+='<li style="width:20%">'+item.PROJECT+'</li>';
                    html+='<li style="width:20%">'+toPercent2(item.MB)+'</li>';
                    html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+toPercent2(item.YS)+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent2(item.DIFF))+'">'+toPercent2(item.SJ)+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent2(item.DIFF))+'">'+toPercent2(item.DIFF)+'</li>';
                    html+='</ul>';
            	}else if(item.PROJECT == '利润率'){
            		html+='<ul>';
                    html+='<li style="width:20%">'+item.PROJECT+'</li>';
                    html+='<li style="width:20%">'+toPercent(item.MB)+'</li>';
                    html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+toPercent(item.YS)+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent(item.DIFF))+'">'+toPercent(item.SJ)+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent(item.DIFF))+'">'+toPercent(item.DIFF)+'</li>';
                    html+='</ul>';
            	}
                
            }
        });
        html+='<ul class="axis">横轴</ul>';
        $.each(arr,function(index,item){
            if(index>=3){
            	if(item.PROJECT == '预算保障度'){
            		html+='<ul>';
            		html+='<li style="width:20%">'+item.PROJECT+'</li>';
            		html+='<li style="width:20%">'+toPercent2(item.MB)+'</li>';
            		html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+toPercent2(item.YS)+'</li>';
            		html+='<li style="width:20%;'+getColor(AddZero(item.DIFF))+'">'+toPercent2(item.SJ)+'</li>';
            		html+='<li style="width:20%;'+getColor(AddZero(item.DIFF))+'">'+toPercent2(item.DIFF)+'</li>';
            		html+='</ul>';
            	}else if(item.PROJECT == '并联节点'){
                    html+='<ul>';
                    html+='<li style="width:20%">'+item.PROJECT+'</li>';
                    html+='<li style="width:20%">'+item.MB+'人</li>';
                    html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+item.YS+'人</li>';
                    html+='<li style="width:20%;'+getColor(item.DIFF)+'">'+item.SJ+'人</li>';
                    html+='<li style="width:20%;'+getColor(item.DIFF)+'">'+item.DIFF+'人</li>';
                    html+='</ul>';
                }else if(item.PROJECT == '预实零差'){
            		html+='<ul>';
                    html+='<li style="width:20%">'+item.PROJECT+'</li>';
                    html+='<li style="width:20%">'+toPercent2(item.MB)+'</li>';
                    html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+toPercent2(item.YS)+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent(item.DIFF))+'">'+toPercent2(item.SJ)+'</li>';
                    html+='<li style="width:20%;'+getColor(toPercent(item.DIFF))+'">'+toPercent2(item.DIFF)+'</li>';
                    html+='</ul>';	
                }else if(item.PROJECT == '增值分享'){
                	html+='<ul>';
                    html+='<li style="width:20%">'+item.PROJECT+'</li>';
                    html+='<li style="width:20%">'+AddZero(item.MB)+'万</li>';
                    html+='<li style="width:20%;'+getColor(item.YS-item.MB)+'">'+AddZero(item.YS)+'万</li>';
                    html+='<li style="width:20%;'+getColor(AddZero(item.DIFF))+'">'+AddZero(item.SJ)+'万</li>';
                    html+='<li style="width:20%;'+getColor(AddZero(item.DIFF))+'">'+AddZero(item.DIFF)+'万</li>';
                    html+='</ul>';
                }
         } 	
        });
        $(".content_left_table").html(html);
    }
    //年表格
    function setYearTableData(data){
        var arr=data["690_yhxw_009"];
        var html='<ul><li>纵横匹配</li><li>目标</li><li>承接</li><li>差</li> </ul>';
        html+='<ul class="axis">纵轴</ul>';
        $.each(arr,function(index,item){
            if(index<3){
            	if(item.PROJECT == '首位度'){
            		html+='<ul>';
                    html+='<li >'+item.PROJECT+'</li>';
                    html+='<li >'+toPercent3(AddZero(item.MB))+'</li>';
                    html+='<li style="'+getColor(toPercent3(AddZero(item.DIFF)))+'">'+toPercent3(AddZero(item.YS))+'</li>';
                    html+='<li style="'+getColor(toPercent3(AddZero(item.DIFF)))+'">'+toPercent3(AddZero(item.DIFF))+'</li>';
                    html+='</ul>';
            	}else if(item.PROJECT == '收入增幅'){
            		html+='<ul>';
                    html+='<li>'+item.PROJECT+'</li>';
                    html+='<li>'+toPercent2(item.MB)+'</li>';
                    html+='<li style="'+getColor(toPercent2(item.DIFF))+'">'+toPercent2(item.YS)+'</li>';
                    html+='<li style="'+getColor(toPercent2(item.DIFF))+'">'+toPercent2(item.DIFF)+'</li>';
                    html+='</ul>';
            	}else if(item.PROJECT == '利润率'){
            		html+='<ul>';
                    html+='<li>'+item.PROJECT+'</li>';
                    html+='<li>'+toPercent(item.MB)+'</li>';
                    html+='<li style="'+getColor(toPercent(item.DIFF))+'">'+toPercent(item.YS)+'</li>';
                    html+='<li style="'+getColor(toPercent(item.DIFF))+'">'+toPercent(item.DIFF)+'</li>';
                    html+='</ul>';
            	}
            }
        });
        html+='<ul class="axis">横轴</ul>';
        $.each(arr,function(index,item){
            if(index>=3){
            	if(item.PROJECT == '预算保障度'){
            		html+='<ul>';
            		html+='<li >'+item.PROJECT+'</li>';
            		html+='<li >'+toPercent2(item.MB)+'</li>';
            		html+='<li style="'+getColor(toPercent(item.DIFF))+'">'+toPercent2(item.YS)+'</li>';
            		html+='<li style="'+getColor(toPercent(item.DIFF))+'">'+toPercent2(item.DIFF)+'</li>';
            		html+='</ul>';
            	}else if(item.PROJECT == '并联节点'){
                    html+='<ul>';
                    html+='<li >'+item.PROJECT+'</li>';
                    html+='<li >'+item.MB+'人</li>';
                    html+='<li style="'+getColor(item.DIFF)+'">'+item.YS+'人	</li>';
                    html+='<li style="'+getColor(item.DIFF)+'">'+item.DIFF+'人</li>';
                    html+='</ul>';
                }else if(item.PROJECT == '预实零差'){
            		html+='<ul>';
                    html+='<li >'+item.PROJECT+'</li>';
                    html+='<li >'+toPercent2(item.MB)+'</li>';
                    html+='<li style="'+getColor(toPercent(item.DIFF))+'">'+toPercent2(item.YS)+'</li>';
                    html+='<li style="'+getColor(toPercent(item.DIFF))+'">'+toPercent2(item.DIFF)+'</li>';
                    html+='</ul>';	
                }else if(item.PROJECT == '增值分享'){
                	html+='<ul>';
                    html+='<li >'+item.PROJECT+'</li>';
                    html+='<li >'+AddZero(item.MB)+'万</li>';
                    html+='<li style="'+getColor(AddZero(item.DIFF))+'">'+AddZero(item.YS)+'万</li>';
                    html+='<li style="'+getColor(AddZero(item.DIFF))+'">'+AddZero(item.DIFF)+'万</li>';
                    html+='</ul>';
                }
            }
        });
        $(".content_left_table").html(html);
    }

      //加零
      function AddZero(value) {
        if (value) {
            if (value == '-') {
                return value;
            }
            if (value.indexOf(".") == 0) {
                value = "0" + value;
            }
            return value;
        }else{
            return '-'
        }

    }


})