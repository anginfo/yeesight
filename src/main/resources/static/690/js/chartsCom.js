let bodyScale = 1;
let colors = [
    // '#fdc856',
    // '#ff6475',
    // '#40c8fa',
    // '#333fed',
    '#1ab20f',
    '#797efe',
    '#fbdb14',
    // '#3d3d55'
];
let c_blueMain = '#15346a';
let [cTextWeek, cSplitLine, cTextLine] = ["#0083b3", "#15346a", "#0379a7"];
let barWidth = 12 * bodyScale;
let lineStyle = {
    shadowColor: 'rgba(255, 255, 255, 1)',
    shadowBlur: 4 * bodyScale
};

//所有图表的公共属性
let com_charts = {
    color: colors,
    textStyle: {
        fontFamily: 'PingFang SC, microsoft yahei,微软雅黑, sans-serif',
        color: cTextLine,
        fontSize: 10 * bodyScale
    },
    legend: {
        itemWidth: 24 * bodyScale,
        itemHeight: 16 * bodyScale,
        textStyle: {
            fontSize: 18 * bodyScale,
            color: '#fff',
        },
        show: false
    },
    grid: {
        bottom: "10%",//底边留空
        top: '10%',
        left: '10%',
        right: '0%',
        containLabel: true  //是否包含在内
    },
};

//直角坐标系坐标轴
let com_axis = {
    axisLabel: { //标签名称
        color: cTextLine,
        margin: 8 * bodyScale,
        fontSize: 12 * bodyScale,
        // fontWeight: 'bold'
    },
    nameTextStyle: { //坐标轴名称
        fontSize: 10 * bodyScale,
        color: cTextLine
    },
    nameGap: 10 * bodyScale, //坐标轴名称距离
    axisTick: { //小刻度线
        show: false
    },
    axisLine: { //坐标轴
        lineStyle: {
            color: cSplitLine
        }
    },
    splitLine: { //分割线
        show: false,
        lineStyle: {
            color: cSplitLine,
            type: 'dashed'
        }
    },
    boundaryGap: true
};

//圆环图 series里的属性设置
let circle_series_label = {
    normal: {
        show: true,
        fontSize: 12 * bodyScale
    },
    emphasis: {
        show: true,
        textStyle: {
            fontSize: 15 * bodyScale,
            fontWeight: 'normal'
        }
    }
};
let circle_series_labline = {
    normal: {
        show: true,
    }

};
let axisLine_Y = {
    lineStyle: {
        color: cSplitLine
    }
};
let splitLine = {
    show: false
};
//横条图公共属性
//opt_bar_horizon写在里面是为了不让后面的对象覆盖opt_com
let opt_bar_horizon = $.extend(true, {}, com_charts, {
    legend: {
        show: false
    },
    xAxis: $.extend(true, {}, com_axis, {
        type: 'value',
        splitLine: { //分割线
            show: false,
        },

    }),
    yAxis: $.extend(true, {}, {}, {
        type: 'category',
        splitLine: { //分割线
            show: false,
            lineStyle: {
                color: cSplitLine,
                type: 'dashed'
            }
        },
    }),
    barWidth: '20%'
});

//竖条图公共属性
let opt_bar_vertical = $.extend(true, {}, com_charts, {
    legend: {
        show: false
    },
    xAxis: $.extend(true, {}, com_axis, {
        boundaryGap: true,
        type: 'category'
    }),
    yAxis: $.extend(true, {}, com_axis, {
        type: 'value'
    }),
    barWidth: '40%'
    //这里写此类图表其他属性
});

//折线图公共属性
let com_line = $.extend(true, {}, com_charts, {
    xAxis: $.extend(true, {}, com_axis, {
        type: 'category',
        splitLine: { //分割线
            show: false,
        },
        axisTick: { //小刻度线
            show: false
        },
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
    }),

    //这里写此类图表其他属性
    tooltip: {
        trigger: 'axis',
        extraCssText:'width:150px;height:150px;'
    },
    color: colors
});
let com_lineSeries = {
    type: 'line',
    smooth: true,
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
//饼图公共属性
let com_pie = $.extend(true, {}, com_charts, {
    legend: {
        show: false,
        orient: 'vertical',
        right: '3%',
        top: 'middle'
    },

});
let com_circle = $.extend(true, {}, com_pie, {});
let com_circleSeries = {
    type: 'pie',
    radius: ['45%', '65%'],
}
//散点图公共属性
let opt_scatter = $.extend(true, {}, com_charts, {
    xAxis: $.extend(true, {}, com_axis, {
        type: 'category'
    }),
    yAxis: $.extend(true, {}, com_axis, {
        type: 'category'
    }),

    //这里写此类图表其他属性

});

//雷达图公共属性
let opt_radar = $.extend(true, {}, {
    legend: {
        itemWidth: 7 * bodyScale,
        itemHeight: 7 * bodyScale,
        textStyle: {
            fontSize: 10 * bodyScale,
            color: cTextWeek, //图例白色,全局样式不能影响到
        },
        top: '2%',
        left: 'right',
        orient: 'vertical'
    },
    tooltip: {
        axisPointer: {
            label: {
                backgroundColor: '#6a7985'
            }
        },
        textStyle: {
            align: 'left',
            fontFamily: 'PingFang SC, microsoft yahei,微软雅黑, sans-serif',
            // fontSize: 15 * bodyScale
        }
    },
    radar: {
        center: ['50%', '58%'],
        // shape: 'circle',
        name: {
            textStyle: {
                color: cTextLine,
                fontSize: 12 * bodyScale
                // backgroundColor: '#999',
                // borderRadius: 3,
                // padding: [3, 5]
            }
        },

        splitArea: {
            areaStyle: {
                color: 'rgba(0,0,0,0)'
            }
        },
        axisLine: {
            lineStyle: {
                color: cTextLine
            }
        },
        // splitLine: {
        //     lineStyle: {
        //         color: cSplitLine,
        //         type: 'dashed'
        //     }
        // }
    },
});
//圆环图
let opt_circle = $.extend(true, {}, com_charts, {
    tooltip: {
        // formatter: "{a} <br/>{b}: {c} ({d}%)"
        trigger: 'axis',
        axisPointer: {
            //type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        },
        textStyle: {
            align: 'left'
        },
    },
    color: colors,
    legend: {
        center: 'center',
        textStyle: {
            color: cSplitLine,
            fontSize: 10 * bodyScale
        },
        top: '2%'
    }

});

//倒三角
let opt_funnel = $.extend(true, {}, com_charts, {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            // type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        },
        textStyle: {
            align: 'left'
        },
        formatter: "{a} <br/>{b} : {c}%"
    },
    calculable: true,
    legend: {
        show: false
    },
})


function scatterSymbolSize(data) {
    return Math.sqrt(data[2]) * bodyScale / 2;
}

function lineAreaStyle(index) {
    return {
        color: {
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [{
                offset: 0, color: colors[index] // 0% 处的颜色
            }, {
                offset: 1, color: '#00adef11' // 100% 处的颜色
            }]
        }
    }

}
