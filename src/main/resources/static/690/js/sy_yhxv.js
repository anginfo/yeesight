var params = "";
var inCode = "";
var time
var max
var beilv
 //设置默认日期
 if (new Date().getDate() > 4) {
    time = (formatDate(new Date() - 1000 * 60 * 60 * 24)).replace(/-/g, "");
} else {
    if(new Date().getMonth()==0){
        time  = (getLastDay(new Date().getFullYear()-1,12)).replace(/-/g, "");
    }
    time  = (getLastDay(new Date().getFullYear(),new Date().getMonth())).replace(/-/g, "");
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
//化成百分数，不保留小数位
function toPercent2(point) {
    var str = "-"
    if (point != "-") {
        str = Number(point * 100).toFixed(0);
        str += "%";
    }
    return str;

}
//将数据保留一位小数
function toPercent3(point) {
    var str = "-"
    if (point != "-") {
        str = Number(point).toFixed(1);
    }
    return str;
}

//获取所有产业收入增幅和利润率的最大值
getDateByCommonInterface("690_yhxw_l0011", "time::" + time, setMAX);
function setMAX(data) {
    max = data['690_yhxw_t6'][0]['MAX'];
    beilv = 5 / max;
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
$(function () {
    allInCode = ['BAA', 'ABA', 'BCB', 'FAB', 'BCD', 'BAA', 'ABA', 'BCB', 'FAB', 'BCD']
    //获取平台柱状图数据
    getDateByCommonInterface("690_yhxw_yj_018", "time::" + time, setPTBar);
    //获取平台数据
    getDateByCommonInterface("690_yhxw_pt_cy", "time::" + time, setPTData);
    //获取平台评论数据
    getDateByCommonInterface("690_yhxw_pl", "time::" + time, setPTPLData);
    allInCode.forEach(function (item, i) {
        params = "inCode::" + item + ";;time::" + time;
        params1 = "inCode::" + item + ";;time::" + time + ";;xw_code::ALL"
        //获取产业柱状图数据
        getDateByCommonInterfaceByParam('690_yhxw_l001', params, allBar, allBar, i);
        //获取页面数据
        getDateByCommonInterfaceByParam('690_yhxw_yj_004t', params1, setMonthSJData, setMonthSJData, i);
        //获取产业星级数据
        getDateByCommonInterfaceByParam("690_yhxw_yj_015", params1, levels, levels, i);
        //获取评论数据
        getDateByCommonInterfaceByParam("690_yhxw_p03", params, setCommentData, setCommentData, i);
    })
    setTimeout(() => {
        setScroll();
    }, 2000);

    //封装产业评论数据
    function setCommentData(data, dataIndex) {
        var pinglun = "";
        if (dataIndex == 0) {
            pinglun = "<span>地位:</span><input value=" + data['690_yhxw_p3'][0]['SJ_HYDW'].replace(/[\r\n]/g, "").replace(/>/g,'＞') + "><span>升级:</span><input value=" + data['690_yhxw_p3'][0]['SJ_SJ'].replace(/[\r\n]/g, "") + ">";
            $(".pinglun1").html(pinglun);
        } else if (dataIndex == 1) {
            pinglun = "<span>地位:</span><input value=" + data['690_yhxw_p3'][0]['SJ_HYDW'].replace(/[\r\n]/g, "").replace(/>/g,'＞') + "><span>升级:</span><input value=" + data['690_yhxw_p3'][0]['SJ_SJ'].replace(/[\r\n]/g, "") + ">";
            $(".pinglun2").html(pinglun);
        } else if (dataIndex == 2) {
            pinglun = "<span>地位:</span><input value=" + data['690_yhxw_p3'][0]['SJ_HYDW'].replace(/[\r\n]/g, "").replace(/>/g,'＞') + "><span>升级:</span><input value=" + data['690_yhxw_p3'][0]['SJ_SJ'].replace(/[\r\n]/g, "") + ">";
            $(".pinglun3").html(pinglun);
        } else if (dataIndex == 3) {
            pinglun = "<span>地位:</span><input value=" + data['690_yhxw_p3'][0]['SJ_HYDW'].replace(/[\r\n]/g, "").replace(/>/g,'＞') + "><span>升级:</span><input value=" + data['690_yhxw_p3'][0]['SJ_SJ'].replace(/[\r\n]/g, "") + ">";
            $(".pinglun4").html(pinglun);
        } else if (dataIndex == 4) {
            pinglun = "<span>地位:</span><input value=" + data['690_yhxw_p3'][0]['SJ_HYDW'].replace(/[\r\n]/g, "").replace(/>/g,'＞') + "><span>升级:</span><input value=" + data['690_yhxw_p3'][0]['SJ_SJ'].replace(/[\r\n]/g, "") + ">";
            $(".pinglun5").html(pinglun);
        }
    }
    //写入产业星级和产业主
    function levels(data, dataIndex) {
        // console.log(data)
        
        tit = ".title" + Number(dataIndex + 2)
        let name = data['690_yhxw_015'][0]['INDUSTRY_Z'];
        $(tit).append(name + "&ensp;" + "&ensp;")
        let num = data['690_yhxw_015'][1]['XJ'];
        for (i = 0; i < num; i++) {
            $(tit).append('<span class="star_cy"></span>')
        }

    }
    //封装平台柱状图数据
    function setPTBar(data) {
        // console.log(data)
        MBData = [Number(data['690_yhxw_yj_018'][0]['MB_SRZF']).toFixed(1), Number(data['690_yhxw_yj_018'][0]['MB_LRZF']).toFixed(2), Number(data['690_yhxw_yj_018'][0]['MB_LRL']).toFixed(3)];
        SJData = [Number(data['690_yhxw_yj_018'][0]['SJ_SRZF']).toFixed(1), Number(data['690_yhxw_yj_018'][0]['SJ_LRZF']).toFixed(2), Number(data['690_yhxw_yj_018'][0]['SJ_LRL']).toFixed(3)];
        createChart2(MBData, SJData, `#ec00_bar`);
    }
    function setPTData(data) {
        var abledata = data['690_yhxw_pt_cy'];
        area = ".area_1"
        arr = grouping(abledata);
        $(area + ' .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $(area + ' .block_tit').each(function (i, item) {
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
        forHundred(a);
    }
    function setPTPLData(data){
        console.log(data)
        ptpl = "升级: </span><input value=" + data['690_yhxw_pl'][0]['XWSJ'].replace(/[\r\n]/g, "") + ">";
        $(".pinglun0").html(ptpl);
    }
    //封装所有产业柱状图
    function allBar(data, dataIndex) {
        var MBData = []
        var SJData = []
        var MBData = [Number(data['690_yhxw_t0'][2]['SWD']).toFixed(1), Number(data['690_yhxw_t0'][2]['SR']).toFixed(2), Number(data['690_yhxw_t0'][2]['LRL']).toFixed(3)];
        var SJData = [Number(data['690_yhxw_t0'][0]['SWD']).toFixed(1), Number(data['690_yhxw_t0'][0]['SR']).toFixed(2), Number(data['690_yhxw_t0'][0]['LRL']).toFixed(3)];
        var index = dataIndex + 4
        if (index < 10) {
            createChart(MBData, SJData, "#ec0" + Number(index) + "_bar");
        } else {
            createChart(MBData, SJData, "#ec" + Number(index) + "_bar");
        }
        // if (dataIndex == 0) {
        //     createChart(MBData, SJData, `#ec04_bar`);
        // } else if (dataIndex == 1) {
        //     createChart(MBData, SJData, `#ec05_bar`);
        // } else if (dataIndex == 2) {
        //     createChart(MBData, SJData, `#ec06_bar`);  
        // } else if (dataIndex == 3) {
        //     createChart(MBData, SJData, `#ec07_bar`);
        // } else if (dataIndex == 4) {
        //     createChart(MBData, SJData, `#ec08_bar`);
        // } else if (dataIndex == 5) {
        //     createChart(MBData, SJData, `#ec09_bar`);
        // } else if (dataIndex == 6) {
        //     createChart(MBData, SJData, `#ec10_bar`);
        // } else if (dataIndex == 7) {
        //     createChart(MBData, SJData, `#ec11_bar`);
        // } else if (dataIndex == 8) {
        //     createChart(MBData, SJData, `#ec12_bar`);
        // } else if (dataIndex == 9) {
        //     createChart(MBData, SJData, `#ec13_bar`);
        // }
    }


    //产业1实际
    function setMonthSJData(data, dataIndex) {
        var abledata = data['690_yhxw_004t'];
        area = ".area_" + Number(dataIndex + 2)
        arr = grouping(abledata);
        $(area + ' .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        let a = [];
        $(area + ' .block_tit').each(function (i, item) {
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
        forHundred(a);
    }
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
            if (arr[0]['PT_NAME']) {
                str = '<div class="block_scroll_screen_off">';
                arr.forEach(creatStr);
                str += '</div>';
            } else {
                str = '<div class="block_scroll_screen">';
                arr.forEach(creatStr);
                str += '</div>';
            }
        }
        return str;
    }
    // 3.拼接字符串
    function creatStr(item, i) {
        if (item['PT_NAME']) {
            // console.log(item)
            str +=
            `<div class="roll flex_center" style=` + diced(item) + `>
                    <div class="pointer pointer_year" data-id="`+ countScroll + `" data-xwcode="` + item.XW_CODE + `"data-xwname="` + item['XW_NAME'] + `"data-xwz="` + item['XWZ'] + `"data-year="` + item['YEAR'] + `">
                        <div class="p_on `+ xJudge(item) + `" >
                            <span class=`+ starColor(item) + `></span>
                            <div class="p_on_tit" style="display:inline-block;margin-left:1rem;">` + item['INDUSTRY_NAME'] + `<span class="rise_ico ` + rise(item) + `"></span></div>
                            <div class=`+ centerTxt(xJudge(item)) + `>目标 ：` + toPercent3(item['MB_SWD']) + `/` + toPercent2(item['MB_SR']) + `/` + toPercent(item['MB_LRL']) +
                            `，实际 ：<span style="color:` + (toPercent3(item['SJ_SWD']) < toPercent3(item['MB_SWD']) ? 'red' : '') + `;">` + toPercent3(item['SJ_SWD']) + `</span>/<span style="color:` + (parseFloat(toPercent2(item['SJ_SR'])) < parseFloat(toPercent2(item['MB_SR'])) ? 'red' : '') + `;">` + toPercent2(item['SJ_SR']) + `</span>/<span style="color:` + (parseFloat(toPercent(item['SJ_LRL'])) < parseFloat(toPercent(item['MB_LRL'])) ? 'red' : '') + `;">` + toPercent(item['SJ_LRL']) + `</span></div>
                        </div>
                    </div>
                </div>`;
        } else {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_year" data-id="`+ countScroll + `" data-xwcode="` + item.XW_CODE + `"data-xwname="` + item['XW_NAME'] + `"data-xwz="` + item['XWZ'] + `"data-year="` + item['YEAR'] + `">
                        <div class="p_on `+ xJudge(item) + `" >
                            <span class=`+ starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + item['XW_NAME'] + `(` + item['XWZ'] + `)<span class="` + (item['HQ_FLAG'] == '0' && item['XJ'] != '1' && item['XJ'] != '2' ? 'p_on_img' : '') + `"></span>` + `</div>
                            <div>目标 ：横`+ toPercent3(item.MB_SWD) + `/` + toPercent2(item.MB_SRZF) + `/` + toPercent(item.MB_LRL) + `, 纵` + toPercent2(item.MB_BZD) + `/` + toPercent2(item.MB_YSLC) + `/` + toPercent3(item.MB_ZZFX) + `万元</div>
                            <div>实际 ：横<span style="color:`+ ((toPercent3(item['SWD']) - toPercent3(item['MB_SWD'])) < 0 ? 'red' : '') + `;">` + toPercent3(item['SWD']) + `</span>/<span style="color:` + ((parseInt(toPercent2(item['SRZF'])) - parseInt(toPercent2(item['MB_SRZF']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['SRZF']) + `</span>/<span style="color:` + ((parseInt(toPercent(item['LRL'])) - parseInt(toPercent(item['MB_LRL']))) < 0 ? 'red' : '') + `;">` + toPercent(item['LRL']) + `</span>, 纵<span style="color:` + ((parseInt(toPercent2(item['BZD'])) - parseInt(toPercent2(item['MB_BZD']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['BZD']) + `</span>/<span style="color:` + ((parseInt(toPercent2(item['YSLC'])) - parseInt(toPercent2(item['MB_YSLC']))) < 0 ? 'red' : '') + `;">` + toPercent2(item['YSLC']) + `</span>/<span style="color:` + ((toPercent3(item['ZZFX']) - toPercent3(item['MB_ZZFX'])) < 0 ? 'red' : '') + `;">` + toPercent3(item['ZZFX']) + `万元</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        }
    }
    // 4.判断纵轴指标定义点的位置
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
    /**
     * 判断产业数量决定高度（平台产业展示）
     */
    function diced(item) {
        var style = '';
        style = 'height:'
        switch (item['XJ']) {
            case '1': style += (50 / item['SL5']) + '%;';break;
            case '2': style += (50 / item['SL4']) + '%;';break;
            case '3': style += (50 / item['SL3']) + '%;';break;
            case '4': style += (50 / item['SL2']) + '%;';break;
            case '5': style += (50 / item['SL1']) + '%;';break;
        }
        if (document.body.clientWidth > 1400) {
            return style;
        }else{
            style += 'transform:scale(.8);'
            return style;
        }
    }
    /**
     * 判断X轴居中文本（平台产业展示）
     */
    function centerTxt(val) {
        switch (val) {
            case 'p_on_3': return 'back_1';
            case 'p_on_4': return 'back_2';
            case 'p_on_5': return 'back_3';
            default: return 0;
        }
    }
    // 右侧轮播
    var timer;
    function wfscroll() {
        $('.m_right_scroll .screen').stop().animate({ marginLeft: '-100%' }, 600, "linear", function () {
            var a = $('.m_right_scroll .screen div:first').clone(true);
            scrollagain(a);
            $('.m_right_scroll .screen div:first').remove();
            a.appendTo('.m_right_scroll .screen');
            $('.m_right_scroll .screen').css('marginLeft', '0');
        })
    }
    timer = setInterval(wfscroll, 15000);

    $('.m_right_scroll').hover(function () {
        clearInterval(timer);
    }, function () {
        timer = setInterval(wfscroll, 15000);
    })
    //轮播箭头
    $('.btn_next').click(function () {
        wfscroll();
    })


    /**
     * 绘制产业柱状图，并转为图片
     */
    function createChart(MBData, SJData, toId) {
        // console.log(MBData)
        // console.log(SJData)
        $(toId).empty();
        // console.log(`${toId}_img`)
        $(`${toId}_img`).css('background', 'none');
        if (0) {
            console.log(`${toId}无轮播图数据`);
        } else {
            var xdata = ['首位度', '收入增幅', '利润率'];
            var mbData = MBData;
            var sjData = SJData;
            mbData[0] /= beilv;
            sjData[0] /= beilv;
            $(toId).removeAttr('_echarts_instance_');
            let ec0001_bar = echarts.init($(toId)[0]);
            ec0001_bar.clear();
            ec0001_bar.setOption(opt_bar_vertical);
            ec0001_bar.setOption({
                animation: false,
                color: "#0083b3",
                grid: {
                    left: 0,
                    right: '5%',
                    top: '25%',
                    bottom: '3%'
                },
                // legend: {
                //     show: true,
                //     left: 'right',
                //     data: ['目标', '实际'],
                //     itemHeight: 8 * bodyScale,
                //     itemWidth: 10 * bodyScale,
                //     textStyle: {
                //         fontSize: 10 * bodyScale,
                //         color: '#0083b3'
                //     }
                // },
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
                    max: max,
                    show: false,
                },
                barWidth: '30%',
                barGap: '0%',
                series: [{
                    data: mbData,
                    name: "目标",
                    itemStyle: {
                        normal: {
                            color: '#009966',
                            label: {
                                align: 'center',
                                fontSize: 10 * bodyScale,
                                color: '#fff',
                                show: true,
                                position: 'top',
                                formatter: function (data) {
                                    if (data.dataIndex == 0) {
                                        return toPercent3(data.data * beilv)
                                    } else if (data.dataIndex == 1) {
                                        return toPercent2(data.data)
                                    } else if (data.dataIndex == 2) {
                                        return toPercent(data.data)
                                    }

                                }
                            }
                        }
                    },
                }, {
                    data: sjData,
                    name: "实际",
                    itemStyle: {
                        normal: {
                            color: '#3366ff',
                            label: {
                                align: 'center',
                                fontSize: 10 * bodyScale,
                                color: '#fff',
                                show: true,
                                position: 'top',
                                formatter: function (data) {
                                    var dataValue;
                                    if (data.dataIndex == 0) {
                                        dataValue = toPercent3(data.data * beilv)
                                    } else if (data.dataIndex == 1) {
                                        dataValue = toPercent2(data.data)
                                    } else if (data.dataIndex == 2) {
                                        dataValue = toPercent(data.data)
                                    }
                                    if (data.data < mbData[data.dataIndex]) {
                                        return '{red|' + dataValue + '}'
                                    }
                                    return dataValue;
                                },
                                rich: {
                                    red: {
                                        fontSize: 12 * bodyScale,
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
            //转为图片
            paintEchartsImg(toId, `${toId}_img`);
        }
    }
    //绘制平台柱状图
    function createChart2(MBData, SJData, toId) {
        $(toId).empty();
        // console.log(`${toId}_img`)
        $(`${toId}_img`).css('background', 'none');
        if (0) {
            console.log(`${toId}无轮播图数据`);
        } else {
            var xdata = ['收入增幅', '利润增幅', '利润率'];
            var mbData = MBData;
            var sjData = SJData;
            $(toId).removeAttr('_echarts_instance_');
            let ec0001_bar = echarts.init($(toId)[0]);
            ec0001_bar.clear();
            ec0001_bar.setOption(opt_bar_vertical);
            ec0001_bar.setOption({
                animation: false,
                color: "#0083b3",
                grid: {
                    left: 0,
                    right: '5%',
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
                    max: max,
                    show: false,
                },
                barWidth: '30%',
                barGap: '0%',
                series: [{
                    data: mbData,
                    name: "目标",
                    itemStyle: {
                        normal: {
                            color: '#009966',
                            label: {
                                align: 'center',
                                fontSize: 10 * bodyScale,
                                color: '#fff',
                                show: true,
                                position: 'top',
                                formatter: function (data) {
                                    if (data.dataIndex == 0) {
                                        return toPercent2(data.data)
                                    } else if (data.dataIndex == 1) {
                                        return toPercent2(data.data)
                                    } else if (data.dataIndex == 2) {
                                        return toPercent(data.data)
                                    }
                                }
                            }
                        }
                    },
                }, {
                    data: sjData,
                    name: "实际",
                    itemStyle: {
                        normal: {
                            color: '#3366ff',
                            label: {
                                align: 'center',
                                fontSize: 10 * bodyScale,
                                color: '#fff',
                                show: true,
                                position: 'top',
                                formatter: function (data) {
                                    var dataValue;
                                    if (data.dataIndex == 0) {
                                        dataValue = toPercent2(data.data)
                                    } else if (data.dataIndex == 1) {
                                        dataValue = toPercent2(data.data)
                                    } else if (data.dataIndex == 2) {
                                        dataValue = toPercent(data.data)
                                    }
                                    if (data.data < mbData[data.dataIndex]) {
                                        return '{red|' + dataValue + '}'
                                    }
                                    return dataValue;
                                },
                                rich: {
                                    red: {
                                        fontSize: 12 * bodyScale,
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
            //转为图片
            paintEchartsImg(toId, `${toId}_img`);
        }
    }
    //判断数据中是否有负数
    function negative(MBData, SJData) {
        var a = [3, 0]
        MBData.forEach(function (item, i) {
            if (item < 0) {
                a = [10, 5]
            }
        })
        SJData.forEach(function (item, i) {
            if (item < 0) {
                a = [10, 5]
            }
        })
        return a
    }
    // 重置轮播
    function scrollagain(area) {
        var lunboitem2 = area.find('.block_scroll_screen');
        // console.log(lunboitem2);
        lunboitem2.each(lunbo1);
    }
    function lunbo1(i, item) {
        var timers;
        function wfscroll() {
            $(item).stop().animate({ top: '-100%' }, 500, "linear", function () {
                var a = $(item).children().eq(0).clone(true);
                $(item).children().eq(0).remove();
                a.appendTo(item);
                $(item).css('top', '0');
            })
        }
        timers = setInterval(wfscroll, 4000 + i * 1000);
        $(item).parent().hover(function () {
            clearInterval(timers);
        }, function () {
            timers = setInterval(wfscroll, 4000 + i * 1000);
        })
    }

});