///var domain="http://localhost:9999/api/v1/common/interface";
var domain = "http://${ctx}/api/v1/common/interface";
var clientUrl = domain + "/getByDataType";//接口地址
var insetUrl = domain + "/insertDate";
var userCode = "A0007773";//用户编码
var industryCode = "ALL";//产业编码
var industryName;//产业名称
var vdate;//当前选择时间
var next1Month;//下一个月时间
var next2Month;//下两个月时间
var next1Jidu;//下一个季度时间
var next2Jidu;//下两个季度时间
var guZhiRongsu;//估值 融资
var module = "孵化";
var page_num = 1;
var x;
var y;
var backIndustryCode;
var backXW_CODE;
var countScroll = 0;
var echarts_max;//柱状图最大值

/**
 * 通过统一接口请求接口数据
 * @param dataType:统一接口dataType
 * @param params:接口参数
 * @param callBack:回调函数
 */
function getDateByCommonInterface(dataType, params, successCallBack, failureCallBack) {
    if (!params) {
        params += "userCode::" + userCode;//拼接统一用户信息
    } else {
        params += ";;userCode::" + userCode;//拼接统一用户信息
    };
    $.get(clientUrl, { "dataType": dataType, "params": params }, function (data, status) {
        if (status == "success") {
            var jsonData = JSON.parse(data);
            if (jsonData.result == "00000000") {
                //数据请求成功
                successCallBack(jsonData.data);
            } else {
                if (failureCallBack) {
                    failureCallBack(jsonData.data);
                };
                console.error(dataType + "请求异常，" + jsonData.errorMsg);
            }
        }
    })
};

function getDateByCommonInterfaceByParam(dataType, params, successCallBack, failureCallBack, callBackParams) {
    if (!params) {
        params += "userCode::" + userCode;//拼接统一用户信息
    } else {
        params += ";;userCode::" + userCode;//拼接统一用户信息
    };
    $.get(clientUrl, { "dataType": dataType, "params": params }, function (data, status) {
        if (status == "success") {
            var jsonData = JSON.parse(data);
            if (jsonData.result == "00000000") {
                //数据请求成功
                successCallBack(jsonData.data, callBackParams);
            } else {
                if (failureCallBack) {
                    failureCallBack(jsonData.data, callBackParams);
                };
                console.error(dataType + "请求异常，" + jsonData.errorMsg);
            }
        }
    })
};

function insetDateToServer(dataType, params, callBack) {
    if (!params) {
        params += "userCode::" + userCode;//拼接统一用户信息
    } else {
        params += ";;userCode::" + userCode;//拼接统一用户信息
    };
    $.post(insetUrl, { "dataType": dataType, "params": params }, function (data, status) {
        if (status == "success") {
            var jsonData = JSON.parse(data);
            // console.log(jsonData)
            if (jsonData.result == "00000000") {
                //数据请求成功
                callBack(jsonData);
            } else {
                alert(dataType + "请求异常，" + jsonData.errorMsg);
            }
        }
    })
}
/**
 * 提交评论
 */
function submitComment() {
    if (industryCode) {
        initVarParams();
        var timestamp = getCurrentTimestamp();
        console.log("timestamp=" + timestamp, "guZhiRongsu=" + guZhiRongsu);
        var params = "";
        params = joinParams(params, "id", timestamp);
        params = joinParams(params, "module", module);
        params = joinParams(params, "page_num", page_num);
        params = joinParams(params, "industry_code", industryCode);
        params = joinParams(params, "vmonth", vdate);
        params = joinParams(params, "weidu", guZhiRongsu);
        params = joinParams(params, "next_month", next1Month);
        params = joinParams(params, "next_month2", next2Month);
        params = joinParams(params, "next_jidu", next1Jidu);
        params = joinParams(params, "next2_jidu", next2Jidu);
        params = joinParams(params, "create_date", getCurrentDate());
        params = joinParams(params, "create_by", userCode);
        //结论
        $("div [class^='jielun']").each(function (index, item) {
            // console.log("结论" + (index + 1), $(item).val());
            params = joinParams(params, "jielun" + (index + 1), $(item).val());
        });
        // console.log(params);
        // insetDateToServer("hl_690_dp_industrycomment",params,insertCommentSuccess);
    } else {
        alert("请选择产业！");
    };
};

/**
 * 获取url中参数 从二级页面返回用
 */
function initBackParams() {
    time = getQueryString("time");
    backIndustryCode = getQueryString("industryCode");
    industryCode = backIndustryCode;
    var mType = getQueryString("guZhiRongsu");
    backXW_CODE = getQueryString("xwset");
    //默认时间
    let date = new Date(new Date());    //当前时间
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    if (day <= 4) {       // 天数小于4显示上月最后一天
        month--;
        if (month == 0) {
            month = 12;
            year--;
        }
        day = getLastDay(year, month);      //获取当月最后一天
    } else {
        day = day - 1;
    }
    if (time) {
        $("#dateIpt").val(time);
    } else {
        // $("#dateIpt").val(formatDate(new Date() - 1000 * 60 * 60 * 24));
        //$("#dateIpt").val("2018-12-31");
        $("#dateIpt").val(formatDate(`${year}-${month}-${day}`));
    };
    if (mType == "1") {
        $(".switch").find("div").eq(0).addClass("switch_1").removeClass("switch_2");
        $(".switch").find("div").eq(1).addClass("switch_2").removeClass("switch_1");
    }
    if (localStorage.getItem('cutArrow') == '1') {
        $('.right_arrow').show();
        $('.left_arrow').hide();
        localStorage.setItem('cutArrow', '0');
    }
}

/**
 * 获取当月最后一天
 * @param year 当前年份
 * @param month 当前月份
 */
function getLastDay(year, month) {
    var new_year = year;  //取当前的年份
    var new_month = month;  //取当前的月份
    if (month > 12) {
        new_month -= 12;    //月份减
        new_year++;      //年份增
    }
    var new_date = new Date(new_year, new_month, 1);   //取当年当月中的第一天
    return (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();//获取当月最后一天日期
}

/**
 * 查询方法
 */
function searchFunction() {
    initVarParams();
    disEditDom();

    // ----提交评论功能移除
    // var menu_w = $('.submitbox').css('width', '0');
    // $('.submitbox_btn').text('◀');
    // if(industryCode==undefined||industryCode=="ALL"){
    //     $(".summary").hide();
    // }else{
    //     $(".summary").show();
    // }
    // if(industryCode){
    //     // console.log(industryCode)
    //     var params="";
    //     params=joinParams(params,"module",module);
    //     params=joinParams(params,"page_num",page_num);
    //     params=joinParams(params,"industry_code",industryCode);
    //     params=joinParams(params,"vmonth",vdate);
    //     params=joinParams(params,"weidu",guZhiRongsu);
    //     params=joinParams(params,"next_month",next1Month);
    //     params=joinParams(params,"next_month2",next2Month);
    //     params=joinParams(params,"next_jidu",next1Jidu);
    //     params=joinParams(params,"next2_jidu",next2Jidu);
    //     //获取评论
    //     // getDateByCommonInterface("hl_690_dp_getindustrycomment",params,getCommentValueSuccess);
    //     var time=formatDate($("#dateIpt").val());
    //     // initComments(industryCode,time);
    // }else{
    //     //alert("请选择产业！");
    // };

    // 修改标题区域时间
    let selectTime = formatDate($("#dateIpt").val());
    selectTime = selectTime.replaceAll("-", "");
    let year = selectTime.substring(0, 4);
    let month = selectTime.substring(4, 6);
    month = Number(month);
    if (month > 9) {
        $('#yearTit').text(`${Number(year) + 1}年目标`);
    } else {
        $('#yearTit').text(`${Number(year)}年目标`);
    }
    $('#nowMonth').text(`${month}月累计预实差`);
    if (month == 12) {
        $('#monthT1').text(`${Number(year) + 1}年1月承接`);
        $('#monthT2').text(`${Number(year) + 1}年2月承接`);
    } else if (month == 11) {
        $('#monthT1').text(`${Number(year)}年12月承接`);
        $('#monthT2').text(`${Number(year) + 1}年1月承接`);
    } else {
        $('#monthT1').text(`${Number(year)}年${Number(month) + 1}月承接`);
        $('#monthT2').text(`${Number(year)}年${Number(month) + 2}月承接`);
    }
    let season = getJiduDate2(year, month);
    $('#Q1Tit').text(`${season[0]}目标`);
    $('#Q2Tit').text(`${season[1]}目标`);

    //获取下一个季度和下两个季度
    function getJiduDate2(year, month) {
        var n = parseInt(parseInt(month) / 3);//当前月属于第几季度
        var yu = parseInt(month) % 3;
        if (yu > 0) {
            n++;
        };
        if (month == 3 || month == 6 || month == 9 || month == 12) {
            var nestJiduValue = n + 1;
        } else {
            var nestJiduValue = n;
        }
        //下一个季度
        if (nestJiduValue == 5) {
            next1Jidu = (parseInt(year) + 1) + "年Q1";//下一个季度时间
            next2Jidu = (parseInt(year) + 1) + "年Q2";//下两个季度时间
        } else {
            next1Jidu = year + "年Q" + nestJiduValue;//下一个季度时间
            //下两个季度
            var nest2JiduValue = nestJiduValue + 1;
            if (nest2JiduValue == 5) {
                next2Jidu = (parseInt(year) + 1) + "年Q1";
            } else {
                next2Jidu = year + "年Q" + nest2JiduValue;
            }
        };
        return [next1Jidu, next2Jidu];
    }


    //获取融资轮次数据
    getRzlcData();
    // 设置产业信息
    // getIndustryJxInfo();
    // 获取柱状图、结论
    getEchartsData();
}

/**
 * 保存评论成功回调
 */
function insertCommentSuccess(data) {
    alert("提交成功!");
    searchFunction();
};
/**
 * 查询成功回调--设置评论数据
 */
function getCommentValueSuccess(data) {
    initVarParams();
    //当前数
    if (data.dp.length > 0) {
        $("div [class^='jielun']").each(function (index, item) {
            var value = data.dp[0]["jielun" + (index + 1)];
            if (value) {
                $(item).val(value);
            };
        });
    } else {
        resetCommentValue();
    };
    //下一个月
    if (data.dp_nextmonth.length > 0) {
        var dp_nextmonth = data.dp_nextmonth[0];
        if (next1Month == dp_nextmonth.next_month) {
            $(".jielun5").val(dp_nextmonth.jielun5);
            $(".jielun6").val(dp_nextmonth.jielun6);
        } else if (next1Month == dp_nextmonth.next_month2) {
            $(".jielun5").val(dp_nextmonth.jielun7);
            $(".jielun6").val(dp_nextmonth.jielun8);
        } else {
            $(".jielun5").val('');
            $(".jielun6").val('');
        }
    };

    //下两个月
    if (data.dp_nextmonth2.length > 0) {
        var dp_nextmonth2 = data.dp_nextmonth2[0];
        if (next2Month == dp_nextmonth2.next_month) {
            $(".jielun7").val(dp_nextmonth2.jielun5);
            $(".jielun8").val(dp_nextmonth2.jielun6);
        } else if (next2Month == dp_nextmonth2.next_month2) {
            $(".jielun7").val(dp_nextmonth2.jielun7);
            $(".jielun8").val(dp_nextmonth2.jielun8);
        } else {
            $(".jielun7").val('');
            $(".jielun8").val('');
        }
    };
    //下一个季度
    if (data.dp_nextjidu.length > 0) {
        var dp_nextjidu = data.dp_nextjidu[0];
        // console.log(dp_nextjidu)
        if (next1Jidu == dp_nextjidu.next_jidu) {
            $(".jielun9").val(dp_nextjidu.jielun9);
            $(".jielun10").val(dp_nextjidu.jielun10);
        } else if (next1Jidu == dp_nextjidu.next2_jidu) {
            $(".jielun9").val(dp_nextjidu.jielun11);
            $(".jielun10").val(dp_nextjidu.jielun12);
        } else {
            $(".jielun9").val('');
            $(".jielun10").val('');
        }
    };

    if (data.dp_nextjidu2.length > 0) {
        var dp_nextjidu2 = data.dp_nextjidu2[0];
        if (next2Jidu == dp_nextjidu2.next_jidu) {
            $(".jielun11").val(dp_nextjidu2.jielun9);
            $(".jielun12").val(dp_nextjidu2.jielun10);
        } else if (next2Jidu == dp_nextjidu2.next2_jidu) {
            $(".jielun11").val(dp_nextjidu2.jielun11);
            $(".jielun12").val(dp_nextjidu2.jielun12);
        } else {
            $(".jielun11").val('');
            $(".jielun12").val('');
        };
    };

};
/**
 * 获取柱状图、结论数据
 */
function getEchartsData() {
    var time = formatDate($("#dateIpt").val());
    time = time.replaceAll("-", "");
    var industry = $("#industryIpt").val();
    if (!industry) {
        industry = "ALL";
    }
    //查询数据
    var params = "";
    params = joinParams(params, "inCode", industry);
    params = joinParams(params, "time", time);
    // console.log(params)
    getDateByCommonInterface('690_fhxw_zxt_max', params, queryChartData);
    function queryChartData(data) {
        var abledata = data['690_fhxw_max_1'][0]
        if (abledata) {
            echarts_max = parseFloat(abledata['MAX']).toFixed(2);
        }
        // 年柱状图、结论
        getDateByCommonInterface('690_fhxw_z006', params, paintEcharts);
        // 本月柱状图、结论
        getDateByCommonInterface('690_fhxw_z001', params, paintEcharts2);
        // T+1月柱状图、结论
        getDateByCommonInterface('690_fhxw_z002', params, paintEcharts);
        // T+2月柱状图、结论
        getDateByCommonInterface('690_fhxw_z003', params, paintEcharts);
        // Q+1季度柱状图、结论
        getDateByCommonInterface('690_fhxw_z004', params, paintEcharts);
        // Q+1季度柱状图、结论
        getDateByCommonInterface('690_fhxw_z005', params, paintEcharts);
    }
}
/**
 * 绘制柱状图区域、写入结论
 */
function paintEcharts(data) {
    for (let key in data) {
        switch (key) {
            case '690_fhxw_z12': createChart(data, '690_fhxw_z12', '#ec00_bar', 'MB'); createSummary(data, '690_fhxw_z12', 'MB', '.jielun1', '.jielun2'); break;
            case '690_fhxw_z5': createChart(data, '690_fhxw_z5', '#ec04_bar', 'YS'); createSummary(data, '690_fhxw_z5', 'YS', '.jielun9', '.jielun10'); break;
            case '690_fhxw_z7': createChart(data, '690_fhxw_z7', '#ec05_bar', 'YS'); createSummary(data, '690_fhxw_z7', 'YS', '.jielun11', '.jielun12'); break;
            case '690_fhxw_z8': createChart(data, '690_fhxw_z8', '#ec06_bar', 'MB'); createSummary(data, '690_fhxw_z8', 'MB', '.jielun13', '.jielun14'); break;
            case '690_fhxw_z10': createChart(data, '690_fhxw_z10', '#ec07_bar', 'MB'); createSummary(data, '690_fhxw_z10', 'MB', '.jielun15', '.jielun16'); break;
        }
    }
}
function paintEcharts2(data) {
    // console.log(data);
    createChart(data, '690_fhxw_z3', '#ec01_bar', 'MB'); createSummary(data, '690_fhxw_z3', 'MB', '.jielun3', '.jielun4');
    createChart(data, '690_fhxw_z2', '#ec02_bar', 'YS'); createSummary(data, '690_fhxw_z2', 'YS', '.jielun5', '.jielun6');
    createChart(data, '690_fhxw_z1', '#ec03_bar', 'SJ'); createSummary(data, '690_fhxw_z1', 'SJ', '.jielun7', '.jielun8');
}
//绘制柱状图
var muBiaoData = [];
function createChart(data, dataType, oDiv, str) {
    let abledata = data[dataType];
    $(oDiv).empty();
    $(`${oDiv}_img`).css('background', 'none');
    if (!abledata[0]) {
        console.log(`${oDiv}无柱状图数据`);
    } else {
        var xdata = ['估值', '融资额', '生态收入'];
        var ydata = [Number(abledata[0][`${str}_GZ`]).toFixed(0), Number(abledata[0][`${str}_RZE`]).toFixed(0), Number(abledata[0][`${str}_SR`]).toFixed(0)];
        if (abledata[0][`${str}_GZ`] == null && abledata[0][`${str}_RZE`] == null && abledata[0][`${str}_SR`] == null) {
            console.log(`${oDiv}柱状图数据为空`);
        } else {
            if (dataType == '690_fhxw_z3') {
                muBiaoData = [].concat(ydata);
            };
            $(oDiv).removeAttr('_echarts_instance_');
            let ec0001_bar = echarts.init($(oDiv)[0]);
            ec0001_bar.clear();
            ec0001_bar.setOption(opt_bar_vertical);
            ec0001_bar.setOption({
                animation: false,
                color: "#0083b3",
                grid: {
                    left: '0%',
                    right: '10%',
                    top: '25%',
                    bottom: '3%'
                },
                xAxis: {
                    axisLabel: { //标签名称
                        margin: 2 * bodyScale,
                        fontSize: 12 * bodyScale,
                        interval: 0,
                        color: '#0083b3'
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#0083b3'
                        }
                    },
                    data: xdata,
                },
                yAxis: {
                    show: false,
                    max: echarts_max
                },
                series: [{
                    data: ydata,
                    itemStyle: {
                        normal: {
                            color: function (params) {
                                var colorList = [
                                    '#006633',
                                    '#996633',
                                    '#3366ff',
                                ];
                                return colorList[params.dataIndex]
                            },
                            label: {
                                show: true,
                                position: 'top',
                                formatter: function (data) {
                                    let dataValue = data.data;
                                    if (dataType == '690_fhxw_z2' || dataType == '690_fhxw_z1') {
                                        if (muBiaoData[data.dataIndex] > dataValue) {
                                            return '{red|' + dataValue + '}'
                                        }
                                    }
                                    return '{white|' + dataValue + '}'
                                },
                                rich: {
                                    white: {
                                        color: '#fff'
                                    },
                                    red: {
                                        color: '#f00'
                                    }
                                }
                            }
                        }
                    },
                }
                ].map(function (item) {
                    return $.extend(item, { type: 'bar' })
                }),
            });
            paintEchartsImg(oDiv, `${oDiv}_img`);
        }
    }
}
/**
 * 写入结论数据
 */
function createSummary(data, dataType, str, oDiv1, oDiv2) {
    let abledata = data[dataType][0];
    $(oDiv1).val('');
    $(oDiv2).val('');
    if (!abledata) {
        console.log(`${dataType}无结论数据`);
    } else {
        if (abledata[`${str}_YBMB`]) {
            $(oDiv1).val(abledata[`${str}_YBMB`])
        } else {
            $(oDiv1).val(abledata[`YBMB`])
        }
        $(oDiv2).val(abledata[`${str}_SJ`])
    }
}
/**
 * 获取融资轮次数据
 */
function getRzlcData() {
    var time = formatDate($("#dateIpt").val());
    time = time.replaceAll("-", "");
    // var industry=industryCode;
    var industry = $("#industryIpt").val();
    // console.log($("#industryIpt").find("option:selected").text());
    var weidu = guZhiRongsu;//估值 融资
    if (guZhiRongsu == "估值") {
        weidu = "gz";
    } else {
        weidu = "rz";
    };
    if (!industry) {
        industry = "";
    }
    var xwCode = backXW_CODE;
    if (!xwCode) {
        xwCode = "ALL";
    }
    //查询数据
    var params = "";
    params = joinParams(params, "time", time);
    params = joinParams(params, "cyCode", industry);
    params = joinParams(params, "type", weidu);
    params = joinParams(params, "xwCode", xwCode);
    // console.log(params);
    //上方数据
    getDateByCommonInterface("690_fhxw_yj_0011", params, createTopRzlcHtmlBlock, createTopRzlcHtmlBlock);
    //下方数据
    getDateByCommonInterface("690_fhxw_yj_0031", params, createBottomRzlcHtmlBlock, createBottomRzlcHtmlBlock);
};
/**
 * 将轮次数据分组
 * @type {[*]}
 */

var lunciArr = ["独角兽", "C轮", "B轮", "A轮", "天使轮"];
function diviseRzlcMap(data) {
    var map = new Map();
    for (var x = 0; x < 8; x++) {
        var lunciMap = new Map();
        for (var y = 0; y < lunciArr.length; y++) {
            lunciMap.put(lunciArr[y], []);
        };
        $.each(data["690fhxw_t" + x], function (index, item) {
            for (var y = 0; y < lunciArr.length; y++) {
                if (lunciArr.indexOf(item.zz) != -1) {
                    var preData = lunciMap.get(item.zz);
                    if (preData == undefined) {
                        preData = [];
                    }
                    if (preData.indexOf(item) == -1) {
                        preData.push(item);
                    }
                    lunciMap.put(item.zz, preData);
                }
            };

        });
        map.put(x, lunciMap);
    };
    //    console.log(map)
    return map;
};

/**
 * 将轮次数据分组
 * @type {[*]}
 */
var bottomArr = ["成立一年以上", "成立半年以上", "成立半年以内"];
function diviseBottomRzlcMap(data) {
    var map = new Map();
    for (var x = 0; x < 8; x++) {
        var lunciMap = new Map();
        for (var y = 0; y < bottomArr.length; y++) {
            lunciMap.put(bottomArr[y], []);
        };
        $.each(data["690fhxw_t" + x], function (index, item) {
            for (var y = 0; y < bottomArr.length; y++) {
                if (bottomArr.indexOf(item.zz) != -1) {
                    var preData = lunciMap.get(item.zz);
                    if (preData == undefined) {
                        preData = [];
                    }
                    if (preData.indexOf(item) == -1) {
                        preData.push(item);
                    }
                    lunciMap.put(item.zz, preData);
                }
            };

        });
        map.put(x, lunciMap);
    };
    return map;
};
/**
 * 创建上方小块HTML内容
 */
function createTopRzlcHtmlBlock(data) {
    // console.log(data);
    //step1:将返回的5个集合的每一个集合再次进行拆分
    var map = diviseRzlcMap(data);
    map.each(function (key, value, index) {
        var htmls = createTopOneBlock(value, index, true);
        //////////
        $(".lunci" + (index + 1)).html(htmls.join(''));
    });
    //重新绑定悬浮事件
    hoverFunForRzlcBlock();
    scroll();
    // 将本月数据按需显示
    // 本月目标
    checkMonthData('.now_goal', '目标');
    // 本月承接
    checkMonthData('.now_get', '承接');
    // 本月实际
    checkMonthData('.now_fact', '实际');
};
/**
 * 
 * 判断当前月数据按需显示
 */
function checkMonthData(element, val) {
    var count = 0;
    $(element).find('.p_on_tit').nextAll().each(function (i, item) {
        var txt = $(item).text();
        if (txt.indexOf(val)) {
            $(item).css('display', 'none');
        } else {
            count++;
        }
    })
    if (count == 0) {
        $(element).find('.block_scroll').css('display', 'none');
    }
}
function getIndustryJxInfo() {
    var time = formatDate($("#dateIpt").val());
    time = time.replaceAll("-", "");
    var params = "";
    params = joinParams(params, "time", time);
    params = joinParams(params, "cyCode", industryCode);
    getDateByCommonInterface("690_fhxw_yj_004", params, setIndustryJxInfo, setIndustryJxInfo);
};
/**
 * 设置产业信息
 * @param data
 */
function setIndustryJxInfo(data) {
    var d = data["690fhxw"];
    // console.log(d)
    if (d.length > 0) {
        d = d[0];
        $(".index_name_div").html(d.name);
        $(".levelStar_1").html(getSatrt(d.mbxj));
        $(".levelStar_2").html(getSatrt(d.ysxj));
        $(".levelStar_3").html(getSatrt(d.sjxj));
    }
};
function getSatrt(num) {
    var star = "";
    if (num != "-") {
        for (var x = 0; x < parseInt(num); x++) {
            star += "★";
        }
        return star;
    } else {
        return num;
    }

}
function createTopOneBlock(value, index, needLine) {
    var htmls = [];
    var hz = [];
    for (x = 0; x < 5; x++) {
        $(".numsvalue_" + (index + 1) + "_" + (x + 1)).html('');
    }
    //单条数据
    value.each(function (innerKey, innerValue, innerIndex) {
        var rate = "0/0";
        if (innerValue != null && innerValue.length > 0) {
            rate = innerValue[0].rate;
            if (rate == "null" || rate == null) {
                rate = "0/0";
            }
        }
        htmls.push('<div class="block"><div style="display:none" class="block_tit">' + rate + '</div><div class="block_scroll">');
        //console.log(innerValue);
        if (innerValue.length <= 1) {
            htmls.push('<div class="block_scroll_screen_off">');
        } else {
            htmls.push('<div class="block_scroll_screen" data-id="' + countScroll + '">');
            countScroll++;
        }
        $.each(innerValue, function (index2, item) {
            if (item['mb_lc_time'] == null) {
                item['mb_lc_time'] = '未知';
            }
            if (item['ys_lc_time'] == null) {
                item['ys_lc_time'] = '未知';
            }
            if (item['sj_lc_time'] == null) {
                item['sj_lc_time'] = '未知';
            }
            htmls.push('<div class="roll flex_center"><div class="pointer" data-id="' + (countScroll - 1) + '" xwxj="' + item.xwxj + '" xw_code="' + item.xw_code + '" data-index=' + index + ' style="margin-left:' + xJudge2(item) + ';width:100%;">');
            htmls.push('<div class="p_on" style="margin-left:' + xJudge(item) + '%;"><span class="' + starColor(item) + '"></span></div>');
            htmls.push('<div><div class="p_on_tit">' + item.xw_name + '(' + item.fr + ')<span class="' + rise(item) + '"></span></div>');
            htmls.push('<div>目标 : ' + item['mb_lc_time'] + '-' + formatNumsNew(item.gz1, null, false, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, null, false, item.rze1_ys, item.rze1_bz) + '/'
                + formatNumsNew(item.sr1, null, false, item.sr1_ys, item.sr1_bz) + '</div>');
            if (index == 0 || index == 2 || index == 3) {
                htmls.push(' <div>承接 : ' + item['ys_lc_time'] + '-' + formatNumsNew(item.gz2, item.gz1, true, item.gz2_ys, item.gz2_bz) + '/' + formatNumsNew(item.rze2, item.rze1, true, item.rze2_ys, item.rze2_bz) + '/'
                    + formatNumsNew(item.sr2, item.sr1, true, item.sr2_ys, item.sr2_bz) + ' </div>');
            } else if (index == 7) {
                htmls.push(' <div>承接 : ' + item['ys_lc_time'] + '-' + formatNumsNew(item.gz1, item.gz2, true, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, item.rze2, true, item.rze1_ys, item.rze1_bz) + '/'
                    + formatNumsNew(item.sr1, item.sr2, true, item.sr1_ys, item.sr1_bz) + ' </div>');
            } else if (index != 4 && index != 5 && index != 6) {
                htmls.push(' <div>实际 : ' + item['sj_lc_time'] + '-' + formatNumsNew(item.gz1, item.gz2, true, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, item.rze2, true, item.rze1_ys, item.rze1_bz) + '/'
                    + formatNumsNew(item.sr1, item.sr2, true, item.sr1_ys, item.sr1_bz) + ' </div>');
            }
            htmls.push('</div></div></div>');
            if (hz.indexOf(item.hz) == -1) {
                hz.push(parseInt(item.hz));
            };
        });
        htmls.push('</div></div></div>');
        //设置x轴坐标
        if (needLine && hz != null && hz.length > 0) {

            if ($('.switch_2').text() == '估值') {
                hz.sort(function (a, b) {
                    if (a < b) {
                        return -1;
                    }
                    if (a > b) {
                        return 1;
                    }
                    // a = b
                    return 0;
                });
                //console.log(hz);
                var max = parseInt(hz[hz.length - 1]);
                max = (max / 10) * 10;
                var min = parseInt(hz[0]);
                min = (min / 10) * 10;
                if (max == min) {
                    min = 0;
                };

                var c = parseInt((max - min) / 5);
                // console.log(max+","+min+","+c)
                //从小到大
                // console.log(index)
                for (x = 0; x < 5; x++) {
                    $(".numsvalue_" + (index + 1) + "_" + (5 - x)).html(parseInt(((max - x * c) / 10000)));
                }
            } else {
                hz.sort((num1, num2) => {
                    return num1 - num2 < 0
                });
                //console.log(hz);
                var max = hz[0];
                var min = hz[hz.length - 1];
                if (max == min) {
                    min = 0;
                };
                var c = Math.floor((max - min) / 5);
                //融速  要求写死  25 -0
                // var max = 25;
                // var c = 5;
                // console.log(index)
                for (x = 0; x < 5; x++) {
                    $(".numsvalue_" + (index + 1) + "_" + (x + 1)).html((max - x * c));
                }
            }
        };

    });
    // 判断纵轴指标定义点的位置
    function xJudge(item) {
        var left = -1;
        var added = 20;
        var xwxj = item['xwxj'];
        if (xwxj == null || xwxj == undefined) {
            return;
        }
        if ((parseInt(xwxj) - 1) * added + left < 0) {
            return 0;
        }
        return (parseInt(xwxj) - 1) * added + left;

    }
    function xJudge2(item) {
        /*var xclass;
        var hzmax = hz.sort()[hz.length-1];
        var pre = item['hz']/hzmax;
        if(pre>.4){
            pre = .4;
        }
        pre = pre/2;
        xclass = toPercent(pre);
        // console.log(xclass);
        // xclass = isNaN(xclass)?'':xclass;
        return xclass;*/
        return "2%;"
    }
    // 判断增幅趋势图表
    function rise(item) {
        return "";
        var zf = '';
        switch (item['xjzf']) {
            case '1': zf = 'sheng'; break;
            case '0': zf = 'ping'; break;
            case null: zf = 'ping'; break;
            case '-1': zf = 'jiang'; break;
            default: zf = '';
        }
        return zf;
    }


    // 根据融资额变更点的大小
    function scale(item) {
        var able = (item['rze1'] / 5000) * .5;
        if (able < .5) {
            able = .7;
        } else if (able > 1.5) {
            able = 1.5;
        }
        return able;
    }
    return htmls;
};

/**
 * 创建下方小块HTML内容
 */
function createBottomRzlcHtmlBlock(data) {
    // console.log(data);
    //step1:将返回的5个集合的每一个集合再次进行拆分
    var map = diviseBottomRzlcMap(data);
    map.each(function (key, value, index) {

        value.each(function (innerKey, innerValue, innerIndex) {
            var htmls = '';


            var rate = "0/0";
            if (innerValue != null && innerValue.length > 0) {
                rate = innerValue[0].rate;
                if (rate == "null" || rate == null) {
                    rate = "0/0";
                }
            }
            htmls += '<div style="display:none;" class="block_tit">' + rate + '</div><div class="block_scroll">';
            if (innerValue.length <= 1) {
                htmls += '<div class="block_scroll_screen_off">';
            } else {
                htmls += '<div class="block_scroll_screen">';
            }
            //console.log(key,innerKey)
            $.each(innerValue, function (index2, item) {
                var h = '';
                h += '<div class="roll flex_center"><div class="pointer" >';
                h += '<div><div class="p_on_tit">';
                h += item.xw_name + '(' + item.fr + ')';
                var zf = '';
                /*  switch (item['xjzf']) {
                      case '1': zf = 'sheng'; break;
                      case '0': zf = 'ping'; break;
                      case null: zf = 'ping'; break;
                      case '-1': zf = 'jiang'; break;
                      default: zf = '';
                  }*/
                h += '<span class="' + zf + '"></span>';
                h += '</div>';
                if (index == 0 || index == 2 || index == 3) {
                    h += '<div>目标 : ' + formatNumsNew(item.gz1, null, false, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, null, false, item.rze1_ys, item.rze1_bz) + '/'
                        + formatNumsNew(item.sr1, null, false, item.sr1_ys, item.sr1_bz) + ' ' +
                        '</div><div>承接 : ' + formatNumsNew(item.gz2, item.gz1, true, item.gz2_ys, item.gz2_bz) + '/' + formatNumsNew(item.rze2, item.rze1, true, item.rze2_ys, item.rze2_bz) + '/'
                        + formatNumsNew(item.sr2, item.sr1, true, item.sr2_ys, item.sr2_bz) + '</div>';
                    h += '</div></div></div>';
                } else if (index == 7) {
                    h += '<div>承接 : ' + formatNumsNew(item.gz1, item.gz1, true, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, item.rze1, true, item.rze1_ys, item.rze1_bz) + '/'
                        + formatNumsNew(item.sr1, item.sr1, true, item.sr1_ys, item.sr1_bz) + '</div>';
                    h += '</div></div></div>';
                } else if (index == 1) {
                    h += '<div>实际 : ' + formatNumsNew(item.gz1, item.gz1, true, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, item.rze1, true, item.rze1_ys, item.rze1_bz) + '/'
                        + formatNumsNew(item.sr1, item.sr1, true, item.sr1_ys, item.sr1_bz) + '</div>';
                    h += '</div></div></div>';
                } else if (index != 5 && index != 4) {
                    h += '<div>目标 : ' + formatNumsNew(item.gz1, null, false, item.gz1_ys, item.gz1_bz) + '/' + formatNumsNew(item.rze1, null, false, item.rze1_ys, item.rze1_bz) + '/'
                        + formatNumsNew(item.sr1, null, false, item.sr1_ys, item.sr1_bz) + '</div>';
                    h += '</div></div></div>';
                }

                htmls += h;
            });
            htmls += '</div></div></div>';
            $(".center_lunci" + (index + 1) + "_" + (innerIndex + 1)).html(htmls);
            // 将本月数据按需显示
            // 本月目标
            checkMonthData('.now_goal_down', '目标');
            // 本月承接
            checkMonthData('.now_get_down', '承接');
            // 本月实际
            checkMonthData('.now_fact_down', '实际');
        });

        //var htmls=createTopOneBlock(value,index,false);
        //console.log(htmls)
        //$(".bottom_lunci1").html(htmls)
        //////////
        // $(".lunci"+(index+1)).html(htmls.join(''));
    });
    scroll();
};

function formatNums(orgNum, compareNum, needCompare) {
    var result = "";
    var isRed = false;
    if (orgNum == null) {
        orgNum = 0;
    };
    if (compareNum == null) {
        compareNum = 0;
    };

    if (parseFloat(orgNum) < parseFloat(compareNum)) {
        isRed = true;
    }
    if (Math.abs(orgNum) < 10000) {
        orgNum = mFormartNum(parseFloat(orgNum).toFixed(1)) + "万";
    } else {
        orgNum = mFormartNum((parseFloat(orgNum) / 10000).toFixed(1)) + "亿";
    };
    if (isRed && needCompare) {
        return "<span style='color:red'>" + orgNum + "</span>";
    } else {
        return orgNum;
    }
}

function formatNumsNew(orgNum, compareNum, needCompare, showNum, danwei) {
    var bz = "¥";
    if (danwei == "d") {
        bz = "$";
        //console.log(showNum,bz)
    }

    var result = "";
    var isRed = false;
    if (orgNum == null || orgNum == undefined) {
        orgNum = 0;
    };
    if (showNum == null || showNum == undefined) {
        showNum = 0;
    }
    if (compareNum == null) {
        compareNum = 0;
    };

    if (parseFloat(orgNum) < parseFloat(compareNum)) {
        isRed = true;
    }
    if (Math.abs(showNum) < 10000) {
        showNum = mFormartNum(parseFloat(showNum).toFixed(1)) + "万" + bz;
    } else {
        showNum = mFormartNum((parseFloat(showNum) / 10000).toFixed(1)) + "亿" + bz;
    };
    if (isRed && needCompare) {
        return "<span style='color:red'>" + showNum + "</span>";
    } else {
        return showNum;
    }
}
function mFormartNum(input) {
    if (input.indexOf(".0") != -1) {
        return input.substr(0, input.length - 2);
    } else {
        return input;
    }
}
/**
 * 根据小微编码查询弹出表格数据
 * @param xwCode
 */
function getXiaoWeiTableData(xwCode, dataIndex) {
    var time = formatDate($("#dateIpt").val());
    time = time.replaceAll("-", "");
    var params = "";
    params = joinParams(params, "time", time);
    params = joinParams(params, "xwCode", xwCode);
    // console.log(params)
    if (dataIndex == 0) {
        //本年弹出
        getDateByCommonInterfaceByParam("690_fhxw_yj_1201", params, createXiaoWeiAlertTable, createXiaoWeiAlertTable, dataIndex);
    } else if (dataIndex == 1) {
        //本月
        // getDateByCommonInterfaceByParam("690_fhxw_yj_002", params, createXiaoWeiAlertTable, createXiaoWeiAlertTable, dataIndex);
    } else if (dataIndex == 2) {
        //t1
        getDateByCommonInterfaceByParam("690_fhxw_yj_0021", params, createXiaoWeiAlertTable, createXiaoWeiAlertTable, dataIndex);
    } else if (dataIndex == 3) {
        //t2
        getDateByCommonInterfaceByParam("690_fhxw_yj_0022", params, createXiaoWeiAlertTable, createXiaoWeiAlertTable, dataIndex);
    } else if (dataIndex == 4) {
        //q1
        getDateByCommonInterfaceByParam("690_fhxw_yj_0023", params, createXiaoWeiAlertTable, createXiaoWeiAlertTable, dataIndex);
    } else if (dataIndex == 5) {
        //q2
        getDateByCommonInterfaceByParam("690_fhxw_yj_0024", params, createXiaoWeiAlertTable, createXiaoWeiAlertTable, dataIndex);
    }
};
function dealNum(input) {
    if (input == undefined || input == "0" || input == null) {
        return "/";
    }
    if (input.indexOf(".") == 0) {
        input = "0" + input;
    }
    if (input.indexOf("-") == 0 && input.indexOf(".") == 1) {
        input = "-0." + input.substr(2, input.length);
    }
    if (Math.abs(input) < 10000) {
        input = mFormartNum(parseFloat(input).toFixed(1)) + "万";
    } else {
        input = mFormartNum((parseFloat(input) / 10000).toFixed(1)) + "亿";
    };
    return input;
}
function dealNumNew(input, danwei, product) {
    var bz = "¥";
    if (danwei == "d") {
        bz = "$";
        // console.log(input,bz)
    }

    if (input == undefined || input == "0" || input == null) {
        return "/";
    }
    if (input.indexOf(".") == 0) {
        input = "0" + input;
    }
    if (input.indexOf("-") == 0 && input.indexOf(".") == 1) {
        input = "-0." + input.substr(2, input.length);
    }
    if (Math.abs(input) < 10000) {
        input = mFormartNum(parseFloat(input).toFixed(1)) + "万";
    } else {
        input = mFormartNum((parseFloat(input) / 10000).toFixed(1)) + "亿";
    };
    if (product == "差") {
        return input + "¥";
    }
    return input + bz;
}
function dealString(input) {
    if (input == undefined || input == null) {
        input = "";
    }
    return input;
}
function dealChaColor(product, value) {
    if (product == '差') {
        if (parseFloat(value) < 0) {
            return "color:red";
        }
    }
    return "";
}
/**
 * 创建小微弹出框数据
 */
var types = [];
function createXiaoWeiAlertTable(data, dataIndex) {
    // console.log(data);
    var htmls = "";
    if (dataIndex == 0) {
        var dataArr = data['690_fhxw_1201'];
        // console.log(dataArr);
        htmls += '<div class="alertbox_row"><span>项目</span><span>轮次</span><span>时间(月)</span><span>估值</span><span>融资额</span><span>生态收入</span></div>';
        $.each(dataArr, function (index, item) {
            htmls += '<div class="alertbox_row">';
            htmls += '<span>' + dealString(item.project) + '</span>';
            htmls += '<span>' + dealString(item.lc) + '</span>';
            htmls += '<span>' + (dealString(item.year) == '0' ? '' : dealString(item.year)) + '</span>';
            if (item.project != '差') {
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_gz)) + '">' + dealNumNew(item.mb_gz_ys, item.mb_gz_bz, dealString(item.project)) + '</span>';
                htmls += '<span  style="' + dealChaColor(dealString(item.project), dealNum(item.mb_rze)) + '">' + dealNumNew(item.mb_rze_ys, item.mb_rze_bz, dealString(item.project)) + '</span>';
                htmls += '<span  style="' + dealChaColor(dealString(item.project), dealNum(item.mb_sr)) + '">' + dealNumNew(item.mb_sr_ys, item.mb_sr_bz, dealString(item.project)) + '</span>';
            } else {
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_gz)) + '">' + dealNumNew(item.mb_gz, item.mb_gz_bz, dealString(item.project)) + '</span>';
                htmls += '<span  style="' + dealChaColor(dealString(item.project), dealNum(item.mb_rze)) + '">' + dealNumNew(item.mb_rze, item.mb_rze_bz, dealString(item.project)) + '</span>';
                htmls += '<span  style="' + dealChaColor(dealString(item.project), dealNum(item.mb_sr)) + '">' + dealNumNew(item.mb_sr, item.mb_sr_bz, dealString(item.project)) + '</span>';
            }
            htmls += '</div>';
        });
    } else if (dataIndex == 1 || dataIndex == 2 || dataIndex == 3 || dataIndex == 4 || dataIndex == 5) {
        var dataArr;
        if (dataIndex == 1) {
            return;
            dataArr = data['690_fhxw_002'];
            var dataArr2 = data['690_fhxw_003'];
            dataArr = dataArr.concat(dataArr2);
        } else if (dataIndex == 2) {
            dataArr = data['690_fhxw_004'];
        } else if (dataIndex == 3) {
            dataArr = data['690_fhxw_005'];

        } else if (dataIndex == 4) {
            dataArr = data['690_fhxw_006'];
        } else if (dataIndex == 5) {
            dataArr = data['690_fhxw_007'];
            // console.log(dataArr)
        }

        htmls += '<div class="alertbox_row"><span>项目</span><span>轮次</span><span>时间</span><span>估值</span><span>融资额</span><span>生态收入</span><span>月度环比增速</span></div>';
        $.each(dataArr, function (index, item) {
            htmls += '<div class="alertbox_row">';
            htmls += '<span>' + dealString(item.project) + '</span>';
            htmls += '<span>' + dealString(item.lc) + '</span>';
            htmls += '<span>' + dealString(item.year_mon) + '</span>';
            if (item.project != '差') {
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_gz)) + '">' + dealNumNew(item.mb_gz_ys, item.mb_gz_bz, dealString(item.project)) + '</span>';
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_rze)) + '">' + dealNumNew(item.mb_rze_ys, item.mb_rze_bz, dealString(item.project)) + '</span>';
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_sr)) + '">' + dealNumNew(item.mb_sr_ys, item.mb_sr_bz, dealString(item.project)) + '</span>';
            } else {
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_gz)) + '">' + dealNumNew(item.mb_gz, item.mb_gz_bz, dealString(item.project)) + '</span>';
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_rze)) + '">' + dealNumNew(item.mb_rze, item.mb_rze_bz, dealString(item.project)) + '</span>';
                htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_sr)) + '">' + dealNumNew(item.mb_sr, item.mb_sr_bz, dealString(item.project)) + '</span>';
            }
            htmls += '<span style="' + dealChaColor(dealString(item.project), dealNum(item.mb_hb)) + '">' + (item.mb_hb ? Number(item.mb_hb).toFixed(2) : '/') + '</span>';
            htmls += '</div>';
        });
    }


    $(".alertbox").html(htmls);
    // toggleXiaoWeiAlertTable();

};
function thisMouth(data) {
    var types = [];
    var topData = data['690_fhxw_002'];
    var bottomData = data['690_fhxw_003'];
    for (let i = 0; i < topData.length; i++) {
        types.push(topData[i].project);
    }
    for (let i = 0; i < bottomData.length; i++) {
        types.push(bottomData[i].project);
    }
    var htmls = "";
    htmls += '<div class="alertbox_row"><span>项目</span><span>轮次</span><span>时间</span><span>估值</span><span>融资额</span><span>生态收入</span><span>月度环比增速</span></div>';
    for (var x = 0; x < types.length - 1; x++) {
        htmls += '<div class="alertbox_row">';
        htmls += '<span>' + types[x] + '</span>';
        htmls += '<span>' + topData[x].lc + '</span>';
        htmls += '<span>' + topData[x].year_mon + '</span>';
        htmls += '<span>' + topData[x].mb_gz + '</span>';
        htmls += '<span>' + topData[x].mb_rze + '</span>';
        htmls += '<span>' + topData[x].mb_sr + '</span>';
        htmls += '<span>' + topData[x].mb_hb + '</span>';
        htmls += '</div>';
    };
    htmls += '<div class="alertbox_row">';
    htmls += '<span>' + types[3] + '</span>';
    htmls += '<span>-</span>';
    htmls += '<span>' + bottomData[0].DIFF_MON + '</span>';
    htmls += '<span>' + bottomData[0].DIFF_GZ + '</span>';
    htmls += '<span>' + bottomData[0].DIFF_RZE + '</span>';
    htmls += '<span>' + bottomData[0].DIFF_SR + '</span>';
    htmls += '<span>' + bottomData[0].DIFF_HB + '</span>';
    htmls += '</div>';
    $(".alertbox").html(htmls);
}


/**
 * 绑定融资轮次数据的悬浮事件
 */
function hoverFunForRzlcBlock() {

    $('.showPointer .pointer').hover(function () {
        x = this.getBoundingClientRect().left;
        y = this.getBoundingClientRect().top;
        var xwCode = $(this).attr("xw_code");
        var dataIndex = $(this).attr("data-index");
        $('.alertbox').data('id', $(this).data('id'));
        //获取当前小微具体数据
        getXiaoWeiTableData(xwCode, dataIndex);
        var dvicx = document.body.clientWidth;
        var dvicy = document.body.clientHeight;
        var minx = dvicx - $('.alertbox').width();
        var miny = dvicy - $('.alertbox').height();
        if (x > minx) {
            x = x - $('.alertbox').width();
        }
        if (y > (miny - 60)) {
            y = y - $('.alertbox').height();
        }
        if (dataIndex) {
            $('.alertbox').css({ 'left': x + 20, 'top': y + 20, 'visibility': 'visible' });
            $('.alertbox').hover(function () {
                $('.alertbox').css('visibility', 'visible');
            }, function () {
                $('.alertbox').css('visibility', 'hidden');
            });
        }
    }, function () {
        $('.alertbox').css('visibility', 'hidden');
    });
};
/**
 * 显示/隐藏小微弹出表格数据
 * @param x
 * @param y
 */
function toggleXiaoWeiAlertTable() {
    var dvicx = document.body.clientWidth;
    var dvicy = document.body.clientHeight;
    var minx = dvicx - $('.alertbox').width();
    var miny = dvicy - $('.alertbox').height();
    if (x > minx) {
        x = x - $('.alertbox').width();
    }
    if (y > (miny - 60)) {
        y = y - $('.alertbox').height();
    }
    $('.alertbox').css({ 'left': x + 40, 'top': y + 40, 'visibility': 'visible' });
    $('.alertbox').hover(function () {
        $('.alertbox').css('visibility', 'visible');
    }, function () {
        $('.alertbox').css('visibility', 'hidden');
    });
}
/**
 * 清空评论
 */
function resetCommentValue() {
    $("div [class^='jielun']").each(function (index, item) {
        $(item).val('');
    });
};
function editDom() {
    $("div [class^='jielun']").each(function (index, item) {
        $(item).removeAttr('disabled');
    });
};
function disEditDom() {
    $("div [class^='jielun']").each(function (index, item) {
        $(item).attr('disabled', "disabled");
    });
};
/**
 * @param params
 * 拼接参数
 */
function joinParams(params, key, value) {
    if (!params) {
        params += key + "::" + value;
    } else {
        params += ";;" + key + "::" + value;
    };
    return params;
};
function getGuzhiRongsu() {
    guZhiRongsu = $(".switch_2").text();
}
/**
 * 初始化变量值
 */
function initVarParams() {
    // changeIndustry();
    dateChange();
    getGuzhiRongsu();
};
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return "";
}
/**
 * 获取产业下拉
 * @param data
 */
function setIndustryInfo(data) {
    if (data.dp.length == 5) {
        var htmls = '<option value="ALL">智家定制生态圈</option>';
    } else {
        var htmls = '';
    }
    $.each(data.dp, function (index, item) {
        if (backIndustryCode == item.industry_code) {
            htmls += '<option selected value="' + item.industry_code + '">' + item.industry_name + '</option>'
        } else {
            htmls += '<option value="' + item.industry_code + '">' + item.industry_name + '</option>'
        }

    });
    $("#industryIpt").html(htmls);
    $("#industryIpt").on("change", changeIndustry);
    getXwInfo();
    searchFunction();
};
/**
 * 切换产业
 */
function changeIndustry() {
    backXW_CODE = 'ALL';
    industryCode = $('#industryIpt option:selected').val();
    industryName = $('#industryIpt option:selected').text();
    getXwInfo();
    // console.log("industryCode="+industryCode,"industryName="+industryName);
};
/**
 * 获取小微用户下拉
 */
function getXwInfo() {
    var inCode = $("#industryIpt").val();
    var params = "";
    params = joinParams(params, "inCode", inCode);
    getDateByCommonInterface("690_fhxw_xw", params, setXwInfo);
}
/**
 * 设置小微用户下拉
 */
function setXwInfo(data) {
    var htmls = '<option value="ALL">全部小微</option>';
    $.each(data['690_fhxw_xw'], function (index, item) {
        if (backXW_CODE == item.XW_CODE) {
            htmls += '<option selected value="' + item.XW_CODE + '">' + item.XW_NAME + '</option>'
        } else {
            htmls += '<option value="' + item.XW_CODE + '">' + item.XW_NAME + '</option>'
        }

    });
    $("#select_xwcode").html(htmls);
    $("#select_xwcode").on("change", function () {
        backXW_CODE = $("#select_xwcode").val();
    });
}
/**
 * 时间切换
 * @param obj
 */
function dateChange() {
    vdate = formatDate($("#dateIpt").val());
    vdate = vdate.substr(0, 7);
    next1Month = getNextMonth(vdate);
    next2Month = getNextMonth(next1Month);
    getJiduDate();
    setTitle();
    //console.log("vdate="+vdate,"next1Month="+next1Month,"next2Month="+next2Month);
};
/**
 * 设置标题
 */
function setTitle() {
    $(".title1").html(vdate.substr(5, 2) + "月预实差");
    $(".title2").html(next1Month + "承接");
    $(".title3").html(next2Month + "承接");
    $(".title4").html(next1Jidu + "目标");
    $(".title5").html(next2Jidu + "目标");
}
/**
 * 获取下一个 下两个季度
 */
function getJiduDate() {
    vdate = formatDate($("#dateIpt").val());
    var year = vdate.substr(0, 4);
    var month = vdate.substr(5, 2);
    var n = parseInt(parseInt(month) / 3);//当前月属于第几季度
    var yu = parseInt(month) % 3;
    if (yu > 0) {
        n++;
    };
    if (month == 3 || month == 6 || month == 9 || month == 12) {
        var nestJiduValue = n + 1;
    } else {
        var nestJiduValue = n;
    }
    //下一个季度
    if (nestJiduValue == 5) {
        next1Jidu = (parseInt(year) + 1) + "年Q1";//下一个季度时间
        next2Jidu = (parseInt(year) + 1) + "年Q2";//下两个季度时间
    } else {
        next1Jidu = year + "年Q" + nestJiduValue;//下一个季度时间
        //下两个季度
        var nest2JiduValue = nestJiduValue + 1;
        if (nest2JiduValue == 5) {
            next2Jidu = (parseInt(year) + 1) + "年Q1";
        } else {
            next2Jidu = year + "年Q" + nest2JiduValue;
        }
    };
    //console.log("next1Jidu="+next1Jidu,"nest2Jidu="+next2Jidu)

}
/**
 * 获取当前时间戳
 */
function getCurrentTimestamp() {
    return parseInt(new Date().getTime() / 1000);
};
//格式化日期
function formatDate(val) {
    var result = "";
    if (val != null && val != '') {
        var date = new Date(val);
        var m = date.getMonth() + 1;
        var d = date.getDate();
        result = date.getFullYear() + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
    };
    return result;
};
function getCurrentDate() {
    var d = new Date();
    var hour = d.getHours();
    var minute = d.getMinutes();
    var second = d.getSeconds();
    var str = d.getFullYear() + "-" + ((d.getMonth() + 1) >= 10 ? +(d.getMonth() + 1) : "0" + (d.getMonth() + 1)) + "-" + ((d.getDate()) >= 10 ? d.getDate() : '0' + d.getDate());
    str += " " + hour + ":" + minute + ":" + second;
    return str;
}
//获得下一月份
function getNextMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中的月的天数
    var year2 = year;
    var month2 = parseInt(month) + 1;
    if (month2 == 13) {
        year2 = parseInt(year2) + 1;
        month2 = 1;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0' + month2;
    }

    var t2 = year2 + '-' + month2;
    return t2;
};

/**
 * 获取上一个月
 *
 * @date 格式为yyyy-mm-dd的日期，如:2014-01-25
 */
function getPreMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中月的天数
    var year2 = year;
    var month2 = parseInt(month) - 1;
    if (month2 == 0) {//如果是1月份，则取上一年的12月份
        year2 = parseInt(year2) - 1;
        month2 = 12;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {//如果原来日期大于上一月的日期，则取当月的最大日期。比如3月的30日，在2月中没有30
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0' + month2;//月份填补成2位。
    }
    var t2 = year2 + '-' + month2 + '-' + day2;
    return t2;
};
Array.prototype.remove = function (s) {
    for (var i = 0; i < this.length; i++) {
        if (s == this[i])
            this.splice(i, 1);
    }
}

// 判断星级定义点的颜色
function starColor(item) {
    var starlv = '';
    switch (item['zz']) {
        case '天使轮': starlv = 'one'; break;
        case 'A轮': starlv = 'two'; break;
        case 'B轮': starlv = 'three'; break;
        case 'C轮': starlv = 'four'; break;
        case '独角兽': starlv = 'five'; break;
    }
    return starlv;
}

/**
 * Simple Map
 *
 *
 * var m = new Map();
 * m.put('key','value');
 * ...
 * var s = "";
 * m.each(function(key,value,index){
 *      s += index+":"+ key+"="+value+"/n";
 * });
 * alert(s);
 *
 * @author dewitt
 * @date 2008-05-24
 */
function Map() {
    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();

    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */
    this.put = function (key, value) {
        if (this.data[key] == null) {
            this.keys.push(key);
        }
        this.data[key] = value;
    };

    /**
     * 获取某键对应的值
     * @param {String} key
     * @return {Object} value
     */
    this.get = function (key) {
        return this.data[key];
    };

    /**
     * 删除一个键值对
     * @param {String} key
     */
    this.remove = function (key) {
        this.keys.remove(key);
        this.data[key] = null;
    };

    /**
     * 遍历Map,执行处理函数
     *
     * @param {Function} 回调函数 function(key,value,index){..}
     */
    this.each = function (fn) {
        if (typeof fn != 'function') {
            return;
        }
        var len = this.keys.length;
        for (var i = 0; i < len; i++) {
            var k = this.keys[i];
            fn(k, this.data[k], i);
        }
    };

    /**
     * 获取键值数组(类似Java的entrySet())
     * @return 键值对象{key,value}的数组
     */
    this.entrys = function () {
        var len = this.keys.length;
        var entrys = new Array(len);
        for (var i = 0; i < len; i++) {
            entrys[i] = {
                key: this.keys[i],
                value: this.data[i]
            };
        }
        return entrys;
    };

    /**
     * 判断Map是否为空
     */
    this.isEmpty = function () {
        return this.keys.length == 0;
    };

    /**
     * 获取键值对数量
     */
    this.size = function () {
        return this.keys.length;
    };

    /**
     * 重写toString
     */
    this.toString = function () {
        var s = "{";
        for (var i = 0; i < this.keys.length; i++ , s += ',') {
            var k = this.keys[i];
            s += k + "=" + this.data[k];
        }
        s += "}";
        return s;
    };
};

/**
 * 扩展js替换方法
 * @param f
 * @param e
 * @returns {string}
 */
String.prototype.replaceAll = function (f, e) {//吧f替换成e
    var reg = new RegExp(f, "g"); //创建正则RegExp对象
    return this.replace(reg, e);
};

// 用户小微轮播控制
// 小轮播延迟版
function scroll() {
    var lunboitem = document.querySelectorAll('.block_scroll_screen');
    lunboitem.forEach(lunbo);
}
function lunbo(item, i) {
    var timers;
    function wfscroll() {
        if ($('.alertbox').data('id') == $(item).data('id') && $('.alertbox').css('visibility') == 'visible') {
            $(item).stop();
        } else {
            $(item).stop().animate({ top: '-100%' }, 500, "linear", function () {
                var a = $(item).children().eq(0).clone(true);
                $(item).children().eq(0).remove();
                a.appendTo(item);
                $(item).css('top', '0');
            })
        }
    }
    timers = setInterval(wfscroll, 4000 + i * 1000);
    $(item).parent().hover(function () {
        clearInterval(timers);
    }, function () {
        timers = setInterval(wfscroll, 4000 + i * 1000);
    })
}