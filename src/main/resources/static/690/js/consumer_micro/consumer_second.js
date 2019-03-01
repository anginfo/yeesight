var XW_CODE, time;
var params;
var param1;
var time;
$(function () {

    //返回
    $("#back_btn").on('click', function () {

        parent.location.href = "/bigSreen/sys/v1/yhxv?" + param1;
    })
    GetRequest();

    //获取url将小微主小微名称时间写入页面
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
        inCode = theRequest['inCode'];
        XW_CODE = theRequest['XW_CODE'];
        time = theRequest['time'];

        if (theRequest['year']) {
            year = theRequest['year']
            $("#tit").html(year + "年承接差");
            //接口中的参数
            params = "xwCode::" + XW_CODE + ";;time::" + time;
            //获取折线图数据
            getDateByCommonInterface('690_yhxw_l006', params, Allfun);
            //获取右下角表格数据
            getDateByCommonInterface('690_yhxw_l009', params, setTableData2);
            //获取评论框数据
            getDateByCommonInterface('690_yhxw_l010', params, setTextData2);

        } /*else {
            month = theRequest['month']
            $("#tit").html(month + "月承接差");
            inCode = theRequest['inCode'];
            XW_CODE = theRequest['XW_CODE'];
            time = theRequest['time'];
            
            //接口中的参数
            params = "xwCode::" + XW_CODE + ";;time::"+time;
            //获取折线图数据
            getDateByCommonInterface('690_yhxw_l006', params, Allfun);
            //获取右下角表格数据
            getDateByCommonInterface('690_yhxw_l007', params, setTableData);
            //获取评论框数据
            getDateByCommonInterface('690_yhxw_l008', params, setTextData);
        }*/
        //点击返回链接中的参数
        param1 = "inCode=" + inCode + "&&time=" + time + "&&xw_code=ALL";
    }

    //月份-封装评论框数据
    function setTextData(data) {
        //console.log(data)
        var abledata = data['690_yhxw_t1'];
        $("#txt1").val(abledata[0].DMB_SJYC);
        $("#txt2").val(abledata[0].XXJD_YSYC);
        $("#txt3").val(abledata[0].ZZFX_SQMX);
    }
    //月份-封装评论框数据
    function setTextData2(data) {
        // console.log(data)
        var abledata = data['690_yhxw_n'];
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
    //年-将表格数据按行分类
    function setTableData2(data) {
        //console.log(data)
        var abledata = data['690_yhxw_n'];
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
        // trs[7].innerHTML = data['YSC_SJ'];
        // trs[8].innerHTML = data['YSC_DIFF'];
        //trs[7].innerHTML = data['YSC_GCYS'];


    }

    function Allfun(data) {
        shouweidu(data['690_yhxw_t0']);
        shouru(data['690_yhxw_t1']);
        lirunlv(data['690_yhxw_t2']);
    }

    // 纵横匹配-首位度  数据库数据必须按时间排序
    function shouweidu(data) {
        var xdata = [];
        var ydata1 = [];
        var ydata2 = [];
        var allData = [];
        var abledata = data.slice(-6);
        var count = 0;
        var time1 = time.substr(0, 6)
        for (let i = 0; i < abledata.length; i++) {
            if (abledata[i]['DATE_DT'] <= time1 - 0) {
                xdata.push(abledata[i]['DATE_DT'].substr(4, 2));
                ydata1.push(abledata[i]['VALUE']);
                allData.push(abledata[i]['VALUE'])
                ydata2.push("-");
            } else {
                ydata2.push(abledata[i]['VALUE']);
                xdata.push(abledata[i]['DATE_DT'].substr(4, 2) + "E");
                count++;
            };
            if (abledata[i]['VALUE'] !== null) {
                allData.push(abledata[i]['VALUE'])
            }

        }
        ydata2.splice(-(count + 1), 1, ydata1[ydata1.length - 1]);
        let max = allData.sort()[allData.length - 1];
        let ec001_line = echarts.init($("#ec01_line")[0]);
        ec001_line.clear();
        ec001_line.setOption(com_line);
        ec001_line.setOption({
            color: "#0083b3",
            tooltip: {
                show: false
            },
            grid: {
                left: '0%',
                bottom: '2%',
                right: '8%'
            },
            xAxis: {
                name: '月份',
                nameGap: 2 * bodyScale,
                axisLabel: {
                    show: true,
                    interval: 0,
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                },
                data: xdata,
            },
            yAxis: {
                show: false,
                max: max * 2
            },
            series: [{
                data: ydata1,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize: 12 * bodyScale,
                        color: '#f9ae15',
                        formatter: function (data) {
                            if(data.dataIndex == 3){
                                return ''
                            }
                            var a = toPercent3(data.data);
                            return a;
                        }
                    }
                },
            }, {
                data: ydata2,
                // smooth:false,   //关键点，为true是不支持虚线，实线就用true
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1,
                            type: 'dotted'  //'dotted'虚线 'solid'实线
                        }
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize: 12 * bodyScale,
                        color: '#f9ae15',
                        formatter: function (data) {
                            var a = toPercent3(data.data);
                            return a;
                        }
                    }
                },
            }
            ].map(function (item) {
                return $.extend(true, item, com_lineSeries)
            }),
        });
    }

    // 纵横匹配-收入
    function shouru(data) {
        var xdata = [];
        var ydata1 = [];
        var ydata2 = [];
        var abledata = data.slice(-6);

        // var date = new Date();
        // var year = date.getFullYear();

        // var month = date.getMonth() + 1;

        var time1 = time.substr(0, 6)
        var count = 0;
        for (let i = 0; i < abledata.length; i++) {
            if (abledata[i]['DATE_DT'] <= time1) {
                xdata.push(abledata[i]['DATE_DT'].substr(4, 2));
                ydata1.push(abledata[i]['VALUE']);
                ydata2.push("-");
            } else {
                ydata2.push(abledata[i]['VALUE']);
                xdata.push(abledata[i]['DATE_DT'].substr(4, 2) + "E");
                count++;
            };

        }
        ydata2.splice(-(count + 1), 1, ydata1[ydata1.length - 1]);
        let ec001_line = echarts.init($("#ec02_line")[0]);
        ec001_line.clear();
        ec001_line.setOption(com_line);
        ec001_line.setOption({
            color: "#0083b3",
            tooltip: {
                show: false
            },
            grid: {
                left: '0%',
                bottom: '2%',
                right: '8%'
            },
            xAxis: {
                name: '月份',
                nameGap: 2 * bodyScale,
                axisLabel: {
                    show: true,
                    interval: 0,
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                },
                data: xdata,
            },
            yAxis: {
                show: false,
            },
            series: [{
                data: ydata1,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize: 12 * bodyScale,
                        color: '#f9ae15',
                        formatter: function (data) {
                            if(data.dataIndex == 3){
                                return ''
                            }
                            var a = toPercent2(data.data);
                            return a;
                        }
                    }
                },
            }, {
                data: ydata2,
                // smooth:false,   //关键点，为true是不支持虚线，实线就用true
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1,
                            type: 'dotted'  //'dotted'虚线 'solid'实线
                        }
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize: 12 * bodyScale,
                        color: '#f9ae15',
                        formatter: function (data) {
                            var a = toPercent2(data.data);
                            return a;
                        }
                    }
                },
            }
            ].map(function (item) {
                return $.extend(true, item, com_lineSeries)
            }),
        });
    }

    // 纵横匹配-利润率
    function lirunlv(data) {
        var xdata = [];
        var ydata1 = [];
        var ydata2 = [];
        var abledata = data.slice(-6);
        var time1 = time.substr(0, 6)
        var count = 0;
        for (let i = 0; i < abledata.length; i++) {
            if (Number(abledata[i]['DATE_DT']) <= Number(time1)) {
                xdata.push(abledata[i]['DATE_DT'].substr(4, 2));
                ydata1.push(abledata[i]['VALUE']);
                ydata2.push("-");
            } else {
                ydata2.push(abledata[i]['VALUE']);
                xdata.push(abledata[i]['DATE_DT'].substr(4, 2) + "E");
                count++;
            };

        }
        ydata2.splice(-(count + 1), 1, ydata1[ydata1.length - 1]);
        let ec001_line = echarts.init($("#ec03_line")[0]);
        ec001_line.clear();
        ec001_line.setOption(com_line);
        ec001_line.setOption({
            color: "#0083b3",
            tooltip: {
                show: false
            },
            grid: {
                left: '0%',
                bottom: '2%',
                right: '8%'
            },
            xAxis: {
                name: '月份',
                nameGap: 2 * bodyScale,
                axisLabel: {
                    show: true,
                    interval: 0,
                    margin: 2 * bodyScale,
                    fontSize: 12 * bodyScale,
                },
                data: xdata,
            },
            yAxis: {
                show: false,
            },
            series: [{
                data: ydata1,
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize: 12 * bodyScale,
                        color: '#f9ae15',
                        formatter: function (data) {
                            var a = toPercent(data.data);
                            return a;
                        }
                    }
                },
            }, {
                data: ydata2,
                // smooth:false,   //关键点，为true是不支持虚线，实线就用true
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1,
                            type: 'dotted'  //'dotted'虚线 'solid'实线
                        }
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        fontSize: 12 * bodyScale,
                        color: '#f9ae15',
                        formatter: function (data) {
                            if(data.dataIndex == 3){
                                return ''
                            }
                            var a = toPercent(data.data);
                            return a;
                        }
                    }
                },
            }
            ].map(function (item) {
                return $.extend(true, item, com_lineSeries)
            }),
        });
    }
    function toPercent(point) {
        var str = "-"
        if (point != "-") {
            str = Number(point * 100).toFixed(1);
            str += "%";
        }
        return str;

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

    $('.submit').click(function () {
        var stars = [mb_star, sj_star, t1_star, t2_star, t3_star, t4_star, t5_star, t6_star];
    })
})