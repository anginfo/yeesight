$(function () {

    //返回
    $("#back_btn").on('click', function(){
        history.back(-1);
    })

    Request = GetRequest();
        
    //获取url将商圈主商圈名称写入页面   
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
            wgName = theRequest['wgName'];
            WGZ = theRequest['WGZ'];
            wgCode = theRequest['wgCode'];
            cyName = theRequest['cyName'];
            inCode = theRequest['inCode'];
            time = theRequest['time'];
            wgName = decodeURI(wgName);
            WGZ = decodeURI(WGZ);
            cyName = decodeURI(cyName);
            if (WGZ === 'null') {
                WGZ = '---';
            }
            $("#wgName").html(wgName);
            $("#WGZ").html(WGZ);
            $("#cyName").html(cyName);
            return theRequest;

    } 

    var params= "";
    var defaule_lineSeries = {
        type: 'line',
        smooth: false,
        showSymbol: true,
        symbol: 'emptyCircle',
        symbolSize: 5,
        itemStyle : {
            normal : {
                lineStyle:{
                    width:1,//折线宽度
                }
            }
        },
    };
    setParams();

    //拼接参数
    function setParams(){
        params = `inCode::${inCode};;time::${time};;wgCode::${wgCode}`;
    };

    console.log(params)
    getDateByCommonInterface('690_cdwl_20', params, lineSuccess);
    getDateByCommonInterface('690_cdwl_21', params, changeIcon);
    //changeIcon();
    function lineSuccess(data){
        if(data['690_cdwl_t0'][0].WGZ){
            $("#WGZ").html(data['690_cdwl_t0'][0].WGZ);
        }
        setLineData("ec01_line","零售增幅",data,'690_cdwl_t0',"VALUE");
        setLineData("ec02_line","收入增幅",data,'690_cdwl_t1',"VALUE");
        setLineData("ec03_line","达标率",data,'690_cdwl_t2',"VALUE");
        setLineData("ec04_line","用户付薪",data,'690_cdwl_t3',"VALUE");
    }
    function setLineData(echartId,ect_title,data,dataType,key){
        var xdata = [];
        var ydataS = [];
        var ydataX = [];
        var abledata = data[dataType];
        var thisMonth = time.substr(0, 6) - 0;
        for (let i = 0; i < abledata.length; i++) {
            var value=abledata[i][key] + '';
            var month = abledata[i]['DATE_DT'] - 0;
            //console.log(value,value.indexOf("."));
            // if(value.indexOf(".")==0){
            //     value="0"+value;
            // }
            // if (value.indexOf(".") != -1) {
            //     value = value.substr(value.indexOf("."), value.indexOf(".") + 3);
            //     //console.log(value)
            // }
            if (month == thisMonth) {
                ydataS.push(value);
                ydataX.push(value);
            }else if (month < thisMonth) {
                ydataS.push(value);
                ydataX.push("-");
            }else {
                ydataX.push(value);
            }        
            let date = abledata[i]['DATE_DT'].substr(0, 4) + '.' + abledata[i]['DATE_DT'].substr(4, 2) + '';
            xdata.push(date);
        }
        console.log(xdata)
        let commonLine = echarts.init($("#"+echartId)[0]);
        commonLine.clear();
        commonLine.setOption(com_line);
        commonLine.setOption({
            color: "#0083b3",
            grid: {
                left: '3%',
                bottom: '5%',
                right: '5%',
                top:'30%'
            },
            tooltip: {
                show: false,
            },
            title: {
                text: ect_title,
                left: 'center',
                top:5*bodyScale,
                textStyle:{
                    color:'white',
                    fontSize:15*bodyScale
                }

            },
            xAxis: [
                {
                    position: 'bottom',
                    name: '月份',
                    nameGap: 15 * bodyScale,
                    boundaryGap: 0,
                    axisLabel: { //标签名称
                        margin: 2 * bodyScale,
                        fontSize: 13 * bodyScale,
                        interval: 0,
                        formatter: function(data){
                            var xAxisTime = data.substr(0, 4) + data.substr(5, 2);
                            if (parseInt(xAxisTime) > parseInt(time.substr(0, 6))){
                                return data.substr(5, 2) + "E";
                            }
                            return data.substr(5, 2);
                        },
                    },
                    axisTick: {
                        show: true,
                        alignWithLabel: true
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#0083b3'
                        }
                    },
                    data: xdata,
                }

            ],
            yAxis: {
                show: false,
            },
            series: [{
                data: ydataS,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize:13*bodyScale,
                        color: '#f9ae15',
                        formatter: function(data){
                            let dataValue = data.data;
                            if(data.dataIndex == 9){
                                return ''
                            }
                            if (dataType === '690_cdwl_t3') {
                                dataValue -= 0;
                                return dataValue.toFixed(1)
                            }else if (data) {
                                return toPercent(dataValue)
                            }
                            return dataValue;
                        },
                    }
                },
                lineStyle:{
                    normal:{
                        width:2
                    }
                }
            },{
                data: ydataX,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize:13*bodyScale,
                        color: '#f9ae15',
                        formatter: function(data){
                            let dataValue = data.data;
                            if (dataType === '690_cdwl_t3') {
                                dataValue -= 0;
                                return dataValue.toFixed(1)
                            }else if (data) {
                                return toPercent(dataValue)
                            }
                            return dataValue;
                        },
                    }
                },
                lineStyle:{
                    normal:{
                        width:2,
                        type:'dotted',
                    }
                }
            }
            ].map(function (item) {
                return $.extend(item, defaule_lineSeries)
            }),
        });
    }
    function changeIcon(data){
        console.log(data)
        var backData=data["690_cdwl_w21"][0];
        var v1=backData["SJ_LSZF_HB"];
        var v2=backData["SJ_SRZF_HB"];
        getBackStyle(v1,"arrow_box_0");
        getBackStyle(v2,"arrow_box_1");

    }
    function getBackStyle(nums,ids){
        var style="arrow_up";
        /*if(nums==undefined){
            $("#"+ids).addClass(style);
            return;
        }*/
        if(nums.indexOf(".")==0){
            nums="0"+nums;
        }
        if(parseFloat(nums)>0){
            style="arrow_up";
        }else  if(parseFloat(nums)<0){
            style="arrow_down";
        }else{
            style="arrow_on";
        }
        $("#"+ids).addClass(style);
    }
})