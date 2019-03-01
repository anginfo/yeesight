//判断x轴位置

var offnub;
offnub = -(document.body.clientHeight * 0.105);
if (document.body.clientHeight > 700) {
    offnub = -(document.body.clientHeight * 0.110);
}
var ec04bottom = '15.6%';
if (document.body.clientHeight < 700) {
    ec04bottom = '14.2%';
} else if (document.body.clientHeight < 900) {
    ec04bottom = '15%';
}
//公共属性
let default_com_line = $.extend(true, {}, com_charts, {
    xAxis: $.extend(true, {}, com_axis, {
        type: 'category',
        splitLine: { //分割线
            show: false,
        },
        axisTick: { //小刻度线
            show: false
        },
        nameGap: 0,
        axisLabel: { margin: 0 }
    }),
    yAxis: $.extend(true, {}, com_axis, {
        type: 'value',
        splitLine: { //分割线
            show: false,
        },
        axisLine: { show: true },
        axisLabel: { show: false },
        nameGap: 10 * bodyScale,
        nameTextStyle: {
            padding: [0, 12 * bodyScale, 0, 0],
        },
        axisTick: { //小刻度线
            show: false
        },
        axisLabel: { margin: 0 }
    }),

    color: colors
});
// 饼图公共属性
let pie_com = {
    title: {
        x: 'center',
        textStyle: {
            color: '#0083b3',
            fontSize: 14
        }
    },
    color: ['#7a7dfe', '#3d3d55', '#fbdb14']
    // 颜色:[实际，其他专利，对手]
}

var defcode = getQueryString("xwCode");
var getTime = getQueryString("time").replace(/-/g, "");
$(function () {
    // 判断不同小微
    if (defcode == '0CA0') {
        $('#ec01_line').css('display', 'none');
        $('.fake').css('display', 'block');
        $('.fake').css('background-image', 'url("/690Project_web/images/tbe.jpg")');
        $('#nav_top_btn').hide();
    } else if (defcode == '03H0') {
        $('#ec01_line').css('display', 'none');
        $('.fake').css('display', 'block');
        $('.fake').css('background-image', 'url("/690Project_web/images/AS.jpg")');
        $('#changeTit').html('专利数');
        $('#ec04_bar').css('display', 'none');
        $('.wtf').css('display', 'block');
        setPie();
    } else if (defcode == '01Z0') {
        $('#ec01_line').css('display', 'none');
        $('.fake').css('display', 'block');
    } else {
        $('.fake').css('display', 'none');
        $('#ec01_line').css('display', 'block');
        $('#bottom_line').css('left', '15%');
    }
    // 获取数据
    getDateByCommonInterface("690_fhxw_ej_0025", `xwCode::${defcode}`, funcALL);
    getDateByCommonInterface("690_fhxw_ej_0026", `xwCode::${defcode}`, setBar02Data);
    getDateByCommonInterface("690_fhxw_ej_0027", `xwCode::${defcode}`, setName);
    getDateByCommonInterface("690_fhxw_er_001", `time::${getTime};;xwCode::${defcode}`, setConclusion);
});

function funcALL(data) {
    // console.log(data);
    // setCreateTime(data)
    setLineData(data);
    setScatterData(data);
    setBar01Data(data);
    setFDGTData(data);
}
/**
 * 设置小微及对手名称、成立时间
 */
function setName(data) {
    // console.log(data);
    var xw_name = '--';
    var xw_create_time = '--';
    var ds_name = '--';
    var ds_create_time = '--';
    var abledata = data['690_fhxw_0029'][0];
    xw_name = abledata.BS_NAME;
    xw_create_time = setDate(abledata.BS_TIME);
    if (abledata.DS_NAME) {
        ds_name = abledata.DS_NAME;
        ds_create_time = setDate(abledata.DS_TIME);
    }
    $('#mb span').add('#sj span, #fhxv_title span, #xw_name, #title_name').text(xw_name);
    $('#xw_create_time').text(xw_create_time);
    $('#ds').add('#ds_name').text(ds_name);
    $('#ds_create_time').text(ds_create_time);
}

function setDate(dateStr) {
    if (dateStr) {
        let year = dateStr.substr(0, 4);
        let month = dateStr.substr(6, 7);
        return month ? `${year}年${month}月` : `${year}年`;
    } else {
        return '未知';
    }
}

/**
 * 设置结论
 */
function setConclusion(data) {
    var abledata = data['690_fhxw_er_001'][0];
    if (abledata) {
        $('.jielun1').text(abledata['FINANCING']);
        $('.jielun2').text(abledata['STSR']);
        $('.jielun3').text(abledata['FDGT']);
    }
}

//设置单位
function setDW(datas) {
    var res = datas.data;
    //   if(Math.abs(res) >= 10000){
    if (defcode == '0CA0') {
        if (datas.name != '对手') {
            var a = Math.round(res / 1000) / 100;
            return a;
        }
    }
    var a = Math.round(res / 1000) / 10;
    return a;
    //   }else{
    //      return res + '万元'
    //   }
}

//封装融资额折线图数据 
function setLineData(data) {
    //console.log(data);
    let seriesData = [{ data: [] }, { data: [] }, { data: [] }];
    let seriesDataArr = [{ data: [] }, { data: [] }, { data: [] }];
    $.each(data["690_fhxw_0025"], function (index, item) {
        seriesData[0].data.push(item.mb_rze);
        seriesDataArr[0].data.push({ "lunci": item.lc, "value": item.mb_rze - 0 });
    });

    $.each(data["690_fhxw_0026"], function (index, item) {
        seriesData[1].data.push(item.sj_rze);
        seriesDataArr[1].data.push({ "lunci": item.lc, "value": item.sj_rze - 0 });
    });

    $.each(data["690_fhxw_0027"], function (index, item) {
        seriesData[2].data.push(item.ds_rze);
        seriesDataArr[2].data.push({ "lunci": item.ds_lc, "value": item.ds_rze });
    });



    let sortArr = ["孵化期", "天使轮", "A轮", "B轮", "C轮", "Pre-IPO"];
    for (let x = 0; x < seriesData.length; x++) {
        let sData = seriesData[x];
        let finalData = [];
        for (let y = 0; y < sortArr.length; y++) {
            let has = false;
            let value = 0;
            $.each(seriesDataArr[x].data, function (index, item) {
                if (item.lunci == sortArr[y]) {
                    has = true;
                    if (item.lunci == "Pre-IPO") {
                        value = [item.value, "独角兽"];
                    } else {
                        value = [item.value, item.lunci];
                    }
                    //console.log(value)
                }
            });
            if (value[1]) {
                finalData.push(value);
            }
        }
        if (finalData[0]) {
            if (finalData[0][0] != "孵化期") {
                finalData.unshift([0, "孵化期"]);
            }
        }
        seriesData[x].data = finalData;
    }
    ec01_line(seriesData);
};
/* 融资额-折线图 */
function ec01_line(seriesData) {
    //console.log(seriesData)
    // if (defcode == '0FA0'||defcode == '0DG0'||defcode == '0QH0') {
    seriesData[0].data.shift();
    seriesData[1].data.shift();
    seriesData[2].data.shift();

    // }
    // 判断label显示位置
    if (defcode == '0AP0' || defcode == '0DG0' || defcode == '0BC0') {
        sj_ps = 'left';
    } else {
        sj_ps = 'top';
    }

    let ec001_line = echarts.init($("#ec01_line")[0]);
    // let { ...lineDefault } = com_lineSeries;
    // lineDefault.label = {
    //     normal: {
    //         show: true,
    //         position: 'top',
    //         color: '#fff'
    //     }
    // }
    ec001_line.clear();
    ec001_line.setOption(default_com_line);
    ec001_line.setOption({
        grid: {
            left: '5%',
            right: '5%',
            bottom: '0%',
            top: 0
        },
        /*tooltip: {
            extraCssText:'width:auto;height:auto;',
            trigger: 'axis',
            formatter: function (datas) {
            var res = datas[0].name + '<br/>'
            for (var i = 0, length = datas.length; i < length; i++) {
              var dataValue = datas[i].data[0];
            	if(dataValue > 10000){
            		var a=  dataValue/10000;
            		res += datas[i].seriesName + '：' 
                    + a.toFixed(2) +'亿' + '<br/>'
            	}else{
            		 res += datas[i].seriesName + '：' 
                     + dataValue +'万元' +'<br/>'
            	}
              
             }
             return res
           } 
       },*/
        xAxis: [
            {
                type: 'value',
                position: 'bottom',
                offset: offnub,
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#15346a'
                    }
                }
            }, {
                type: 'value',
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                },
            },
        ],
        yAxis: {
            type: 'category',
            data: ["孵化期", "天使轮", "A轮", "B轮", "C轮", "独角兽"],
            splitLine: { //分割线
                show: false,
            },
            axisLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false
            }
        },
        series: [{
            name: "目标",
            lineStyle: {
                normal: {
                    width: 3,
                }
            },
            data: seriesData[0].data,
            label: {
                normal: {
                    show: true,
                    position: 'top',
                    color: '#fff'
                }
            }
        }, {
            name: "实际",
            lineStyle: {
                normal: {
                    width: 3,
                }
            },
            data: seriesData[1].data,
            label: {
                normal: {
                    show: true,
                    position: sj_ps,
                    color: '#fff'
                }
            }
        }, {
            name: "对手",
            lineStyle: {
                normal: {
                    width: 3,
                }
            },
            data: seriesData[2].data,
            label: {
                normal: {
                    show: true,
                    position: 'top',
                    color: '#fff'
                }
            }
        }].map(function (item) {
            return $.extend(item, com_lineSeries)
        }),

        /* : seriesData.map(function (item, index) {
            return $.extend(true, {}, com_lineSeries, item);
        }) */
    });

};

//封装市场估值散点图数据 
var seriesDataSD = [{ data: [], bz: [], ys: [], gzbs: [] }, { data: [], bz: [], ys: [], gzbs: [] }, { data: [], bz: [], ys: [], gzbs: [] }];
function setScatterData(data) {
    //console.log('11111111111111111111111');
    $.each(data["690_fhxw_0025"], function (index, item) {
        //console.log(item);
        seriesDataSD[0].data.push(item.mb_gz);
        seriesDataSD[0].ys.push(item.mb_gz_ys);
        seriesDataSD[0].bz.push(item.mb_gz_bz);
        seriesDataSD[0].gzbs.push(item.GZBS);
    });

    $.each(data["690_fhxw_0026"], function (index, item) {
        seriesDataSD[1].data.push(item.sj_gz);
        seriesDataSD[1].ys.push(item.sj_gz_ys)
        seriesDataSD[1].bz.push(item.sj_gz_bz);
        seriesDataSD[1].gzbs.push(item.GZBS);
    });

    $.each(data["690_fhxw_0027"], function (index, item) {
        seriesDataSD[2].data.push(item.ds_gz);
        seriesDataSD[2].ys.push(item.ds_gz_ys)
        seriesDataSD[2].bz.push(item.ds_gz_bz);
        seriesDataSD[2].gzbs.push(item.GZBS);
    });

    ec02_scatter(seriesDataSD);
};
// console.log(seriesDataSD[2].gzbs)
function clearNullArr(arr) {
    for (var i = 0, len = arr.length; i < len; i++) {
        if (!arr[i] || arr[i] == '' || arr[i] === undefined) {
            arr.splice(i, 1);
            len--;
            i--;
        }
    }
    return arr;
}
/*  市场估值-散点图*/
function ec02_scatter(seriesData) {
    // console.log("|||||||||||||||||||||")
    // console.log(seriesData);
    let ec002_scatter = echarts.init($("#ec02_scatter")[0]);
    seriesData[0].data.unshift(null);
    seriesData[1].data.unshift(null);
    seriesData[2].data.unshift(null);
    let allData = clearNullArr(seriesData[0].data.concat(seriesData[1].data).concat(seriesData[2].data));
    let finalData = [];
    let jz = 0;
    for (var x = 0; x < allData.length; x++) {
        jz = jz + parseInt(allData[x] / 10000);
        finalData.push(allData[x] / 10000);
    };
    jz = jz / allData.length;
    var max = Math.max.apply(0, finalData);
    var min = Math.min.apply(0, finalData);
    var bs = Math.abs(max - min);
    // console.log(max + ":" + min + ":" + bs);
    // 判断x轴数值方式
    if (defcode != '01Z0' && defcode != '0CA0') {
        xtype = 'value';
    } else {
        xtype = 'log';
    }
    // 判断label显示位置
    if (defcode == '0CA0' || defcode == '0DG0' || defcode == '0BC0') {
        mb_position = 'right';
    } else {
        mb_position = 'bottom';
    }
    if (defcode == '0CA0' || defcode == '0DG0' || defcode == '03H0' || defcode == '0AP0' || defcode == '0BC0') {
        sj_position = 'left';
    } else {
        sj_position = 'bottom';
    }
    ec002_scatter.clear();
    ec002_scatter.setOption(default_com_line);
    ec002_scatter.setOption({
        grid: {
            left: '11%',
            right: '10%',
            bottom: 0,
            top: 0
        },
        /*tooltip: {
            extraCssText:'width:auto;height:auto;',
            trigger: 'axis',
            formatter: function (datas) {
            var res = datas[0].name + '<br/>'
            for (var i = 0, length = datas.length; i < length; i++) {
            	if(datas[i].data > 10000){
            		var a=  datas[i].data/10000;
            		res += datas[i].seriesName + '：' 
                    + a.toFixed(2)+'亿' + '<br/>'
            	}else{
            		 res += datas[i].seriesName + '：' 
                     + datas[i].data + '<br/>'
            	}
              
             }
             return res
           } 
       },*/
        xAxis: [
            {
                type: xtype,
                // data: [1500, 3000, 4000, 15000, 30000],
                position: 'bottom',
                offset: offnub,
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#15346a',
                    }
                },
                logBase: 10,
                splitNumber: 2,
                axisLabel: {
                    formatter: (val) => {
                        if (Math.abs(val) >= 10000) {
                            return val / 10000 + '亿'
                        }
                        if (val == 0) {
                            return 0
                        }
                        return val + '万'
                    },
                }
            }, {
                type: 'value',
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                },
            },
        ],
        yAxis: {
            type: 'category',
            data: ["孵化期", "天使轮", "A", "B", "C", "独角兽"],
            splitLine: { //分割线
                show: false,
            },
            axisLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            },
            axisTick: {
                show: false
            }
        },
        series: [
            {
                name: '目标',
                type: 'scatter',
                symbolSize: function (data) {
                    return sybalValue(data, bs, min);
                },
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: mb_position,
                            color: '#fff',
                            padding: [0, 0, 5, 0],
                            formatter: getIndex0
                        }
                    }
                },
                data: seriesData[0].data
            }, {
                name: '实际',
                type: 'scatter',
                symbolSize: function (data) {
                    return sybalValue(data, bs, min);
                },
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: sj_position,
                            color: '#fff',
                            padding: [0, 0, 5, 0],
                            formatter: getIndex1
                        }
                    }
                },
                data: seriesData[1].data
            }, {
                name: '对手',
                type: 'scatter',
                symbolSize: function (data) {
                    return sybalValue(data, bs, min);
                },
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'bottom',
                            color: '#fff',
                            padding: [0, 0, 5, 0],
                            formatter: getIndex2
                        }
                    }
                },
                data: seriesData[2].data
            }
        ]
    });
};
function getIndex0(name) {
    // console.log(name)
    var sortArr = ["孵化期", "天使轮", "A", "B", "C", "独角兽"];
    var index = sortArr.indexOf(name.name) - 1
    // console.log(index+":"+name.data);
    var bz = seriesDataSD[0].bz[index];
    var lo = ""
    if (bz == "d" | bz == "D") {
        lo = "$"
    }
    var ys = seriesDataSD[0].ys[index];
    var or = Math.round(ys / 1000) / 10;
    var bs = seriesDataSD[0].gzbs[index] ? `(${(seriesDataSD[0].gzbs[index] - 0).toFixed(0)}X)` : '';
    // console.log(index)
    // console.log(bz)
    // console.log(ys)

    return `${or}${lo}\n${bs}`
}
function getIndex1(name) {
    // console.log(name)
    var sortArr = ["孵化期", "天使轮", "A", "B", "C", "独角兽"];
    var index = sortArr.indexOf(name.name) - 1
    // console.log(index+":"+name.data);
    var bz = seriesDataSD[1].bz[index];
    var lo = ""
    if (bz == "d" | bz == "D") {
        lo = "$"
    }
    var ys = seriesDataSD[1].ys[index]
    var or = Math.round(ys / 1000) / 10;
    var bs = seriesDataSD[0].gzbs[index] ? `(${(seriesDataSD[0].gzbs[index] - 0).toFixed(0)}X)` : '';
    // console.log(index)
    // console.log(bz)
    // console.log(ys)

    return `${or}${lo}\n${bs}`
}
function getIndex2(name) {
    // console.log(name)
    var sortArr = ["孵化期", "天使轮", "A", "B", "C", "独角兽"];
    var index = sortArr.indexOf(name.name) - 1
    // console.log(index+":"+name.data);
    var bz = seriesDataSD[2].bz[index];
    var lo = ""
    if (bz == "d" | bz == "D") {
        lo = "$"
    }
    var ys = seriesDataSD[2].ys[index]
    var or = Math.round(ys / 1000) / 10;
    var bs = seriesDataSD[0].gzbs[index] ? `(${(seriesDataSD[0].gzbs[index] - 0).toFixed(0)}X)` : '';
    // console.log(index)
    // console.log(bz)
    // console.log(ys)

    return `${or}${lo}\n${bs}`
}
function sybalValue(data, bs, min) {
    if (data && bs > 10) {
        var bs = Math.abs(bs),
            data = Math.abs(data) / 10000;
        var baseMin = Math.abs(data - min);

        var radius = Math.abs((baseMin / bs) * 100 / 2);
        // console.log(data+"-"+min+"="+baseMin+":"+bs);
        // console.log(radius);
        if (radius < 10) {
            return 10
        } else {
            return Math.abs(radius)
        }

        // data=Math.abs(data) / 10000;
        // var i=10;
        // if(data/i<1){
        //      if (data<1){
        //         console.log(data);
        //         return Math.log(data)+8;
        //     }else if (data<3){
        //          return Math.log(data)+13;
        //      }else if (data<5){
        //         return Math.log(data)+12;
        //      }else if (data<8){
        //          return Math.log(data)+10;
        //      }else{
        //          return Math.log(data)+9;
        //      }
        // }else if (data/i<3){
        //     return Math.log(data)+(data/i-1)*12;
        // }else if (data/i<5){
        //     return Math.log(data)+(data/i-1)*9;
        // }else if (data/i<7){
        //     return Math.log(data)+(data/i-1)*5.5;
        // }else if (data/i<10){
        //     return Math.log(data)+(data/i-1)*4.5;
        // }else if (data/i<15){
        //     return Math.log(data)+(data/i-1)*3.2;
        // }else{
        //     return Math.log(data)+(data/i-1)*2.2;
        // }
    } else {
        if (defcode == '0DG0') {
            var bs = Math.abs(bs),
                data = Math.abs(data) / 10000;
            var baseMin = Math.abs(data - min);

            var radius = Math.abs((baseMin / bs) * 100 / 2);
            // console.log(data+"-"+min+"="+baseMin+":"+bs);
            // console.log(radius);
            if (radius < 10) {
                return 10
            } else {
                return Math.abs(radius)
            }
        } else {
            return 10
        }
    }
}
//封装pre_time
function setPreTime(data) {
    let dataArr = data;
    let preDataArr = [0];
    for (let i = 1; i < dataArr.length; i++) {
        let sum = 0;
        for (let j = 0; j < i; j++) {
            sum = dataArr[j] - 0 + sum;
        }
        preDataArr.push(sum);
    }
    return preDataArr
}
//封装融资速度柱状图数据 
function setBar01Data(data) {
    //console.log(data)
    let seriesData = [{ data: [] }, { data: [] }, { data: [] }, { data: [] }, { data: [] }, { data: [] }];
    let seriesDataArr = [{ data: [] }, { data: [] }, { data: [] }, { data: [] }, { data: [] }, { data: [] }];
    $.each(data["690_fhxw_0025"], function (index, item) {
        seriesData[0].data.push(item.mb_rzsd);
        seriesData[1].data.push(item.PRE_TIME);
        seriesDataArr[0].data.push({ "lunci": item.lc, "value": item.mb_rzsd - 0 });
        seriesDataArr[1].data.push({ "lunci": item.lc, "value": item.PRE_TIME - 0 });
    });
    seriesData[1].data = [].concat(setPreTime(seriesData[0].data));

    $.each(data["690_fhxw_0026"], function (index, item) {
        seriesData[2].data.push(item.sj_rzsd);
        seriesData[3].data.push(item.PRE_TIME);
        seriesDataArr[2].data.push({ "lunci": item.lc, "value": item.sj_rzsd - 0 });
        seriesDataArr[3].data.push({ "lunci": item.lc, "value": item.PRE_TIME - 0 });
    });

    $.each(data["690_fhxw_0027"], function (index, item) {
        seriesData[4].data.push(item.ds_rzsd);
        seriesData[5].data.push(item.PRE_TIME);
        seriesDataArr[4].data.push({ "lunci": item.ds_lc, "value": item.ds_rzsd - 0 });
        seriesDataArr[5].data.push({ "lunci": item.ds_lc, "value": item.PRE_TIME - 0 });
    });
    //console.log(seriesData)
    // console.log(seriesDataArr)

    let sortArr = ["孵化期", "天使轮", "A轮", "B轮", "C轮", "独角兽"];
    //console.log(seriesDataArr)
    for (let x = 0; x < seriesData.length; x++) {
        let sData = seriesData[x];
        let finalData = [];
        for (let y = 0; y < sortArr.length; y++) {
            let has = false;
            let value = 0;
            $.each(seriesDataArr[x].data, function (index, item) {
                //console.log(item)
                if (item.lunci == sortArr[y]) {
                    has = true;
                    value = item.value;
                }
            });
            finalData.push(value);
        }
        seriesData[x].data = finalData;
    }
    ec03_bar(seriesData);
};
/* 融资速度-柱状图*/
function ec03_bar(seriesData) {

    let ec003_bar = echarts.init($("#ec03_bar")[0]);
    ec003_bar.clear();
    ec003_bar.setOption(default_com_line);

    ec003_bar.setOption({
        grid: {
            left: '1%',
            right: '9%',
            bottom: 0,
            top: 0
        },
        /* tooltip: {
             extraCssText:'width:auto;height:auto;',
             trigger: 'axis',
             formatter: function (datas) {
             var res = datas[0].name + '<br/>'
             for (var i = 1, length = datas.length; i < length; i=i+2) {
                      res += datas[i].seriesName + '：' 
                      + datas[i].data  +'个月'+ '<br/>'
             
               
              }
              return res
            } 
        },*/
        yAxis: [
            {
                type: 'category',
                data: ["孵化期", "天使轮", "A轮", "B轮", "C轮", "独角兽"],
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                },
                axisLabel: {
                    show: false,
                },
                axisTick: {
                    show: false
                },
            },
        ],
        xAxis: [
            {
                type: 'value',
                position: 'bottom',
                offset: offnub,
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#15346a'
                    }
                },
            }, {
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                }
            }
        ],
        series: [
            {

                type: 'bar',
                stack: '目标',
                itemStyle: {
                    normal: {
                        label: {
                            show: false,
                            position: 'insideRight',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: function (data) {
                                if (data.data != 0) {
                                    return data.data;
                                } else {
                                    return '';
                                }
                            }
                        }
                    }
                },
                barWidth: "15%",
                data: seriesData[1].data,
                color: '#3d3d55'
            },
            {
                name: '目标',
                type: 'bar',
                stack: '目标',

                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'right',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: function (data) {
                                if (data.data != 0) {
                                    return data.data;
                                } else {
                                    return '';
                                }
                            }
                        }
                    }
                },
                barWidth: "15%",
                data: seriesData[0].data
            },
            {

                type: 'bar',
                stack: '实际',

                itemStyle: {
                    normal: {
                        label: {
                            show: false,
                            position: 'insideRight',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: function (data) {
                                if (data.data != 0) {
                                    return data.data;
                                } else {
                                    return '';
                                }
                            }
                        }
                    }
                },
                barWidth: "15%",
                data: seriesData[3].data,
                color: '#3d3d55'
            },
            {
                name: '实际',
                type: 'bar',
                stack: '实际',

                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'right',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: function (data) {
                                if (data.data != 0) {
                                    return data.data;
                                } else {
                                    return '';
                                }
                            }
                        }
                    }
                },
                barWidth: "15%",
                data: seriesData[2].data
            },
            {
                type: 'bar',
                stack: '对手',
                itemStyle: {
                    normal: {
                        label: {
                            show: false,
                            position: 'insideRight',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: function (data) {
                                if (data.data != 0) {
                                    return data.data;
                                } else {
                                    return '';
                                }
                            }
                        }
                    }
                },
                barWidth: "15%",
                data: seriesData[5].data,
                color: '#3d3d55'
            },
            {
                name: '对手',
                type: 'bar',
                stack: '对手',

                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'right',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: function (data) {
                                if (data.data != 0) {
                                    return data.data;
                                } else {
                                    return '';
                                }
                            }
                        }
                    }
                },
                barWidth: "15%",
                data: seriesData[4].data
            },
        ]
    });
};

//封装收入柱状图数据 
function setBar02Data(data) {
    // console.log("!!!!!!!!")
    // console.log(data);
    // setXWName(data)
    let seriesData = [{ data: [] }, { data: [] }, { data: [] }, { data: [] }];
    let yearData = [];
    let beilvData = [];
    let barData = data["690_fhxw_0028"];
    $.each(barData, function (index, item) {
        if (item.FLAG == '目标') {
            seriesData[0].data.push(item.MB_SR - 0);
            yearData.push(item.YEAR);
            beilvData.push(item.MB_SRBS);
        } else if (item.FLAG == '实际') {
            seriesData[1].data.push(item.MB_SR - 0);
            yearData.push(item.YEAR);
            beilvData.push(item.MB_SRBS);
        } else if (item.FLAG == '对手') {
            seriesData[2].data.push(item.MB_SR - 0);
            yearData.push(item.YEAR);
            beilvData.push(item.MB_SRBS);
        }
    });
    // seriesData[0].data = [100000,20000,300000,40000];
    var n = Math.max(seriesData[0].data.length, seriesData[1].data.length, seriesData[2].data.length);
    var arr = new Array(n);
    for (var i = 0; i < arr.length; i++) {
        arr[i] = new Array(3);    //每行有3列
    }
    for (let x = 0; x < n; x++) {
        for (let y = 0; y < 3; y++) {
            arr[x][y] = seriesData[y].data[x];
        }
    }
    ec04_bar(arr, yearData, beilvData);
};
/* 收入-堆积柱状图*/
function ec04_bar(seriesData, yearData, beilvData) {
    if (defcode == '0CA0') {
        for (let i = 0; i < seriesData.length; i++) {
            seriesData[i][0] = seriesData[i][0] * 10;
            seriesData[i][1] = seriesData[i][1] * 10;
        }
    }
    function setB() {
        let a = [];
        for (let i = 0; i < seriesData.length; i++) {
            a.push({
                name: "目标",
                type: 'bar',
                stack: 'all',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'right',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: setDW
                        },
                        color: i >= 3 ? "rgba(26,179,13,.25)" : "#1ab20f",
                        barBorderWidth: i >= 3 ? 2 : 0,
                        borderType: "dashed",//dashed,dotted
                        barBorderColor: "#1ab20f",
                    }
                },
                data: [seriesData[i][0], null, null],

            },
                {
                    name: "目标",
                    type: 'bar',
                    stack: 'all',
                    itemStyle: {
                        normal: {
                            label: {
                                show: false,
                            },
                            color: "#FFFFFF",
                            barBorderWidth: 2,
                            borderType: "solid",
                            barBorderColor: "#FFFFFF",
                            opacity: i >= 3 ? 0 : 1,
                        }
                    },
                    barMinHeight: 1,
                    data: [1, null, null],
                })
        }
        for (let i = 0; i < seriesData.length; i++) {
            a.push({
                name: "实际",
                type: 'bar',
                stack: 'all',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'right',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: setDW
                        },
                        color: "#797efe",
                    }
                },
                data: [null, seriesData[i][1], null],

            },
                {
                    name: "实际",
                    type: 'bar',
                    stack: 'all',
                    itemStyle: {
                        normal: {
                            label: {
                                show: false,
                            },
                            color: "#FFFFFF",
                            barBorderWidth: 2,
                            borderType: "solid",
                            barBorderColor: "#FFFFFF",
                            opacity: i >= 3 ? 0 : 1,
                        }
                    },
                    barMinHeight: 1,
                    data: [null, 1, null],
                })
        }
        for (let i = 0; i < seriesData.length; i++) {
            a.push({
                name: "对手",
                type: 'bar',
                stack: 'all',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'right',
                            color: '#fff',
                            padding: [5, 0, 5, 0],
                            formatter: setDW
                        },
                        color: "#fbdb14",
                    }
                },
                data: [null, null, seriesData[i][2]],
            },
                {
                    name: "对手",
                    type: 'bar',
                    stack: 'all',
                    itemStyle: {
                        normal: {
                            label: {
                                show: false,
                            },
                            color: "#FFFFFF",
                            barBorderWidth: 2,
                            borderType: "solid",
                            barBorderColor: "#FFFFFF",
                            opacity: i >= 3 ? 0 : 1
                        }
                    },
                    barMinHeight: 1,
                    data: [null, null, 1],
                })
        }
        return a;

    }

    let setA = setB();
    // for (let i = 0; i < setA.length; i++) {
    //     for (let j = 0; j < setA[i].data.length; j++) {
    //         setA[i].data[j] = setA[i].data[j]/1000;
    //     }
    // }
    let ec004_bar = echarts.init($("#ec04_bar")[0]);
    ec004_bar.clear();
    ec004_bar.setOption(opt_bar_vertical);
    ec004_bar.setOption({
        grid: {
            left: '0%',
            bottom: ec04bottom,
            right: '8%',
            top: 0
        },
        /*tooltip: {
            extraCssText:'width:auto;height:auto;',
            trigger: 'axis',
            formatter: function (datas) {
            var res = datas[0].name + '<br/>';
            //console.log(yearData.length);
             for (var i = 0, length = datas.length; i < length; i++) {
            	
            		 if(datas[i].value < 9999){
            			
            			 res +=yearData[i] + '：' 
                         + datas[i].value  +'万元,倍率'+beilvData[i]+ '<br/>'
            		
                  	}else if(datas[i].value>9999) {
                  		var a=  datas[i].data/10000;
                  		res += yearData[i] + '：' 
                          + a.toFixed(2)+'亿,倍率:'+beilvData[i]+ '<br/>'
                  	} else if(datas[i].value == 0){
                  		res = "";
                  	}  

            }
             return res
           } 
       },*/
        xAxis: [
            {
                type: 'category',
                data: ['目标', '实际', '对手'],
                position: 'bottom',
                splitLine: { //分割线
                    show: false,
                },
                axisLine: {
                    show: false,
                    lineStyle: {
                        color: '#15346a'
                    }
                },
            }
        ],
        yAxis: [{
            splitLine: { //分割线
                show: false,
            },
            axisLine: {
                show: false,
            },
            axisLabel: {
                show: false,
            }
        },
        {
            type: 'category',
            data: ['2016年', '2017年', '2018年', '2019年', ''],
            nameTextStyle: {
                fontSize: 12
            },
            position: 'left',
            show: true,
            splitLine: { //分割线
                show: false,
            },
            axisLine: {
                show: false,
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                show: true,
                position: 'right'
            }
        }],
        series: setA,
    });
};

//封装分段跟投数据
function setFDGTData(data) {
    //   console.log(data)
    let seriesData = [];
    $.each(data["690_fhxw_0026"], function (index, item) {
        let seriesDataElement = { lc: item.lc, value: item.FDGT, subvalue: item.FDGT_STR };
        if (seriesDataElement.subvalue == null) {
            seriesDataElement.subvalue = '';
        }
        seriesData.push(seriesDataElement);
    });
    setFDGT(seriesData);
}
//分段跟投
function setFDGT(seriesData) {
    let dataArr = seriesData;
    let lcArr = ['新冒出', '天使轮', 'A轮', 'B轮', 'C轮', '独角兽'];
    //console.log(seriesData);
    $.each(dataArr, function (index, item) {
        switch (item.lc) {
            case '新冒出':
                $('#xmc').html(item.value + '<br/><span style="color:red;">' + item.subvalue + '</span>');
                break;
            case '天使轮':
                $('#tsl').html(item.value + '<br/><span style="color:red;">' + item.subvalue + '</span>');
                break;
            case 'A轮':
                $('#al').html(item.value + '<br/><span style="color:red;">' + item.subvalue + '</span>');
                break;
            case 'B轮':
                $('#bl').html(item.value + '<br/><span style="color:red;">' + item.subvalue + '</span>');
                break;
            case 'C轮':
                $('#cl').html(item.value + '<br/><span style="color:red;">' + item.subvalue + '</span>');
                break;
            case '独角兽':
                $('#djs').html(item.value + '<br/><span style="color:red;">' + item.subvalue + '</span>');
                break;
            default:
                break;
        }
    })

}
/**
 * 设置专利数饼图
 */
function setPie(data) {
    // 饼图 1
    let pie_1 = echarts.init($("#pie_1")[0]);
    pie_1.clear();
    pie_1.setOption(pie_com);
    pie_1.setOption({
        title: {
            text: 'AS'
        },
        series: [
            {
                name: 'AS专利数',
                type: 'pie',
                radius: '60%',
                center: ['50%', '58%'],
                data: [
                    { value: 47, name: '发明专利' },
                    { value: 85, name: '其他专利' }
                ],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{b}\n{c}-{d}%',
                        textStyle: {
                            color: "#fff",
                            fontSize: 10 * bodyScale
                        }
                    },
                    // emphasis: {
                    //     show: true,
                    //     textStyle: {
                    //         fontSize: '30',
                    //         fontWeight: 'bold'
                    //     }
                    // }
                },
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    });
    // 饼图 2
    let pie_2 = echarts.init($("#pie_2")[0]);
    pie_2.clear();
    pie_2.setOption(pie_com);
    pie_2.setOption({
        title: {
            text: '光峰'
        },
        series: [
            {
                name: '光峰专利数',
                type: 'pie',
                radius: '70%',
                center: ['50%', '52%'],
                data: [
                    { value: 100, name: '其他专利', itemStyle: { color: '#3d3d55' } },
                    { value: 400, name: '发明专利', itemStyle: { color: '#fbdb14' } },
                ],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{b}\n{c}-{d}%',
                        textStyle: {
                            color: "#fff",
                            fontSize: 10 * bodyScale
                        }
                    },
                    // emphasis: {
                    //     show: true,
                    //     textStyle: {
                    //         fontSize: '30',
                    //         fontWeight: 'bold'
                    //     }
                    // }
                },
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    });
}
/*监控屏幕变化 刷新数据*/
// window.onresize = function () {
//     // window.location.reload();
//     setTimeout("location.reload();", 500);
// }