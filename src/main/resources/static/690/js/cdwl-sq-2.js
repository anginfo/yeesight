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
        sqName = theRequest['sqName'];
        SQZ = theRequest['SQZ'];
        sqCode = theRequest['sqCode'];
        inCode = theRequest['inCode'];
        time = theRequest['time'];
        cyName = theRequest['cyName'];
        sqName = decodeURI(sqName);
        SQZ = decodeURI(SQZ);
        cyName = decodeURI(cyName);
        if (SQZ === 'null') {
            SQZ = '---';
        }
        $("#cyName").html(cyName);
        $("#sqName").html(sqName);
        $("#SQZ").html(SQZ);
        return theRequest;   
    } 
    
    var params="";
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
        params = `inCode::${inCode};;time::${time};;sqCode::${sqCode}`;
        //console.log(params)
    };

    //params=joinParams(params,"inCode",getQueryString("inCode"));
    //params=joinParams(params,"inCode","001");
    //params=joinParams(params,"time",formatDate(new Date()));
    // params=joinParams(params,"time","20181126");
    // params=joinParams(params,"sqCode","sq001");
    getDateByCommonInterface('690_cdwl_020', params, lineSuccess);
    getDateByCommonInterface('690_cdwl_022', params, xjfl);
    getDateByCommonInterface('690_cdwl_021_sq', params, changeIcon);
    //changeIcon();

    function lineSuccess(data){
        var preData=data["690_cdwl_s020"];
        if(data['690_cdwl_s022'][0].SQZ){
            $("#WGZ").html(data['690_cdwl_s022'][0].SQZ);
        }
        setLineData("ec01_line","首位度",data['690_cdwl_s022'],"SJ_SWD");
        setLineData("ec02_line","收入增幅",data['690_cdwl_s023'],"SJ_SRZF");
        setLineData("ec03_line","零售增幅",data['690_cdwl_s024'],"SJ_LSZF");
        setLineData("ec05_line","达标率",data['690_cdwl_s025'],"SJ_DBL");
        setLineData("ec06_line","用户付薪",data['690_cdwl_s020'],"SJ_YHFX");
    }
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    //格式化日期 
    function formatDate(val){
        var result = "";
        if(val!=null && val!=''){
            var date=new Date(val);
            var m=date.getMonth()+1;
            var d=date.getDate();
            result = date.getFullYear()+""+(m<10?("0"+m):m);
        };
        return result;
    };
    function setLineData(echartId,ect_title,abledata,key){
        console.log(abledata)
        var xdata = [];
        var ydataS = [];
        var ydataX = [];
        var thisMonth = time.substr(0, 6) - 0;
        for (let i = 0; i < abledata.length; i++) {
            var value=abledata[i][key];
            var month = abledata[i]['YEAR_MON'] - 0;
            // if(value.indexOf(".")==0){
            //     value="0"+value;
            // }
            // if (value < 1) {
            //     if (value.indexOf(".") != -1) {
            //         value = value.substr(value.indexOf("."), value.indexOf(".") + 3);
            //         //console.log(value)
            //     }
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
            xdata.push(abledata[i]['YEAR_MON']);
        }
        let commonLine = echarts.init($("#"+echartId)[0]);
        commonLine.clear();
        commonLine.setOption(com_line);
        commonLine.setOption({
            color: "#0083b3",
            grid: {
                left: '3%',
                bottom: '5%',
                right: '8%',
                top:'30%'
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
            tooltip: {
                show: false,
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
                            if (parseInt(data) > parseInt(time.substr(0, 6))){
                                return data.substr(4, 2) + "E";
                            }
                            return data.substr(4, 2);
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
            // xAxis: {
                
            //     name: '月份',
            //     nameGap: 2 * bodyScale,
            //     axisLabel: { //标签名称
            //         margin: 2 * bodyScale,
            //         fontSize: 13 * bodyScale,
            //         interval: 0,
            //         formatter: function(data){
            //             return data.substr(4, 2);
            //         },
            //     },
            //     axisTick: {
            //         show: true,
            //         alignWithLabel: true
            //     },
            //     data: xdata,
            // },
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
                            if (key === 'SJ_SWD' || key === 'SJ_YHFX') {
                                dataValue -= 0;
                                return dataValue.toFixed(1);
                            } else {
                                return toPercent(dataValue);
                            }
                        },
                    }
                },
                smooth:false,
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
                            if (key === 'SJ_SWD' || key === 'SJ_YHFX') {
                                dataValue -= 0;
                                return dataValue.toFixed(1);
                            } else {
                                return toPercent(dataValue);
                            }
                        },
                    }
                },
                smooth:false,
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

// 纵横匹配-网格星级分类
    function xjfl(data) {
        var abledata = data['690_cdwl_s026'][0];
        console.log(abledata);
        var xdata = [];
        var ydata = [];
        for (let i = 1; i <= 5; i++) {
            xdata.push(abledata[`XJ${i}`]);
            ydata.push(abledata[`ZB${i}`]);
        }
        //console.log(xdata)
        let ec003_line = echarts.init($("#ec04_bar")[0]);
        ec003_line.clear();
        ec003_line.setOption(opt_bar_vertical);
        ec003_line.setOption({
            color: "#0083b3",
            title: {
                text: '网格星级分类',
                left: 'center',
                top:5*bodyScale,
                textStyle:{
                    color:'white',
                    fontSize:15*bodyScale
                }
            },
            grid: {
                left: '0%',
                bottom: '5%',
                right: '8%',
                top:'25%'
            },
            xAxis: {
                name: '星级',
                nameGap: 2 * bodyScale,
                axisLabel: { //标签名称
                    margin: 2 * bodyScale,
                    fontSize: 13* bodyScale,
                },
                data: xdata,
            },
            yAxis: {
                show: false,
            },
            series: [{
                type: "bar",
                data: ydata,
                itemStyle: {
                    normal: {
                        color: function (params) {
                            var colorList = [
                                '#f5232e',
                                '#f9ae15',
                                '#1890fe',
                                '#94de63',
                                '#247701',
                            ];
                            return colorList[params.dataIndex]
                        },
                    }
                },
                label: {
                    normal: {
                        show: true,
                        color: '#f9ae15',
                        position: 'top',
                        fontSize:13*bodyScale,
                         formatter: function (data) {
                            if (data.data === 0) {
                                return ''
                            }
                                return toPercent(data.data)
                            },
                    }
                },

            }
            ].map(function (item) {
                return $.extend(item, opt_bar_vertical)
            }),
        });
    }

    function changeIcon(data){
        console.log(data)
        var backData=data["690_cdwl_s021"][0];
        var v1=backData["SJ_SWD_HB"];
        var v2=backData["SJ_SRZF_HB"];
        var v3=backData["SJ_LSZF_HB"];
        getBackStyle(v1,"arrow_box_0");
        getBackStyle(v2,"arrow_box_1");
        getBackStyle(v3,"arrow_box_2");

    }
    function getBackStyle(nums,ids){
        var style="arrow_up";
        if(nums==undefined){
            $("#"+ids).addClass(style);
            return;
        }
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