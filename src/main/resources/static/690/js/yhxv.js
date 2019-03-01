var params = "";
var inCode = "";
var xwcode = "";
var chanyeTime = ""
var countScroll = 0;
//化成百分数保留一位小数
function toPercent(point) {
    var str = "-"
    if (point != "-"&&point != null) {
        str = Number(point * 100).toFixed(1);
        str += "%";
    }
    return str;

}
//化成百分数，不保留小数位
function toPercent2(point) {
    var str = "-"
    if (point != "-"&&point != null) {
        str = Number(point * 100).toFixed(0);
        str += "%";
    }
    return str;

}
//将数据保留一位小数
function toPercent3(point) {
    var str = "-"
    if (point != "-"&&point != null) {
        str = Number(point).toFixed(1);
    }
    return str;
}
// init datepaker
$('#chanyeTime').date_input();
//小微下拉点击事件
function XWchange() {
    var inCode = $("#select_indust option:selected").val();
    getDateByCommonInterface('690_yhxw_yj_017', "inCode::" + inCode, setChangeXW);
}
//获取小微下拉框---主动选择
function setChangeXW(data) {
    var selectdata = data['690_yhxw_yj_017'];
    $("#select_xwcode").children().remove();
    $("#select_xwcode").append("<option value='ALL' selected='selected'>全部小微</option>")
    $.each(data["690_yhxw_yj_017"], function (index, item) {
        $("#select_xwcode").append("<option value=" + item.YHXW_CODE + " >" + item.YHXW_NAME + "</option>")
    })
}
//获取小微下拉框
function setXWdata(data) {
    var selectdata = data['690_yhxw_yj_017'];
    $("#select_xwcode").children().remove();
    $("#select_xwcode").append("<option value='ALL' selected='selected'>全部小微</option>")
    $.each(data["690_yhxw_yj_017"], function (index, item) {
        $("#select_xwcode").append("<option value=" + item.YHXW_CODE + " >" + item.YHXW_NAME + "</option>")
    })
    if (localStorage.getItem('yhxwData')) {
        $("#select_xwcode").val(JSON.parse(localStorage.getItem('yhxwData')).selectXwCode);
    }
}
$(function () {
    let yhxwData = localStorage.getItem('yhxwData') ? JSON.parse(localStorage.getItem('yhxwData')) : '';
    if (!yhxwData.fromClick) {
        localStorage.setItem('yhxwData', '')
    } else {
        yhxwData.fromClick = '';
        localStorage.setItem('yhxwData', JSON.stringify(yhxwData))
    }
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    time = theRequest['time'];
    xw_code = yhxwData.selectXwCode;
    inCode = theRequest['inCode'];

    //690一级页面-产业下拉框
    getDateByCommonInterface("690_yhxw_yj_016", "UCode::" + userInfo.userID, setIndustryData);
    //获取url将小微主小微名称时间写入页面
    function GetRequest() {
        if (time == "" || time == null) {
            //设置默认日期
            if (new Date().getDate() > 4) {
                $("#chanyeTime").val(formatDate(new Date() - 1000 * 60 * 60 * 24));
            } else {
                if(new Date().getMonth()==0){
                    $("#chanyeTime").val(getLastDay(new Date().getFullYear()-1,12));
                }
                $("#chanyeTime").val(getLastDay(new Date().getFullYear(),new Date().getMonth()));
            }
            var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
            year = chanyeTime.substring(0, 4);
            month = chanyeTime.substring(4, 6);
            day = chanyeTime.substring(6, 8);
            params = "inCode::" + SelectInCode + ";;time::" + chanyeTime + ";;xw_code::ALL"
        } else {
            inCode = theRequest['inCode'];
            year = time.substring(0, 4);
            month = time.substring(4, 6);
            day = time.substring(6, 8);
            if (inCode && time && xw_code) {
                params = "inCode::" + inCode + ";;time::" + time + ";;xw_code::" + xw_code;
            } else {
                params = "inCode::" + SelectInCode + ";;time::" + time + ";;xw_code::ALL";
            }
            $("#chanyeTime").val(time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8));
        }
        if (month <= 9) {
            $("#yearMB").html(year - 0 + "年目标");
        } else {
            $("#yearMB").html(year - 0 + 1 + "年目标");
        }
        $("#thisMonth").html(Number(month) + "月累计预实差");
        $("#thisMonth").val(month);

        chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        year1 = chanyeTime.substring(0, 4)

        month1 = chanyeTime.substr(4, 2)

        //下两个季度
        Q = getJiduDate(year1, month1);
        $("#NextJidu1").html(Q[0] + "季度");
        $("#NextJidu2").html(Q[1] + "季度");
        //下两个月
        NextMonth1 = getNextMonth(year1 + "-" + month1 + "-" + day);
        $("#NextMonth1").html(NextMonth1.substring(0, 4) + "年" + Number(NextMonth1.substring(5, 8)) + "月")
        NextMonth2 = getNextMonth(NextMonth1);
        $("#NextMonth2").html(NextMonth2.substring(0, 4) + "年" + Number(NextMonth2.substring(5, 8)) + "月")
        //判断本地存储
        if (localStorage.getItem('yhxwData')) {
            var displayType = yhxwData.left_arrow;
            $('.left_arrow').css('display', displayType);
            if (displayType === 'none') {
                $('.right_arrow').css('display', 'block');
            }
        } else {
            $('.left_arrow').css('display', 'block');
        }
        getCommentData(inCode, time);
        ALLfunction(params);
    }

    function ALLfunction(params) {
        //获取所有产业收入增幅和利润率的最大值
        getDateByCommonInterface("690_cdwl+yhxw", "time::" + params.substring(19, 27) + ";;inCode::" + params.substring(8, 12), setMAX);
        //获取产业星级数据
        getDateByCommonInterface("690_yhxw_yj_015", params, levels);
        //获取年目标数据
        getDateByCommonInterface("690_yhxw_yj_001t", params, setYearData);
        //获取当月目标数据
        getDateByCommonInterface("690_yhxw_yj_002t", params, setMonthMBData);
        //获取当月承接数据
        getDateByCommonInterface("690_yhxw_yj_003t", params, setMonthYSxData);
        //获取当月实际数据
        getDateByCommonInterface("690_yhxw_yj_004t", params, setMonthSJData);
        //获取T+1月数据
        getDateByCommonInterface("690_yhxw_yj_005t", params, setMonthT1Data);
        //获取T+2月数据
        getDateByCommonInterface("690_yhxw_yj_006t", params, setMonthT2Data);
        //获取Q+1月数据
        getDateByCommonInterface("690_yhxw_yj_007t", params, setQuarterT1Data);
        //获取Q+2月数据
        getDateByCommonInterface("690_yhxw_yj_008t", params, setQuarterT2Data);
        //获取所有柱状图数据
        getDateByCommonInterface('690_yhxw_l001', params, allBar);

    }

    //获取当前时间
    function getCurrentDate() {
        var inCode = $("#select_indust option:selected").val();
        $("#chanyeTime").val(formatDate(new Date() - 1000 * 60 * 60 * 24));
        var xw_code = $("#select_xwcode option:selected").val();
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        year = chanyeTime.substring(0, 4),
            month = chanyeTime.substring(4, 6),
            inCode = $("#select_indust").val();
        params = "inCode::" + inCode + ";;time::" + chanyeTime + ";;xw_code::" + xw_code
        if (month <= 9) {
            $("#yearMB").html(year - 0 + "年目标");
        } else {
            $("#yearMB").html(year - 0 + 1 + "年目标");
        }
        $("#thisMonth").html(Number(month) + "月累计预实差");
        $("#thisMonth").val(month);
        //下两个季度
        Q = getJiduDate(year, month);
        $("#NextJidu1").html(Q[0] + "季度");
        $("#NextJidu2").html(Q[1] + "季度");
        //下两个月
        NextMonth1 = getNextMonth(year + "-" + month + "-" + day);
        $("#NextMonth1").html(NextMonth1.substr(0, 4) + "年" + Number(NextMonth1.substr(5, 8)) + "月")
        NextMonth2 = getNextMonth(NextMonth1);
        $("#NextMonth2").html(NextMonth2.substr(0, 4) + "年" + Number(NextMonth2.substr(5, 8)) + "月")
        getCommentData(inCode, chanyeTime);
        ALLfunction(params);
    };


    //封装产业下拉框数据
    function setIndustryData(data) {
        var selectdata = data['690_yhxw_016'];
        $.each(data["690_yhxw_016"], function (index, item) {
            if (index == 0) {
                SelectInCode = item.INDUSTRY_CODE;
                $("#select_indust").append("<option value=" + item.INDUSTRY_CODE + " selected='selected'>" + item.INDUSTRY_NAME + "</option>")
            } else {
                $("#select_indust").append("<option value=" + item.INDUSTRY_CODE + " >" + item.INDUSTRY_NAME + "</option>")
            }
        })
        if (inCode) {
            getDateByCommonInterface('690_yhxw_yj_017', "inCode::" + inCode, setXWdata);
            $('#select_indust').val(inCode)
        } else {
            getDateByCommonInterface('690_yhxw_yj_017', "inCode::" + SelectInCode, setXWdata);
            $('#select_indust').val(SelectInCode)
        }
        GetRequest();
        //  else {
        //     getDateByCommonInterface('690_yhxw_yj_017', "inCode::BAA", setXWdata);
        //     $('#select_indust').val('BAA')
        // }
    }

    //获取下一个季度和下两个季度
    function getJiduDate(year, month) {
        var n = parseInt(parseInt(month) / 3);//当前月属于第几季度
        var yu = parseInt(month) % 3;
        if (yu > 0) {
            n++;
        };
        if(month ==3 ||month ==6 ||month ==9 ||month ==12 ){
            var nestJiduValue = n + 1;
        }else{
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
    //将产业星级数据写入页面
    function levels(data) {
        $('.s_lin1_name').text("");
        $('.s_lin1_one').html("");
        $('.s_lin1_two').html("");
        $('.s_lin1_three').html("");
        var abledata = data['690_yhxw_015'];
        $('.s_lin1_name').text(abledata[0]['INDUSTRY_Z']);
        $('.s_lin1_one').html(levelStar(abledata[0]));
        $('.s_lin1_two').html(levelStar(abledata[2]));
        $('.s_lin1_three').html(levelStar(abledata[1]));
    }
    // 星级评价
    function levelStar(data) {
        var str = '';
        var stars = '';
        if (data) {
            for (let i = 0; i < data['XJ']; i++) {
                stars += '★';
            }
            str = data['PROJECT'] + '&nbsp:<span>' + stars + '</span>';
        }
        return str;
    }
    //查询
    $('#select').click(function () {
        var inCode = $("#select_indust option:selected").val();
        var xw_code = $("#select_xwcode option:selected").val();
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        getCommentData(inCode, chanyeTime);
        month = chanyeTime.substring(4, 6);
        year = chanyeTime.substring(0, 4);
        day = chanyeTime.substring(6, 8);
        if (month <= 9) {
            $("#yearMB").html(year - 0 + "年目标");
        } else {
            $("#yearMB").html(year - 0 + 1 + "年目标");
        }
        $("#thisMonth").html(Number(month) + "月累计预实差");
        $("#thisMonth").val(month);

        //下两个季度
        Q = getJiduDate(year, month);
        $("#NextJidu1").html(Q[0] + "季度");
        $("#NextJidu2").html(Q[1] + "季度");
        //下两个月
        NextMonth1 = getNextMonth(year + "-" + month + "-" + day);
        $("#NextMonth1").html(NextMonth1.substr(0, 4) + "年" + Number(NextMonth1.substr(5, 8)) + "月")
        NextMonth2 = getNextMonth(NextMonth1);
        $("#NextMonth2").html(NextMonth2.substr(0, 4) + "年" + Number(NextMonth2.substr(5, 8)) + "月")
        params = "inCode::" + inCode + ";;time::" + chanyeTime + ";;xw_code::" + xw_code

        //  getDateByCommonInterface("690_yhxw_yj_015", params, levels);
        ALLfunction(params);
        // 轮播图中的柱状图
        ['#ec04_bar', '#ec05_bar', '#ec06_bar', '#ec07_bar'].forEach(function (id) {
            $(id).css({ background: 'none' });
            setTimeout(function () {
                paintEchartsImg(id, id);
            }, 1000);
        });
    })
    //点击上一个月
    $('.left_arrow').click(function () {
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        var inCode = $("#select_indust option:selected").val();
        var xw_code = $("#select_xwcode option:selected").val();
        var year = chanyeTime.substring(0, 4)
        var month = chanyeTime.substring(4, 6) - 1
        var day = chanyeTime.substring(6, 8)
        if (month < 10) {
            month = "0" + month
        }

        if (month == "00" || month == "0") {
            month = 12
            year = year - 1
        }
        if (month <= 9) {
            $("#yearMB").html(year - 0 + "年目标");
        } else {
            $("#yearMB").html(year - 0 + 1 + "年目标");
        }
        $("#thisMonth").html(Number(month) + "月累计预实差");
        $("#thisMonth").val(month);
        //下两个季度
        Q = getJiduDate(year, month);
        $("#NextJidu1").html(Q[0] + "季度");
        $("#NextJidu2").html(Q[1] + "季度");
        //下两个月
        NextMonth1 = getNextMonth(year + "-" + month + "-" + day);
        $("#NextMonth1").html(NextMonth1.substr(0, 4) + "年" + Number(NextMonth1.substr(5, 8)) + "月")
        NextMonth2 = getNextMonth(NextMonth1);
        $("#NextMonth2").html(NextMonth2.substr(0, 4) + "年" + Number(NextMonth2.substr(5, 8)) + "月")
        if (month == "04" || month == "06" || month == "09" || month == "11") {
            params = "inCode::" + inCode + ";;time::" + year + "" + month + "30" + ";;xw_code::" + xw_code;
            var innertime = year + "" + month + "30";
            $("#chanyeTime").val(year + "-" + month + "-" + "30");
        } else if (month == "02") {
            params = "inCode::" + inCode + ";;time::" + year + "" + month + "30" + ";;xw_code::" + xw_code;
            var innertime = year + "" + month + "28"
            $("#chanyeTime").val(year + "-" + month + "-" + "28");
        } else {
            params = "inCode::" + inCode + ";;time::" + year + "" + month + "31" + ";;xw_code::" + xw_code;
            var innertime = year + "" + month + "31"
            $("#chanyeTime").val(year + "-" + month + "-" + "31");
        }
        ALLfunction(params);
        $('.left_arrow').css({ 'display': 'none' });
        $('.right_arrow').css({ 'display': 'block' });
        ['#ec04_bar', '#ec05_bar', '#ec06_bar', '#ec07_bar'].forEach(function (id) {
            $(id).css({ background: 'none' });
            setTimeout(function () {
                paintEchartsImg(id, id);
            }, 1000);
        });
        getCommentData(inCode, innertime);
    })
    //点击返回当前月
    $('.right_arrow').click(function () {
        getCurrentDate();
        $('.right_arrow').css({ 'display': 'none' });
        $('.left_arrow').css({ 'display': 'block' });
        ['#ec04_bar', '#ec05_bar', '#ec06_bar', '#ec07_bar'].forEach(function (id) {
            $(id).css({ background: 'none' });
            setTimeout(function () {
                paintEchartsImg(id, id);
            }, 1000);
        });
    })
    //将占比数据相加等于100%
    function forHundred(a) {
        num = 0
        $.each(a, function (index, item) {
            if (index + 1 < a.length) {
                item.biaoqian.innerHTML = (item.sl + '/' + toPercent2(item.zb));
                num += Number(item.zb)
            } else {
                item.biaoqian.innerHTML = (item.sl + '/' + toPercent2(1 - num));
            }

        })
    }
    //年数据
    function setYearData(data) {
        var abledata = data['690_yhxw_001t'];
        arr = grouping(abledata);
        $('.area_1 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_1 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)

        scroll();
    }
    //月目标
    function setMonthMBData(data) {
        var abledata = data['690_yhxw_002t'];
        arr = grouping(abledata);
        $('.area_2 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_2 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)
        scroll2();
    }
    //月承接
    function setMonthYSxData(data) {
        var abledata = data['690_yhxw_003t'];
        arr = grouping(abledata);
        $('.area_3 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];

        $('.area_3 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata.length == 0 || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }


        });
        forHundred(a)
        scroll2();
    }
    //月实际
    function setMonthSJData(data) {
        var abledata = data['690_yhxw_004t'];
        arr = grouping(abledata);
        $('.area_4 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_4 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)
        scroll2();
    }
    //T+1
    function setMonthT1Data(data) {
        var abledata = data['690_yhxw_005t'];
        arr = grouping(abledata);
        $('.area_5 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_5 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)
        scroll();
    }
    //T+2
    function setMonthT2Data(data) {
        var abledata = data['690_yhxw_006t'];
        arr = grouping(abledata);
        $('.area_6 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_6 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)
        scroll();
    }
    //Q+1
    function setQuarterT1Data(data) {
        var abledata = data['690_yhxw_007t'];
        arr = grouping(abledata);
        $('.area_7 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_7 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)
        scroll();
    }
    //Q+2
    function setQuarterT2Data(data) {
        var abledata = data['690_yhxw_008t'];
        arr = grouping(abledata);
        $('.area_8 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $('.area_8 .block_tit').each(function (i, item) {
            var sl = 'SL' + (i + 1);
            var zb = 'ZB' + (i + 1);
            if (abledata.length !== 0) {
                if (abledata[0][sl] == 0 || abledata[0][sl] == "" || abledata[0][sl] == null) {
                    $(item).html("");
                } else {
                    a.push({ "biaoqian": item, "sl": abledata[0][sl], "zb": abledata[0][zb] })
                }
            } else {
                $(item).html("");
            }
        });
        forHundred(a)
        scroll();
    }
    // 通用函数----节点渲染

    // 定义变量
    var str;

    // 执行与判断
    // 1.数据按纵轴指标分组
    function grouping(data) {
        var arr = [[], [], [], [], []];
        data.forEach(function (item, i) {
            switch (item['XJ']) {
                case '5': arr[0].push(item); break;
                case '4': arr[1].push(item); break;
                case '3': arr[2].push(item); break;
                case '2': arr[3].push(item); break;
                case '1': arr[4].push(item); break;
            }
        });
        return arr;
    }
    // 2.生成对应标签
    function creatDiv(arr) {
        str = '';
        if (!arr) {
            return str;
        }
        if (arr.length == 1) {
            str = '<div class="block_scroll_screen_off">';
            arr.forEach(creatStr);
            str += '</div>';
        } else if (arr.length > 1) {
            str = '<div class="block_scroll_screen"  data-id="' + countScroll + '">';
            arr.forEach(creatStr);
            str += '</div>';
            countScroll++;
        }
        return str;
    }
    // 3.拼接字符串
    function creatStr(item, i) {
        for (let i in item) {
            if (item[i] == null || item[i] == undefined) {
                item[i] = "-"
            }
        }
        if (item['YEAR']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_year" data-id="`+ countScroll + `" data-xwcode="` + item.XW_CODE + `"data-xwname="` + item['XW_NAME'] + `"data-xwz="` + item['XWZ'] + `"data-year="` + item['YEAR'] + `">
                        <div class="p_on `+ xJudge(item) + `" >
                            <span class=`+ starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + item['XW_NAME'] + `(` + item['XWZ'] + `)<span class="` + (item['HQ_FLAG'] == '0' && item['XJ'] != '1' && item['XJ'] != '2' ? 'p_on_img' : '') + `"></span>` + `</div>
                            <div>目标 : 横`+ toPercent3(item.MB_SWD) + `/` + toPercent2(item.MB_SRZF) + `/` + toPercent(item.MB_LRL) + `, 纵` + toPercent2(item.MB_BZD) + `/` + toPercent2(item.MB_YSLC) + `/` + toPercent3(item.MB_ZZFX) + `万</div>
                            <div>承接 : 横<span style="color:`+ ((toPercent3(item['YS_SWD']) - toPercent3(item['MB_SWD'])) < 0 ? 'red' : '') + `;">` + toPercent3(item['YS_SWD']) + `</span>/<span style="color:` + ((parseInt(toPercent2(item['YS_SRZF'])) - parseInt(toPercent2(item['MB_SRZF']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['YS_SRZF']) + `</span>/<span style="color:` + ((parseInt(toPercent(item['YS_LRL'])) - parseInt(toPercent(item['MB_LRL']))) < 0 ? 'red' : '') + `;">` + toPercent(item['YS_LRL']) + `</span>, 纵<span style="color:` + ((parseInt(toPercent2(item['YS_BZD'])) - parseInt(toPercent2(item['MB_BZD']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['YS_BZD']) + `</span>/<span style="color:` + ((parseInt(toPercent2(item['YS_YSLC'])) - parseInt(toPercent2(item['MB_YSLC']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['YS_YSLC']) + `</span>/<span style="color:` + ((toPercent3(item['YS_ZZFX']) - toPercent3(item['MB_ZZFX'])) < 0 ? 'red' : '') + `;">` + toPercent3(item['YS_ZZFX']) + `万</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        } else {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_month" data-id="`+ countScroll + `" data-xwcode="` + item.XW_CODE + `"data-xwname="` + item['XW_NAME'] + `"data-xwz="` + item['XWZ'] + `">
                        <div class="p_on `+ xJudge(item) + `" >
                            <span class=`+ starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + item['XW_NAME'] + `(` + item['XWZ'] + `)<span class="` + (item['HQ_FLAG'] == '0' && item['XJ'] != '1' && item['XJ'] != '2' ? 'p_on_img' : '') + `"></span><span class="` + rise(item) + `"></span></div>
                            <div>横 : <span style="color:`+ ((toPercent3(item['SWD']) - toPercent3(item['MB_SWD'])) < 0 ? 'red' : '') + `;">` + toPercent3(item['SWD']) + `</span>/<span style="color:` + ((parseInt(toPercent2(item['SRZF'])) - parseInt(toPercent2(item['MB_SRZF']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['SRZF']) + `</span>/<span style="color:` + ((parseInt(toPercent(item['LRL'])) - parseInt(toPercent(item['MB_LRL']))) < 0 ? 'red' : '') + `;">` + toPercent(item['LRL']) + `</span> <br>纵 : <span style="color:` + ((parseInt(toPercent2(item['BZD'])) - parseInt(toPercent2(item['MB_BZD']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['BZD']) + `</span>/<span style="color:` + ((parseInt(toPercent2(item['YSLC'])) - parseInt(toPercent2(item['MB_YSLC']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['YSLC']) + `</span>/<span style="color:` + ((toPercent3(item['ZZFX']) - toPercent3(item['MB_ZZFX'])) < 0 ? 'red' : '') + `;">` + toPercent3(item['ZZFX']) + `万</span></div>
                        </div>
                    </div>
                </div>`;
        }
    }

    // 4.判断横轴指标定义点的位置
    function xJudge(item) {
        var xclass;
        switch (item['XZZB'] || item['YS_XZZB']) {
            case '高于行业': xclass = ''; break;
            case '预实零差': xclass = 'p_on_2'; break;
            case '套圈': xclass = 'p_on_3'; break;
            case '套圈引领': xclass = 'p_on_4'; break;
            case '换道引领': xclass = 'p_on_5'; break;
        }
        switch (item['MB_XZZB']) {
            case '高于行业': xclass = ''; break;
            case '预实零差': xclass = 'p_on_2'; break;
            case '套圈': xclass = 'p_on_3'; break;
            case '套圈引领': xclass = 'p_on_4'; break;
            case '换道引领': xclass = 'p_on_5'; break;
        }
        return xclass;
    }
    // 5.判断星级定义点的颜色
    function starColor(item) {
        var starlv;
        switch (item['XJ']) {
            case '1': starlv = 'one'; break;
            case '2': starlv = 'two'; break;
            case '3': starlv = 'three'; break;
            case '4': starlv = 'four'; break;
            case '5': starlv = 'five'; break;
        }
        return starlv;
    }
    // 6.判断增幅趋势图表
    function rise(item) {
        var zf = '';
        switch (item['XJ_ZF']) {
            case '1': zf = 'sheng'; break;
            case '0': zf = 'ping'; break;
            case '-1': zf = 'jiang'; break;
            default: zf = '';
        }
        return zf;
    }

    //年弹窗
    $('body').on('mouseover', '.column_1 .pointer_year', function () {
        var XW_CODE = $(this).data("xwcode");
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        params = "time::" + chanyeTime + ";;xwCode::" + XW_CODE
        var x = this.getBoundingClientRect().left;
        var y = this.getBoundingClientRect().top;
        var dvicx = document.body.clientWidth;
        var dvicy = document.body.clientHeight;
        var minx = dvicx - $('.alertbox_year').width();
        var miny = dvicy - $('.alertbox_year').height();
        if (x > minx) {
            x = x - $('.alertbox_year').width();
        }
        if (y > (miny - 60)) {
            y = y - $('.alertbox_year').height();
        }
        $('.alertbox_year').data('id', $(this).data('id'));
        $('.alertbox_year').css({ 'left': x + 20, 'top': y + 20, 'visibility': 'visible' });
        $('.alertbox_year').hover(function () {
            $('.alertbox_year').css('visibility', 'visible');
        }, function () {
            $('.alertbox_year').css('visibility', 'hidden');
        })
        getDateByCommonInterface("690_yhxw_yj_009", params, AlertBoxYearData);
    })
    $('body').on('mouseout', '.pointer_year', function () {
        $('.alertbox_year').css('visibility', 'hidden');
    })
    //年弹窗数据
    function AlertBoxYearData(data) {
        var abledata = data['690_yhxw_009'];
        var uls = document.querySelectorAll('.alertbox_year .alertbox_row');
        uls.forEach(function (item, i) {
            if (i != 0) {
                i = i - 1;
                if (abledata[i]['MB'] == null) {
                    abledata[i]['MB'] = "-"
                }
                if (abledata[i]['YS'] == null) {
                    abledata[i]['YS'] = "-"
                }
                if (abledata[i]['DIFF'] == null) {
                    abledata[i]['DIFF'] = "-"
                }
                switch (i) {
                    case 1:
                    case 3:
                    case 5: item.children[1].innerHTML = toPercent2(Number(abledata[i]['MB']));
                        item.children[2].innerHTML = toPercent2(Number(abledata[i]['YS']));
                        item.children[3].innerHTML = toPercent2(abledata[i]['DIFF']);
                        break;
                    case 2:
                        item.children[1].innerHTML = toPercent(abledata[i]['MB']);
                        item.children[2].innerHTML = toPercent(abledata[i]['YS']);
                        item.children[3].innerHTML = toPercent(abledata[i]['DIFF']);
                        break;

                    case 4: item.children[1].innerHTML = abledata[i]['MB'] + '人';
                        item.children[2].innerHTML = abledata[i]['YS'] + '人';
                        item.children[3].innerHTML = abledata[i]['DIFF'] + '人';
                        break;
                    case 6: item.children[1].innerHTML = toPercent3(AddZero(abledata[i]['MB'])) + '万';
                        item.children[2].innerHTML = toPercent3(AddZero(abledata[i]['YS'])) + '万';
                        item.children[3].innerHTML = toPercent3(AddZero(abledata[i]['DIFF'])) + '万';
                        break;
                    default: item.children[1].innerHTML = toPercent3(AddZero(abledata[i]['MB']));
                        item.children[2].innerHTML = toPercent3(AddZero(abledata[i]['YS']));
                        item.children[3].innerHTML = toPercent3(AddZero(abledata[i]['DIFF']));
                }

                if (parseFloat(item.children[3].innerHTML) < 0) {
                    item.children[2].style.color = 'red';
                    item.children[3].style.color = 'red';
                } else {
                    item.children[2].style.color = 'white';
                    item.children[3].style.color = 'white';
                }
            }

        })
    }
    //月份弹窗
    $('body').on('mouseover', '.column_2 .pointer_month', function () {

        var XW_CODE = $(this).data("xwcode");
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        var params = "time::" + chanyeTime + ";;xwCode::" + XW_CODE
        var x = this.getBoundingClientRect().left;
        var y = this.getBoundingClientRect().top;
        var dvicx = document.body.clientWidth;
        var dvicy = document.body.clientHeight;
        var minx = dvicx - $('.alertbox_month').width();
        var miny = dvicy - $('.alertbox_month').height();
        if (x > minx) {
            x = x - $('.alertbox_month').width();
        }
        if (y > (miny - 60)) {
            y = y - $('.alertbox_month').height();
        }
        $('.alertbox_month').data('id', $(this).data('id'));
        $('.alertbox_month').css({ 'left': x + 20, 'top': y + 20, 'visibility': 'visible' });
        $('.alertbox_month').hover(function () {
            $('.alertbox_month').css('visibility', 'visible');
        }, function () {
            $('.alertbox_month').css('visibility', 'hidden');
        })
        getDateByCommonInterface("690_yhxw_yj_010", params, AlertBoxMonthData);

    })
    $('body').on('mouseout', '.column_2 .pointer_month', function () {
        $('.alertbox_month').css('visibility', 'hidden');
    })
    //月弹窗数据
    function AlertBoxMonthData(data) {
        var abledata = data['690_yhxw_010'];
        var uls = document.querySelectorAll('.alertbox_month .alertbox_row');
        uls.forEach(function (item, i) {
            if (i != 0) {
                i = i - 1;
                if (abledata[i]['MB'] == null || abledata[i]['MB'] == undefined) {
                    abledata[i]['MB'] = "-"
                }
                if (abledata[i]['YS'] == null || abledata[i]['YS'] == undefined) {
                    abledata[i]['YS'] = "-"
                }
                if (abledata[i]['SJ'] == null || abledata[i]['SJ'] == undefined) {
                    abledata[i]['SJ'] = "-"
                }
                if (abledata[i]['DIFF'] == null || abledata[i]['DIFF'] == undefined) {
                    abledata[i]['DIFF'] = "-"
                }
                switch (i) {
                    case 1:
                    case 3:
                    case 5: item.children[1].innerHTML = toPercent2(abledata[i]['MB']);
                        item.children[2].innerHTML = toPercent2(abledata[i]['YS']);
                        item.children[3].innerHTML = toPercent2(abledata[i]['SJ']);
                        item.children[4].innerHTML = toPercent2(abledata[i]['DIFF']);
                        break;
                    case 2: item.children[1].innerHTML = toPercent(abledata[i]['MB']);
                        item.children[2].innerHTML = toPercent(abledata[i]['YS']);
                        item.children[3].innerHTML = toPercent(abledata[i]['SJ']);
                        item.children[4].innerHTML = toPercent(abledata[i]['DIFF']);
                        break;
                    case 4: item.children[1].innerHTML = abledata[i]['MB'] + '人';
                        item.children[2].innerHTML = abledata[i]['YS'] + '人';
                        item.children[3].innerHTML = abledata[i]['SJ'] + '人';
                        item.children[4].innerHTML = abledata[i]['DIFF'] + '人';
                        break;
                    case 6: item.children[1].innerHTML = toPercent3(AddZero(abledata[i]['MB'])) + '万';
                        item.children[2].innerHTML = toPercent3(AddZero(abledata[i]['YS'])) + '万';
                        item.children[3].innerHTML = toPercent3(AddZero(abledata[i]['SJ'])) + '万';
                        item.children[4].innerHTML = toPercent3(AddZero(abledata[i]['DIFF'])) + '万';
                        break;
                    case 0: item.children[1].innerHTML = toPercent3(AddZero(abledata[i]['MB']));
                        item.children[2].innerHTML = toPercent3(AddZero(abledata[i]['YS']));
                        item.children[3].innerHTML = toPercent3(AddZero((abledata[i]['SJ'])));
                        item.children[4].innerHTML = toPercent3(AddZero((abledata[i]['DIFF'])));
                        break;
                }
                if (Number(abledata[i]['YS']) < Number(abledata[i]['MB']) && Number(abledata[i]['YS']).toFixed(3) !== Number(abledata[i]['MB']).toFixed(3) && Number(abledata[i]['YS']).toFixed(2) !== Number(abledata[i]['MB']).toFixed(2)) {
                    item.children[2].style.color = 'red';
                } else {
                    item.children[2].style.color = 'white';
                }
                if (parseFloat(item.children[4].innerHTML) < 0) {
                    item.children[3].style.color = 'red';
                    item.children[4].style.color = 'red';
                } else {
                    item.children[3].style.color = 'white';
                    item.children[4].style.color = 'white';
                }
            }

        })
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
        } else {
            return '-'
        }

    }
    //获取当月最后一天
    function getLastDay(year, month) {
        var new_year = year;  //取当前的年份
        var new_month = month;
        if (month > 12) {
            new_month -= 12;    //月份减
            new_year++;      //年份增
        }
        var new_date = new Date(new_year, new_month, 1);   //取当年当月中的第一天
        return formatDate(new Date(new_date.getTime() - 1000 * 60 * 60 * 24));//获取当月最后一天日期
    }
    //年-跳转点击事件
    $('body').on('click', '.column_1 .pointer', function () {
        localStorage.setItem('yhxwData', JSON.stringify({
            selectXwCode: $("#select_xwcode option:selected").val(),
            left_arrow: $('.left_arrow').css('display'),
            fromClick: true
        }));
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        var XW_CODE = $(this).data("xwcode");
        //var XW_CODE = $("#select_xwcode option:selected").val();
        var XW_NANE = $(this).data("xwname");
        var XWZ = $(this).data("xwz");
        XW_NAME = encodeURI(encodeURI(XW_NANE));
        XWZ = encodeURI(encodeURI(XWZ));
        var inCode = $("#select_indust option:selected").val();
        year = $(this).data("year");
        location.href = "/bigSreen/sys/v1/yhxv-2?XW_CODE=" + XW_CODE + "&&time=" + chanyeTime + "&&XW_NAME=" + XW_NAME + "&&XWZ=" + XWZ + "&&year=" + year + "&&inCode=" + inCode;

    })
    //月-跳转点击事件
    $('body').on('click', '.column_2 .pointer', function () {
        localStorage.setItem('yhxwData', JSON.stringify({
            selectXwCode: $("#select_xwcode option:selected").val(),
            left_arrow: $('.left_arrow').css('display'),
            fromClick: true
        }));
        var chanyeTime = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        var XW_CODE = $(this).data("xwcode");
        //var XW_CODE = $("#select_xwcode option:selected").val();
        var XW_NANE = $(this).data("xwname");
        var XWZ = $(this).data("xwz");
        XW_NAME = encodeURI(encodeURI(XW_NANE));
        XWZ = encodeURI(encodeURI(XWZ));
        var inCode = $("#select_indust option:selected").val();
        month = chanyeTime.substring(4, 6)
        location.href = "/bigSreen/sys/v1/yhxv-3?XW_CODE=" + XW_CODE + "&&time=" + chanyeTime + "&&XW_NAME=" + XW_NAME + "&&XWZ=" + XWZ + "&&month=" + month + "&&inCode=" + inCode;
    })
});


//add

let com_barSeries = {
    type: 'bar',
};
function allBar(data) {
    let ec0000_bar = echarts.init($("#ec00_bar")[0]);
    ec0000_bar.clear();
    let ec0001_bar = echarts.init($("#ec01_bar")[0]);
    ec0001_bar.clear();
    let ec0002_bar = echarts.init($("#ec02_bar")[0]);
    ec0002_bar.clear();
    let ec0003_bar = echarts.init($("#ec03_bar")[0]);
    ec0003_bar.clear();

    ec_00(data['690_yhxw_t5'][0], "#ec00_bar");
    ec_04(data['690_yhxw_t1'][0], "#ec04_bar");
    ec_04(data['690_yhxw_t2'][0], "#ec05_bar");
    ec_04(data['690_yhxw_t3'][0], "#ec06_bar");
    ec_04(data['690_yhxw_t4'][0], "#ec07_bar");
    ['#ec04_bar', '#ec05_bar', '#ec06_bar', '#ec07_bar'].forEach(function (id) {
        paintEchartsImg(id, id);
    });
    for (let i = 0; i < 3; i++) {
        if (data['690_yhxw_t0'][i]) {
            if (data['690_yhxw_t0'][i].TYPE == '目标') {
                ec_00(data['690_yhxw_t0'][i], "#ec01_bar")
            } else if (data['690_yhxw_t0'][i].TYPE == '承接') {
                ec_02(data['690_yhxw_t0'])
            } else if (data['690_yhxw_t0'][i].TYPE == '实际') {
                ec_03(data['690_yhxw_t0'])
            }
        }
    }


}
//获取所有产业收入增幅和利润率的最大值
function setMAX(data) {
    max = (data['690_cdwl_yhxw'][0]['MAX'] - 0).toFixed(2);
    beilv = 5 / max;
}
function ec_00(data, bar_n) {
    abledata = data;
    var xdata = ['首位度', '收入增幅', '利润率'];
    var ydata = [abledata.SWD / beilv, abledata.SR, abledata.LRL];
    let ec0000_bar = echarts.init($(bar_n)[0]);
    ec0000_bar.clear();
    ec0000_bar.setOption(opt_bar_vertical);
    ec0000_bar.setOption({
        color: "#0083b3",
        grid: {
            left: '0%',
            right: '10%',
            top: '25%',
            bottom: '3%'
        },
        xAxis: {
            axisLabel: { //标签名称
                interval: 0,
                margin: 2 * bodyScale,
                fontSize: 12 * bodyScale,
                color: '#0083b3',
            },
            axisLine: {
                lineStyle: {
                    color: '#0083b3'
                }
            },
            data: xdata,
        },
        yAxis: [{
            type: 'value',
            position: 'left',
            show: false,
            max: max,
        }],
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
                        align: 'center',
                        formatter: function (data) {
                            let dataValue = data.data;
                            if (data.dataIndex == 0) {
                                return '{white|' + toPercent3(dataValue * beilv) + '}'
                            } else if (data.dataIndex == 1) {
                                return '{white|' + toPercent2(dataValue) + '}'
                            }
                            else if (data.dataIndex == 2) {
                                return '{white|' + toPercent(dataValue) + '}'
                            }
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
            return $.extend(item, com_barSeries)
        }),
    });
}
function ec_02(data) {
    if (data != null) {
        for (let i = 0; i < data.length; i++) {
            if (data[i].TYPE == '目标') {
                muBiaoData = [data[i].SWD / beilv, data[i].SR, data[i].LRL];
                var MB_SWD = data[i].SWD;
            } else if (data[i].TYPE == '承接') {
                ydata = [data[i].SWD / beilv, data[i].SR, data[i].LRL];
                var CJ_SWD = data[i].SWD;
            }
        }
        var xdata = ['首位度', '收入增幅', '利润率'];
        let ec0002_bar = echarts.init($("#ec02_bar")[0]);
        ec0002_bar.clear();
        ec0002_bar.setOption(opt_bar_vertical);
        ec0002_bar.setOption({
            color: "#0083b3",
            grid: {
                left: '0%',
                right: '10%',
                top: '25%',
                bottom: '3%'
            },
            xAxis: {
                axisLabel: { //标签名称
                    interval: 0,
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                    color: '#0083b3'
                },
                axisLine: {
                    lineStyle: {
                        color: '#0083b3'
                    }
                },
                data: xdata,
            },
            yAxis: [{
                type: 'value',
                position: 'left',
                show: false,
                max: max,
            }],
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
                            align: 'center',
                            position: 'top',
                            formatter: function (data) {
                                let dataValue = data.data;
                                if (data.dataIndex == 0) {
                                    if (toPercent3(MB_SWD) > toPercent3(CJ_SWD)) {
                                        return '{red|' + toPercent3(CJ_SWD) + '}'
                                    }
                                    return '{white|' + toPercent3(CJ_SWD) + '}'
                                } else if (data.dataIndex == 1) {
                                    if (parseInt(toPercent2(muBiaoData[data.dataIndex])) > parseInt(toPercent2(dataValue))) {
                                        return '{red|' + toPercent2(dataValue) + '}'
                                    }
                                    return '{white|' + toPercent2(dataValue) + '}'
                                } else if (data.dataIndex == 2) {
                                    if (parseInt(toPercent(muBiaoData[data.dataIndex])) > parseInt(toPercent(dataValue))) {
                                        return '{red|' + toPercent(dataValue) + '}'
                                    }
                                    return '{white|' + toPercent(dataValue) + '}'
                                }
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
                return $.extend(item, com_barSeries)
            }),
        });
    }
}
function ec_03(data) {
    if (data != null) {
        for (let i = 0; i < data.length; i++) {
            if (data[i].TYPE == '目标') {
                var MB_SWD = data[i].SWD
                muBiaoData = [data[i].SWD / beilv, data[i].SR, data[i].LRL]
            } else if (data[i].TYPE == '实际') {
                var SJ_SWD = data[i].SWD
                ydata = [data[i].SWD / beilv, data[i].SR, data[i].LRL]
            }
        }
        var xdata = ['首位度', '收入增幅', '利润率'];
        let ec0002_bar = echarts.init($("#ec03_bar")[0]);
        ec0002_bar.clear();
        ec0002_bar.setOption(opt_bar_vertical);
        ec0002_bar.setOption({
            color: "#0083b3",
            grid: {
                left: '0%',
                right: '10%',
                top: '25%',
                bottom: '3%'
            },
            xAxis: {
                axisLabel: { //标签名称
                    interval: 0,
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                    color: '#0083b3'
                },
                axisLine: {
                    lineStyle: {
                        color: '#0083b3'
                    }
                },
                data: xdata,
            },
            yAxis: [{
                type: 'value',
                position: 'left',
                show: false,
                max: max,
            }],
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
                            align: 'center',
                            position: 'top',
                            formatter: function (data) {
                                let dataValue = data.data;
                                if (data.dataIndex == 0) {
                                    if (toPercent3(MB_SWD) > toPercent3(SJ_SWD)) {
                                        return '{red|' + toPercent3(SJ_SWD) + '}'
                                    }
                                    return '{white|' + toPercent3(SJ_SWD) + '}'
                                } else if (data.dataIndex == 1) {
                                    if (parseInt(toPercent2(muBiaoData[data.dataIndex])) > parseInt(toPercent2(dataValue))) {
                                        return '{red|' + toPercent2(dataValue) + '}'
                                    }
                                    return '{white|' + toPercent2(dataValue) + '}'
                                } else if (data.dataIndex == 2) {
                                    if (parseInt(toPercent(muBiaoData[data.dataIndex])) > parseInt(toPercent(dataValue))) {
                                        return '{red|' + toPercent(dataValue) + '}'
                                    }
                                    return '{white|' + toPercent(dataValue) + '}'
                                }
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
                return $.extend(item, com_barSeries)
            }),
        });
    }
}
function ec_04(data, bar_n) {
    abledata = data
    if (data != null) {
        var xdata = ['首位度', '收入增幅', '利润率'];
        var ydata = [abledata.SWD / beilv, abledata.SR, abledata.LRL];
        let ec0004_bar = echarts.init($(bar_n)[0]);
        ec0004_bar.clear();
        ec0004_bar.setOption(opt_bar_vertical);
        ec0004_bar.setOption({
            color: "#0083b3",
            animation: false,
            grid: {
                left: '0%',
                right: '10%',
                top: '25%',
                bottom: '3%'
            },
            xAxis: {
                axisLabel: { //标签名称
                    interval: 0,
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                    color: '#0083b3',
                },
                axisLine: {
                    lineStyle: {
                        color: '#0083b3'
                    }
                },
                data: xdata,
            },
            yAxis: [{
                type: 'value',
                position: 'left',
                show: false,
                max: max,
            }],
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
                            align: 'center',
                            formatter: function (data) {
                                let dataValue = data.data;
                                if (data.dataIndex == 0) {
                                    return '{white|' + toPercent3(dataValue * beilv) + '}'
                                } else if (data.dataIndex == 1) {
                                    return '{white|' + toPercent2(dataValue) + '}'
                                }
                                else if (data.dataIndex == 2) {
                                    return '{white|' + toPercent(dataValue) + '}'
                                }
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
                return $.extend(item, com_barSeries)
            }),
        });
    }
}
// 用户小微轮播控制
// 小轮播
function scroll() {
    var lunboitem = document.querySelectorAll('.column_1 .block_scroll_screen');
    lunboitem.forEach(lunbo);
    var lunboitem2 = document.querySelectorAll('.column_scroll .block_scroll_screen');
    lunboitem2.forEach(lunbo);
}
function lunbo(item, i) {
    var timers;
    function wfscroll() {
        if ($('.alertbox_year').data('id') == $(item).data('id') && $('.alertbox_year').css('visibility') == 'visible') {
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
    timers = setInterval(wfscroll, 4000);
    $(item).parent().hover(function () {
        clearInterval(timers);
    }, function () {
        timers = setInterval(wfscroll, 4000);
    })
}
// 小轮播2
function scroll2() {
    var lunboitem = document.querySelectorAll('.column_2 .block_scroll_screen');
    lunboitem.forEach(lunbo2);
}
function lunbo2(item, i) {
    var timers2;
    function wfscroll2() {
        if ($('.alertbox_month').data('id') == $(item).data('id') && $('.alertbox_month').css('visibility') == 'visible') {
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
    timers2 = setInterval(wfscroll2, 4000);
}


/*['#ec04_bar', '#ec05_bar', '#ec06_bar', '#ec07_bar'].forEach(function (id) {
    setTimeout(function () {
        paintEchartsImg(id, id);
    }, 3000);
});*/