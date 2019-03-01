var XW_CODE, time;
var params;
var param1;
$(function () {
    defaule_lineSeries = {
        type: 'line',
        smooth: false,
        showSymbol: true,
        symbol: 'circle',
        symbolSize: 3,
    };
    //返回
    $("#back_btn").on('click', function () {

        parent.location.href = "/bigSreen/sys/v1/yhxv?" + param1;
    })
    //获取url将小微主小微名称时间写入页面
    GetRequest()
    function GetRequest() {
        var url = location.search; //获取url中"?"符后的字串
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for (var i = 0; i < strs.length; i++) {
                theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
            }
        }
        XW_NAME = theRequest['XW_NAME'];
        XWZ = theRequest['XWZ'];
        XW_NAME = decodeURI(XW_NAME);
        XWZ = decodeURI(XWZ);
        $("#XW_NAME").html(XW_NAME);
        $("#XWZ").html(XWZ);

        XW_CODE = theRequest['XW_CODE'];
        time = theRequest['time'];
        inCode = theRequest['inCode'];
        //接口中的参数
        params = "xwCode::" + XW_CODE + ";;time::" + time;
        //获取右下角表格数据
        getDateByCommonInterface('690_yhxw_l007', params, setTableData);
        //获取评论框数据
        getDateByCommonInterface('690_yhxw_l008', params, setTextData);
        //获取三个折线图数据
        eclAll('690_yhxw_ej_017')
        eclAll('690_yhxw_ej_019')
        eclAll('690_yhxw_ej_021')
        //获取三个柱状图数据
        getDateByCommonInterface("690_yhxw_ej_024", params, setBarData);
        //获取折线图上升下降趋势
        getDateByCommonInterface("690_yhxw_ej_023", params, changeIcon);
        param1 = "inCode=" + inCode + "&&time=" + time + "&&xw_code=ALL";
        // 获取左下角结论
        getDateByCommonInterface("690_yhxw_er_pl", params, setSummary);


    }
    // 设置左下角结论
    function setSummary(data) {
        var abledata = data['690_yhxw_er_pl'][0];
        if (abledata['CONCLUSION']) {
            $('.jieLun_cont').html(abledata['CONCLUSION']);
        }
    }
    //封装柱状图数据
    function setBarData(data) {
        var SWD = []; var SRZF = []; var LRL = []
        var abledata = data['690_yhxw_024'][0]
        SWD = [abledata['MB_SWD'], abledata['YS_SWD'], abledata['SJ_SWD']]
        SRZF = [abledata['ROUND(MB_SRZF,2)'], abledata['ROUND(YS_SRZF,2)'], abledata['ROUND(SJ_SRZF,2)']]
        LRL = [abledata['ROUND(MB_LRL,2)'], abledata['ROUND(YS_LRL,2)'], abledata['ROUND(SJ_LRL,2)']]
        //柱状图
        createChart(SWD, 'bar1', '#ec01_bar');
        createChart2(SRZF, 'bar2', '#ec02_bar', abledata['MAX']);
        createChart2(LRL, 'bar3', '#ec03_bar', abledata['MAX']);
    }
    //封装折线图趋势数据
    function changeIcon(data) {
        var v1, v2, v3
        var abledata = data["690_yhxw_023"];
        abledata.forEach(function (item, i) {
            if (item.PROJECT == '实际首位度') {
                v1 = item["TYOE"];
            } else if (item.PROJECT == '实际收入增幅') {
                v2 = item["TYOE"];
            } else if (item.PROJECT == '实际利润率') {
                v3 = item["TYOE"];
            }
        })

        getBackStyle(v1, "arrow_box_0");
        getBackStyle(v2, "arrow_box_1");
        getBackStyle(v3, "arrow_box_2");

    }
    function getBackStyle(nums, ids) {
        var style = "arrow_up";
        if (nums == undefined) {
            $("#" + ids).addClass(style);
            return;
        }
        if (nums.indexOf(".") == 0) {
            nums = "0" + nums;
        }
        if (parseFloat(nums) > 0) {
            style = "arrow_up";
        } else if (parseFloat(nums) < 0) {
            style = "arrow_down";
        } else {
            style = "arrow_on";
        }
        $("#" + ids).addClass(style);
    }
    function eclAll(dataType) {
        //折线图-首位度实际
        getDateByCommonInterface(dataType, params, setSJData);
    }

    //封装折线图-首位度实际数据
    function setSJData(data) {
        Xdata = []
        SJdata = []
        if (data['690_yhxw_017']) {
            var abledata = data['690_yhxw_017']
            abledata.forEach(function (item, i) {
                SJdata.push(toPercent3(item['SJ_SWD']));
                Xdata.push(item['DATE_DT'].substring(4, 6));
            })
            //折线图-首位度目标
            getDateByCommonInterface2('690_yhxw_ej_018', params, setSWDMBData, SJdata, Xdata);
        } else if (data['690_yhxw_019']) {
            var abledata = data['690_yhxw_019']
            abledata.forEach(function (item, i) {
                SJdata.push(Number(item['SJ_SRZF']).toFixed(2));
                Xdata.push(item['DATE_DT'].substring(4, 6));
            })
            //折线图-收入增幅目标
            getDateByCommonInterface2('690_yhxw_ej_020', params, setSRZFMBData, SJdata, Xdata);
        } else if (data['690_yhxw_021']) {
            var abledata = data['690_yhxw_021']
            abledata.forEach(function (item, i) {
                SJdata.push(Number(item['SJ_SR']).toFixed(3));
                Xdata.push(item['DATE_DT'].substring(4, 6));
            })
            //折线图-利润率目标
            getDateByCommonInterface2('690_yhxw_ej_022', params, setLRLMBData, SJdata, Xdata);
        }


    }
    //封装折线图-首位度目标数据
    function setSWDMBData(data, SJdata, Xdata) {
        var MBdata = [];
        var abledata = data['690_yhxw_018']
        abledata.forEach(function (item, i) {
            MBdata.push(toPercent3(item['MB_SWD']));
        })
        setLineData('ec1_line', MBdata, SJdata, Xdata, '690_yhxw_018');
    }
    //封装折线图-收入增幅目标数据
    function setSRZFMBData(data, SJdata, Xdata) {
        var MBdata = [];
        var abledata = data['690_yhxw_020']
        abledata.forEach(function (item, i) {
            MBdata.push(Number(item['MB_SRZF']).toFixed(2));
        })

        setLineData('ec2_line', MBdata, SJdata, Xdata, '690_yhxw_020');
    }
    //封装折线图-利润率目标数据
    function setLRLMBData(data, SJdata, Xdata) {
        var MBdata = [];
        var abledata = data['690_yhxw_022']
        abledata.forEach(function (item, i) {
            MBdata.push(Number(item['MB_SR']).toFixed(3));
        })
        setLineData('ec3_line', MBdata, SJdata, Xdata, '690_yhxw_022');
    }

    //月份-封装评论框数据
    function setTextData(data) {
        //console.log(data)
        var abledata = data['690_yhxw_t1'];
        $("#txt1").val(abledata[0].DMB_SJYC);
        $("#txt2").val(abledata[0].XXJD_YSYC);
        $("#txt3").val(abledata[0].ZZFX_SQMX);
    }
    //月份-将表格数据按行分类
    function setTableData(data) {
        //console.log(data)
        var abledata = data['690_yhxw_t'];
        abledata.forEach(function (item, i) {
            switch (item['BLJD']) {
                case '用户小微': fun2(abledata[i], 0); break;
                case '设计': fun2(abledata[i], 1); break;
                case '模块采购': fun2(abledata[i], 2); break;
                case '销售': fun2(abledata[i], 3); break;
                case '供应链': fun2(abledata[i], 4); break;
                case '服务': fun2(abledata[i], 5); break;
            }
        });

    }
    //向表格中插入数据
    function fun2(data, i) {
        for (const j in data) {
            if (data[j] == null) {
                data[j] = '-';
            }
        }
        var a = "#tr" + i
        var trs = document.querySelectorAll(a + ' td+td');
        trs[0].innerHTML = data['ENAME'];
        trs[1].innerHTML = data['MB_DAN'];
        trs[2].innerHTML = data['SJ_DAN'];
        trs[3].innerHTML = data['YSC_MB'];
        trs[4].innerHTML = data['YSC_ZZFX'];
        trs[5].innerHTML = data['YSC_CJ'];
        trs[6].innerHTML = data['YSC_CJC'];
        trs[7].innerHTML = data['YSC_SJ'];
        trs[8].innerHTML = data['YSC_DIFF'];
        //trs[9].innerHTML = data['YSC_GCYS'];
    }


    //var mbData = [2, 2.5, 2, 2, 2, 2.5, 2, 2, 2, 2.5, 2];
    //var sjData = [1, 1.5, 1, 1, 1, 1.5, 1, 1, 1, 1.5, 1];

    /* setLineData('ec2_line', mbData, sjData);
     setLineData('ec3_line', mbData, sjData);
     setLineData('alertbox', mbData, []);*/

    //用户付薪弹窗弹窗
    $('body').on('mouseover', '.yh_name', function () {
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
        $('.alertbox_year').css({ 'left': x + 60, 'top': y + 20, 'visibility': 'visible' });
        $('.alertbox_year').hover(function () {
            $('.alertbox_year').css('visibility', 'visible');
        }, function () {
            $('.alertbox_year').css('visibility', 'hidden');
        })
        var name = $(this).html();
    })
    $('body').on('mouseout', '.yh_name', function () {
        $('.alertbox_year').css('visibility', 'hidden');
    })
    //并联节点弹窗弹窗
    $('body').on('click', '.yh_jiedian', function () {
        var name = $(this).html();
        param2 = name + time + XW_CODE
        //window.open('http://www.baidu.com')
    })

})
/**
 * 绘制首位度柱状图
 * @param   data     请求得到的数据
 * @param   dataType 接口类型
 * @param   oDiv     目标元素id
 * @param   str      数据前缀（目标，预算，实际）
 */
function createChart(data, dataType, oDiv) {
    let abledata = data;
    $(oDiv).empty();
    if (false) {
        console.log(`${oDiv}无轮播图数据`);
    } else {
        var xdata = ['目标', '承接', '实际'];
        var ydata = abledata;
        let ec0001_bar = echarts.init($(oDiv)[0]);
        ec0001_bar.clear();
        ec0001_bar.setOption(opt_bar_vertical);
        ec0001_bar.setOption({
            animation: false,
            color: "#0083b3",
            grid: {
                left: 0,
                right: '5%',
                top: '12%',
                bottom: '3%'
            },
            xAxis: {
                axisLabel: { //标签名称
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                    interval: 0,
                    color: '#0083b3'
                },
                data: xdata,
            },
            yAxis: {
                max: 5,
                show: false,
            },
            series: [{
                data: ydata,
                itemStyle: {
                    normal: {
                        color: function (params) {
                            var colorList = [
                                '#0070C0',
                                '#FABD02',
                                '#92B3D6',
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: true,
                            color: '#fff',
                            position: 'top',
                            fontSize: 13 * bodyScale,
                            formatter: function (data) {
                                if (dataType == 'bar1') {
                                    return toPercent3(data.data);
                                } else if (dataType == 'bar2') {
                                    return toPercent2(data.data);
                                } else if (dataType == 'bar3') {
                                    return toPercent(data.data);
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
    }
}
/**
 * 绘制收入增幅和利润率柱状图
 * @param   data     请求得到的数据
 * @param   dataType 接口类型
 * @param   oDiv     目标元素id
 * @param   str      数据前缀（目标，预算，实际）
 */
function createChart2(data, dataType, oDiv, max) {
    let abledata = data;
    $(oDiv).empty();
    if (false) {
        console.log(`${oDiv}无轮播图数据`);
    } else {
        var xdata = ['目标', '承接', '实际'];
        var ydata = abledata;
        let ec0001_bar = echarts.init($(oDiv)[0]);
        ec0001_bar.clear();
        ec0001_bar.setOption(opt_bar_vertical);
        ec0001_bar.setOption({
            animation: false,
            color: "#0083b3",
            grid: {
                left: 0,
                right: '5%',
                top: '15%',
                bottom: '3%'
            },
            xAxis: {
                axisLabel: { //标签名称
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                    interval: 0,
                    color: '#0083b3'
                },
                data: xdata,
            },
            yAxis: {
                show: false,
                max: max
            },
            series: [{
                data: ydata,
                itemStyle: {
                    normal: {
                        color: function (params) {
                            var colorList = [
                                '#0070C0',
                                '#FABD02',
                                '#92B3D6',
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: true,
                            color: '#fff',
                            position: 'top',
                            fontSize: 13 * bodyScale,
                            formatter: function (data) {
                                if (dataType == 'bar1') {
                                    return toPercent3(data.data);
                                } else if (dataType == 'bar2') {
                                    return toPercent2(data.data);
                                } else if (dataType == 'bar3') {
                                    return toPercent(data.data);
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
    }
}
/**
 * 绘制折线图
 */
function setLineData(echartId, mbData, sjData, Xdata, dataType) {
    var xdata = Xdata;
    var ydataMB = mbData;
    var ydataSJ = sjData;
    var mb_position = 'top';
    var sj_position = 'bottom';
    if (echartId == 'ec2_line') {
        var mb_position = 'bottom';
        var sj_position = 'top';
    }
    // var abledata = data[dataType];
    let commonLine = echarts.init($("#" + echartId)[0]);
    commonLine.clear();
    commonLine.setOption(com_line);
    commonLine.setOption({
        grid: {
            left: '7%',
            bottom: '20%',
            right: '9%',
            top: '20%'
        },
        // legend: {
        //     show: true,
        //     selectedMode: false,
        //     left: 'right',
        //     data: [{name:'目标',icon:'line'},{name: '实际',icon:'line'}],
        //     top: '-2%',
        //     itemHeight: 8 * bodyScale,
        //     itemWidth: 10 * bodyScale,
        //     textStyle: {
        //         fontSize: 11 * bodyScale,
        //         color: '#0083b3'
        //     }
        // },
        tooltip: {
            show: false,
        },
        xAxis: [
            {
                position: 'bottom',
                boundaryGap: 0,
                axisLabel: { //标签名称
                    show: false,
                },
                axisLine: {
                    show: false
                },
                data: xdata,
            },
            {
                offset: 23 * bodyScale,
                position: 'bottom',
                name: '月份',
                nameGap: 15 * bodyScale,
                boundaryGap: 0,
                axisLabel: {
                    fontSize: 10 * bodyScale,
                },
                axisTick: {
                    show: true,
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
            name: "目标",
            // stack: '总量',
            data: ydataMB,
            label: {
                normal: {
                    padding: 0,
                    align: 'left',
                    show: true,
                    position: mb_position,
                    fontSize: 9 * bodyScale,
                    color: '#0083b3',
                    formatter: function (data) {
                        let dataValue = data.data;
                        if (dataType === '690_yhxw_018') {
                            dataValue -= 0;
                            return toPercent3(dataValue)
                        } else if (dataType === '690_yhxw_020') {
                            return toPercent2(dataValue)
                        } else if (dataType === '690_yhxw_022') {
                            return toPercent(dataValue)
                        }
                    },
                }
            },
            lineStyle: {
                normal: {
                    width: 1,
                    color: "#0083b3",
                }
            }, itemStyle: {
                normal: {
                    width: 1,//折线宽度
                    color: "#0083b3",
                },
            },

        }, {
            data: ydataSJ,
            name: "实际",
            // stack: '总量',
            label: {
                normal: {
                    padding: 0,
                    align: 'right',
                    show: true,
                    position: sj_position,
                    fontSize: 9 * bodyScale,
                    color: '#fff',
                    formatter: function (data) {
                        let dataValue = data.data;
                        if (dataType === '690_yhxw_018') {
                            dataValue -= 0;
                            return toPercent3(dataValue)
                        } else if (dataType === '690_yhxw_020') {
                            return toPercent2(dataValue)
                        } else if (dataType === '690_yhxw_022') {
                            return toPercent(dataValue)
                        }
                    },
                }
            },
            lineStyle: {
                normal: {
                    width: 1,
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    width: 1,//折线宽度
                    color: "#fff",  // 会设置点和线的颜色，所以需要下面定制 line
                }

            }
        }
        ].map(function (item) {
            return $.extend(item, defaule_lineSeries)
        }),
    });
}
function toPercent2(point) {
    var str = "-"
    if (point != "-") {
        str = Number(point * 100).toFixed(0);
        str += "%";
    }
    return str;

}
function toPercent3(point) {
    var str = "-"
    if (point != "-") {
        str = Number(point).toFixed(1);
    }
    return str;
}
//化成百分数保留一位小数
function toPercent(point) {
    var str = "-"
    if (point != "-") {
        str = Number(point * 100).toFixed(1);
        str += "%";
    }
    return str;

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
/**
 * 通过统一接口请求接口数据
 * @param dataType:统一接口dataType
 * @param params：接口参数
 * @param callBack：回调函数
 */
function getDateByCommonInterface2(dataType, params, successCallBack, SJData, XData) {
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
                successCallBack(jsonData.data, SJData, XData);
            }
        }
    })
};