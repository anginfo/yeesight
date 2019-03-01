$(function () {
    // setTimeout(() => {
    //     setScroll();
    // }, 3000);
    let selectData = localStorage.getItem('selectData') ? JSON.parse(localStorage.getItem('selectData')) : '';
    if (!selectData.fromClick) {
        console.log("手动刷新");
        localStorage.setItem('selectData', '')
    } else {
        console.log("鼠标 商圈页", selectData);
        selectData.fromClick = '';
        localStorage.setItem('selectData', JSON.stringify(selectData))
    }
    var params = "";    //商圈参数
    var chartsParams = "";    //柱状图参数
    var inCode = "";    //产业
    var sqCode = "";    //商圈
    var wgCode = "ALL";    //网格
    var dateIpt = "";    //日期
    var muBiaoData = [];    //690触点网络-商圈-本月-目标数据
    var nowDay = '';    //时间控件日期
    var wg_params = '';    //网格下拉框参数
    var sq_params = '';    //商圈下拉框参数
    var xj_params = '';    //星级参数
    var industry_params = "UCode::" + userInfo.userID;    //产业下拉框参数
    var max = '';     //柱状图最大值
    var beilv = '';     //柱状图倍率
    var sqDataOpts = '';     //商圈下拉框选项组
    var rotate_index = 2;     //商圈下拉框index
    var rotate_timer;     //商圈轮播timer
    var rotate_control = 1;     //控制是否结束轮播(通过选择具体商圈),0:停止轮播,1:继续轮播,2:触发轮播
    //产业接口
    getDateByCommonInterface("690_cdwl_499", industry_params, setIndustryData);

    //查询
    $('#select').click(function () {
        if (!rotate_control) {
            clearInterval(rotate_timer);
            rotate_control = 2;
        } else if (rotate_control == 2) {
            rotate_control = 1;
            rotate_timer = setInterval(rotate_interval, 4000);
        }
        $(".block_tit").text("");
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        let nowYear = dateIpt.substr(0, 4) - 0;
        let nowMonth = dateIpt.substr(4, 2) - 0;
        let nowDay = dateIpt.substr(6, 2) - 0;
        inCode = $("#select_indust option:selected").val();
        sqCode = $("#select_sq option:selected").val();
        setMonth(nowMonth, nowYear, nowDay);
        getData();
        if (sqCode == 'ALL') {
            setTimeout(() => {
                setScroll();
            }, 3000);
        }
    })
    //后一个月
    $('.right_arrow').click(function () {
        //let selectTime = formatDate($("#dateIpt").val());
        //dateIpt = selectTime.replaceAll("-", "");
        //let year = dateIpt.substr(0, 4);
        //let month = dateIpt.substr(4, 2) - 0 + 1;
        let date = new Date(new Date() - 1000 * 60 * 60 * 24);
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        if (month == 13) {
            month = 1;
            year++;
        }
        //获取日期
        //let day = nowDay;
        $('.right_arrow').css({ 'display': 'none' });
        $('.left_arrow').css({ 'display': 'block' });
        $('#thisMonth').text(`${month}月累计预实差`);
        $("#dateIpt").val(year + '-' + month + '-' + day);
        inCode = $("#select_indust option:selected").val();
        sqCode = $("#select_sq option:selected").val();
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        setMonth(month, year, day);
        getData();
    })
    //前一个月
    $('.left_arrow').click(function () {
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        let year = dateIpt.substr(0, 4);
        let month = dateIpt.substr(4, 2) - 1;
        if (month == 0) {
            month = 12;
            year--;
        }
        let lastDay = getLastDay(year, month);
        //记录日期
        nowDay = dateIpt.substr(6, 2);        
        $('.left_arrow').css({ 'display': 'none' });
        $('.right_arrow').css({ 'display': 'block' });
        // $('#thisMonth').text(`${month}月${lastDay}日累计预实差`);
        $("#dateIpt").val(year + '-' + month + '-' + lastDay);
        inCode = $("#select_indust option:selected").val();
        sqCode = $("#select_sq option:selected").val();
        selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        setMonth(month, year, nowDay);
        getData();
    })

    // //编辑
    // $('#edit').on('click', function () {
    //     if($('.upgrade .comment').attr('disabled')){
    //         $('.upgrade .comment').removeAttr('disabled');
    //     }else {
    //         $('.upgrade .comment').attr('disabled', 'disabled');
    //     }
    // })
    // //提交
    // $('#submit').on('click', function () {
    //     $('.upgrade .comment').attr('disabled', 'disabled');
    // })
    // 控制用户是否需要停止轮播
    $('#select_sq').click(function () {
        rotate_control = 0;
    })
    // 网格级联
    $('#select_sq').change(function () {
        sqCode = $(this).val();
        wg_params = `inCode::ALL;;sqCode::${sqCode}`;
        getDateByCommonInterface('690_cdwl_wg', wg_params, setWGIndustryData);
    })
    $('#select_wg').change(function () {
        localStorage.setItem('selectData', JSON.stringify({
            select_indust: $("#select_indust").val(),
            select_sq: $("#select_sq").val(),
            select_wg: $("#select_wg").val(),
            dateIpt: $("#dateIpt").val(),
            fromClick: true
        }));
        location.href = '/bigSreen/sys/v1/cdwl-wg';
    })

    //点击跳转二级页面
    $('body').on('click', '.pointer', function () {
        localStorage.setItem('selectData', JSON.stringify({
            select_indust: $("#select_indust").val(),
            select_sq: $("#select_sq").val(),
            select_wg: $("#select_wg").val(),
            dateIpt: formatDate($("#dateIpt").val()),
            left_arrow: $('.left_arrow').css('display'),
            fromClick: true
        }));
        let sqname = $(this).data("sqname");
        let sqz = $(this).data("sqz");
        let sqcode = $(this).data("sqcode");
        let incode = $("#select_indust").val();        
        let selectTime = formatDate($("#dateIpt").val());
        let time = selectTime.replaceAll("-", "");
        let cyname = $("#select_indust option:selected").text();
        sqname = encodeURI(encodeURI(sqname));
        sqz = encodeURI(encodeURI(sqz));
        cyname = encodeURI(encodeURI(cyname));
        location.href = `/bigSreen/sys/v1/cdwl-sq-2?sqName=${sqname}&&SQZ=${sqz}&&sqCode=${sqcode}&&inCode=${incode}&&time=${time}&&cyName=${cyname}`;
    })

    //请求数据
    function getData() {
        params = "inCode::" + inCode + ";;time::" + dateIpt + ";;sqCode::" + sqCode;
        chartsParams = "inCode::" + inCode + ";;time::" + dateIpt;
        xj_params = "inCode::" + inCode + ";;time::" + dateIpt + ";;xwCode::ALL";
        //获取产业星级数据
        getDateByCommonInterface("690_yhxw_yj_015", xj_params, levels);
        // 690触点网络-商圈-位置-年
        getDateByCommonInterface('690_cdwl_001', params, setYearData);
        // 690触点网络-商圈-位置-本月-目标
        getDateByCommonInterface('690_cdwl_002', params, setMonthMBData);
        // 690触点网络-商圈-位置-本月-承接
        getDateByCommonInterface('690_cdwl_003', params, setMonthYSxData);
        // 690触点网络-商圈-位置-本月-实际
        getDateByCommonInterface('690_cdwl_004', params, setMonthSJData);
        // 690触点网络-商圈-位置-T+1月
        getDateByCommonInterface('690_cdwl_005', params, setMonthT1Data);
        // 690触点网络-商圈-位置-T+2月
        getDateByCommonInterface('690_cdwl_006', params, setMonthT2Data);
        // 690触点网络-商圈-位置-Q1季度
        getDateByCommonInterface('690_cdwl_007', params, setQuarter1Data);
        // 690触点网络-商圈-位置-Q2季度
        getDateByCommonInterface('690_cdwl_008', params, setQuarter2Data);

        // 690触点网络-商圈-年-数量占比
        getDateByCommonInterface('690_cdwl_011', params, ltitle_1);
        // 690触点网络-商圈-月-承接-数量占比
        getDateByCommonInterface('690_cdwl_012', params, ltitle_2);
        // 690触点网络-商圈-月-目标-数量占比
        getDateByCommonInterface('690_cdwl_013', params, ltitle_3);
        // 690触点网络-商圈-月-实际-数量占比
        getDateByCommonInterface('690_cdwl_014', params, ltitle_4);
        // 690触点网络-商圈-T+1月-数量占比
        getDateByCommonInterface('690_cdwl_015', params, ltitle_5);
        // 690触点网络-商圈-T+2月-数量占比
        getDateByCommonInterface('690_cdwl_016', params, ltitle_6);
        // 690触点网络-商圈-Q1季度-数量占比
        getDateByCommonInterface('690_cdwl_017', params, ltitle_7);
        // 690触点网络-商圈-Q2季度-数量占比
        getDateByCommonInterface('690_cdwl_018', params, ltitle_8);
        // 690触点网络-商圈-升级+行业地位
        getDateByCommonInterface('690_cdwl_030', chartsParams, setUpgrade);
        // 690触点网络-商圈-星级评论1130
        // getDateByCommonInterface('690_cdwl_019', 'inCode::001;;time::20181130', levels);
        // 690触点网络-商圈-弹出窗-年
        // getDateByCommonInterface('690_cdwl_009', 'inCode::001;;time::20181126;;sqCode::sq001', yearinsert);
        // 690触点网络-商圈-弹出窗-月
        // getDateByCommonInterface('690_cdwl_010', 'inCode::001;;time::20181126;;sqCode::sq001', yearinsert);

        //柱状图最大值
        getDateByCommonInterface("690_cdwl+yhxw", chartsParams, setMAX);
        //行业地位
        getDateByCommonInterface('690_cdwl_030', chartsParams, setUpgrade);
    }
    // 690触点网络-商圈-位置-年
    function setYearData(data) {
        //console.log(data)
        var abledata = data['690_cdwl_s001'];
        var arr = grouping(abledata);
        $('.area_1 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-目标
    function setMonthMBData(data) {
        var abledata = data['690_cdwl_s002'];
        //console.log(data)
        var arr = grouping(abledata);
        $('.area_2 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-承接
    function setMonthYSxData(data) {
        //console.log(data)
        var abledata = data['690_cdwl_s003'];
        var arr = grouping(abledata);
        $('.area_3 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-实际
    function setMonthSJData(data) {
        //console.log(data)
        var abledata = data['690_cdwl_s004'];
        var arr = grouping(abledata);
        $('.area_4 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-T+1月
    function setMonthT1Data(data) {
        //console.log(data)
        var abledata = data['690_cdwl_s005'];
        var arr = grouping(abledata);
        $('.area_5 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-T+2月
    function setMonthT2Data(data) {
        var abledata = data['690_cdwl_s006'];
        var arr = grouping(abledata);
        $('.area_6 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-Q+1月
    function setQuarter1Data(data) {
        var abledata = data['690_cdwl_s007'];
        //console.log(data);
        var arr = grouping(abledata);
        $('.area_7 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-位置-本月-Q+2月
    function setQuarter2Data(data) {
        var abledata = data['690_cdwl_s008'];
        var arr = grouping(abledata);
        $('.area_8 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }
    // 690触点网络-商圈-年-数量占比
    function ltitle_1(data) {
        var abledata = data['690_cdwl_s011'];
        setSLZB('.area_1', abledata);
    }

    // 690触点网络-商圈-月-承接-数量占比
    function ltitle_2(data) {
        var abledata = data['690_cdwl_s012'];
        setSLZB('.area_3', abledata);
    }
    // 690触点网络-商圈-月-目标-数量占
    function ltitle_3(data) {
        var abledata = data['690_cdwl_s013'];
        setSLZB('.area_2', abledata);
    }
    // 690触点网络-商圈-月-实际-数量占比
    function ltitle_4(data) {
        var abledata = data['690_cdwl_s014'];
        setSLZB('.area_4', abledata);
    }
    // 690触点网络-商圈-T+1月-数量占比
    function ltitle_5(data) {
        var abledata = data['690_cdwl_s015'];
        setSLZB('.area_5', abledata);
    }
    // 690触点网络-商圈-T+2月-数量占比
    function ltitle_6(data) {
        var abledata = data['690_cdwl_s016'];
        setSLZB('.area_6', abledata);
    }
    // 690触点网络-商圈-Q1季度-数量占比
    function ltitle_7(data) {
        var abledata = data['690_cdwl_s017'];
        setSLZB('.area_7', abledata);
    }
    // 690触点网络-商圈-Q2季度-数量占比
    function ltitle_8(data) {
        var abledata = data['690_cdwl_s018'];
        setSLZB('.area_8', abledata);
    }
    //690触点网络-商圈-升级+行业地位
    function setUpgrade(data) {
        var abledata = [];
        let abledataArr = ['690_cdwl_t0', '690_cdwl_t2', '690_cdwl_t3', '690_cdwl_t1', '690_cdwl_t4', '690_cdwl_t5', '690_cdwl_t6', '690_cdwl_t7'];
        $('.upgrade').each(function (i, item) {
                $(item).html('');
        })
        $('#ZLMB').val('');
        $.each(abledataArr, function (i, item) {
            let obj = { HYDW: '', SJ: '' };
            if (data[item].length != 0) {
                obj.HYDW = data[item][0].HYDW;
                obj.SJ = data[item][0].SJ;
            }
            abledata.push(obj);
        })
        for (var i = 0; i < 8; i++) {
            let htmls = `
                <span class='jielun_tit flex_center'>行业:</span>
                <input type="text" class="comment" disabled="disabled" value="${abledata[i].HYDW ? abledata[i].HYDW : ''}">
                <span class='jielun_tit flex_center'>升级:</span>
                <input type="text" class="comment" disabled="disabled" value="${abledata[i].SJ ? abledata[i].SJ : ''}">
            `;
            $(`.upgrade_${i}`).html(htmls);
        }
        $('#ZLMB').val(data[`690_cdwl_t0`][0].ZLMB);
    }
    // 690触点网络-商圈-星级评论
    // function levels(data) {
    //     var abledata = data['690_cdwl_s019'];
    //     $('.s_lin1_name').text(abledata[0]['HYZ']);
    //     $('.s_lin1_one').html(levelStar(abledata[0]));
    //     $('.s_lin1_two').html(levelStar(abledata[1]));
    //     $('.s_lin1_three').html(levelStar(abledata[2]));
    // }
    
    //左侧柱状图
    //690触点网络-商圈-位置-年-柱状图
    function nextYear(data) {
        createChart(data, '690_cdwl_336', '#ec00_bar', 'MB');
    }
    //690触点网络-商圈-位置-本月-目标-柱状图
    function muBiao(data) {
        createChart(data, '690_cdwl_331', '#ec01_bar', 'MB');
        getDateByCommonInterface('690_cdwl_z003', chartsParams, chengJie);
        getDateByCommonInterface('690_cdwl_z002', chartsParams, shiJi);
    }
    //690触点网络-商圈-位置-本月-承接-柱状图
    function chengJie(data) {
        createChart(data, '690_cdwl_333', '#ec02_bar', 'YS');
    }
    //690触点网络-商圈-位置-本月-实际-柱状图
    function shiJi(data) {
        //console.log(data)
        createChart(data, '690_cdwl_332', '#ec03_bar', 'SJ');
    }

    //轮播柱状图
    //690触点网络-商圈-位置-本月-T+1月-柱状图
    function t1Month(data) {
        createChart(data, '690_cdwl_334', '#ec04_bar', 'YS');
    }
    //690触点网络-商圈-位置-本月-T+2月-柱状图
    function t2Month(data) {
        createChart(data, '690_cdwl_335', '#ec05_bar', 'YS');
    }
    //690触点网络-商圈-位置-本月-Q+1月-柱状图
    function quarter1(data) {
        createChart(data, '690_cdwl_337', '#ec06_bar', 'MB');
    }
    //690触点网络-商圈-位置-本月-Q+2月-柱状图
    function quarter2(data) {
        createChart(data, '690_cdwl_338', '#ec07_bar', 'MB');
    }

    // 节点渲染
    // 定义变量
    var str;

    // 生成对应标签
    function creatDiv(arr) {
        str = '';
        if (arr.length == 0) {
            return str;
        }
        if (arr.length == 1) {
            str = '<div class="block_scroll_screen_off">';
            arr.forEach(creatStr);
            str += '</div>';
        } else if (arr.length > 1) {
            str = '<div class="block_scroll_screen">';
            arr.forEach(creatStr);
            str += '</div>';
        }
        return str;
    }

    // 拼接字符串
    function creatStr(item, i) {
        if (item['MON1'] || item['MON2']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_month" data-sqname=${item['SQ_NAME']} data-sqz=${item['SQZ']} data-sqcode=${item['SQ_CODE']} >
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['SQ_NAME'] + 
                                (item['SQZ'] ? '(' + item['SQZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>目标 : 横` + 
                                `<span>` + 
                                    (item['MB_SWD'] - 0).toFixed(1) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['MB_SRZF']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['MB_LSZF']) + 
                                `</span>` + 
                                `，纵` + 
                                `<span>` + 
                                    (item['MB_CYFGL'] ? toPercent(item['MB_CYFGL']) : '') + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['MB_DBL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    (item['MB_FIVE_STAR_RATE_PRO'] ? toPercent(item['MB_FIVE_STAR_RATE_PRO']) : '') + 
                                `</span>
                            </div>
                            <div>承接 : 横` + 
                                `<span style="color:` + ((item['SWD'] - 0).toFixed(1) < (item['MB_SWD'] - 0).toFixed(1) ? 'red' : '') + `;">` + 
                                    (item['SWD'] - 0).toFixed(1) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['SRZF']) < toPercent(item['MB_SRZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['SRZF']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['LSZF']) < toPercent(item['MB_LSZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['LSZF']) + 
                                `</span>` + 
                                `，纵` + 
                                `<span style="color:` + (toPercent(item['CYFGL']) < toPercent(item['MB_CYFGL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['FGL']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['DBL']) < toPercent(item['MB_DBL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['DBL']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['YS_FIVE_STAR_RATE_PRO']) < toPercent(item['MB_FIVE_STAR_RATE_PRO']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_FIVE_STAR_RATE_PRO']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        } else if (item['Q1'] || item['Q2']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_month" data-sqname=${item['SQ_NAME']} data-sqz=${item['SQZ']} data-sqcode=${item['SQ_CODE']} >
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['SQ_NAME'] + 
                                (item['SQZ'] ? '(' + item['SQZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>目标 : 横` + 
                                `<span>` + 
                                    (item['SWD'] - 0).toFixed(1) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['SRZF']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['LSZF']) + 
                                `</span>` + 
                                `，纵` + 
                                `<span>` + 
                                    toPercent(item['FGL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['DBL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    (item['MB_FIVE_STAR_RATE_PRO'] ? toPercent(item['MB_FIVE_STAR_RATE_PRO']) : '') + 
                                `</span>
                            </div>
                            <div>承接 : 横` + 
                                `<span>` + 
                                    (item['SWD'] - 0).toFixed(1) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['SRZF']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['LSZF']) + 
                                `</span>` + 
                                `，纵` + 
                                `<span>` + 
                                    toPercent(item['FGL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['DBL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['MB_FIVE_STAR_RATE_PRO']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        } else if (item['YEAR']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_year" data-sqname=${item['SQ_NAME']} data-sqz=${item['SQZ']} data-sqcode=${item['SQ_CODE']} data-sqz=${item['SQZ']} >
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['SQ_NAME'] + 
                                (item['SQZ'] ? '(' + item['SQZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                            </div>
                            <div>
                                目标 : 横` + 
                                (item['MB_SWD'] - 0).toFixed(1) + `/` + 
                                toPercent(item['MB_SRZF']) + `/` + 
                                toPercent(item['MB_LSZF']) + 
                                `，纵` + 
                                toPercent(item['MB_CYFGL']) + `/` + 
                                toPercent(item['MB_DBL']) + `/` + 
                                toPercent(item['MB_FIVE_STAR_RATE_PRO']) + 
                            `</div>
                            <div>承接 : 横` + 
                                `<span style="color:` + ((item['YS_SWD'] - 0).toFixed(1) < (item['MB_SWD'] - 0).toFixed(1) ? 'red' : '') + `;">` + 
                                    (item['YS_SWD'] - 0).toFixed(1) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['YS_SRZF']) < toPercent(item['MB_SRZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_SRZF']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['YS_LSZF']) < toPercent(item['MB_LSZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_LSZF']) + 
                                `</span>，` + 
                                `纵<span style="color:` + (toPercent(item['YS_CYFGL']) < toPercent(item['MB_CYFGL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_CYFGL']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['YS_DBL']) < toPercent(item['MB_DBL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_DBL']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['YS_FIVE_STAR_RATE_PRO']) < toPercent(item['MB_FIVE_STAR_RATE_PRO']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_FIVE_STAR_RATE_PRO']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        } else if (item['SJ_FIVE_STAR_RATE_PRO']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_month" data-sqname=${item['SQ_NAME']} data-sqz=${item['SQZ']} data-sqcode=${item['SQ_CODE']} >
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['SQ_NAME'] + 
                                (item['SQZ'] ? '(' + item['SQZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>横 : 
                                <span style="color:` + ((item['SWD'] - 0).toFixed(1) < (item['MB_SWD'] - 0).toFixed(1) ? 'red' : '') + `;">` + 
                                    (item['SWD'] - 0).toFixed(1) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['SRZF']) < toPercent(item['MB_SRZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['SRZF']) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['LSZF']) < toPercent(item['MB_LSZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['LSZF']) + 
                                `</span>
                            </div>
                            <div>纵 : 
                                <span style="color:` + (toPercent(item['CYFGL']) < toPercent(item['MB_CYFGL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['CYFGL']) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['DBL']) < toPercent(item['MB_DBL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['DBL']) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['SJ_FIVE_STAR_RATE_PRO']) < toPercent(item['MB_FIVE_STAR_RATE_PRO']) ? 'red' : '') + `;">` +
                                    toPercent(item['SJ_FIVE_STAR_RATE_PRO']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`
        } else if (item['YS_FIVE_STAR_RATE_PRO']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_month" data-sqname=${item['SQ_NAME']} data-sqz=${item['SQZ']} data-sqcode=${item['SQ_CODE']} >
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                            item['SQ_NAME'] + 
                            (item['SQZ'] ? '(' + item['SQZ'] + ')' : '') + 
                            `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                            <span class="` + rise(item) + `"></span>
                        </div>
                            <div>横 : 
                                <span style="color:` + ((item['SWD'] - 0).toFixed(1) < (item['MB_SWD'] - 0).toFixed(1) ? 'red' : '') + `;">` +
                                    (item['SWD'] - 0).toFixed(1) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['SRZF']) < toPercent(item['MB_SRZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['SRZF']) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['LSZF']) < toPercent(item['MB_LSZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['LSZF']) + 
                                `</span></div>
                            <div>纵 : 
                                <span style="color:` + (item['CYFGL'] < item['MB_CYFGL'] ? 'red' : '') + `;">` + 
                                    (item['CYFGL'] ? toPercent(item['CYFGL']) : '') + 
                                `</span>/
                                <span style="color:` + (toPercent(item['DBL']) < toPercent(item['MB_DBL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['DBL']) + 
                                `</span>/
                                <span style="color:` + (toPercent(item['YS_FIVE_STAR_RATE_PRO']) < toPercent(item['MB_FIVE_STAR_RATE_PRO']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_FIVE_STAR_RATE_PRO']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`
        } else {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer pointer_month" data-sqname=${item['SQ_NAME']} data-sqz=${item['SQZ']} data-sqcode=${item['SQ_CODE']} >
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['SQ_NAME'] + 
                                (item['SQZ'] ? '(' + item['SQZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>横 : 
                                <span>` + 
                                    (item['SWD'] - 0).toFixed(1) + 
                                `</span>/
                                <span>` + 
                                    toPercent(item['SRZF']) + 
                                `</span>/
                                <span>` + 
                                    toPercent(item['LSZF']) + 
                                `</span>
                            </div>
                            <div>纵 : 
                                <span>` + 
                                    (item['CYFGL'] ? toPercent(item['CYFGL']) : '') + 
                                `</span>/
                                <span>` + 
                                    toPercent(item['DBL']) + 
                                `</span>/
                                <span>` + 
                                    (item['MB_FIVE_STAR_RATE_PRO'] ? toPercent(item['MB_FIVE_STAR_RATE_PRO']) : '') + 
                                    (item['SJ_FIVE_STAR_RATE_PRO'] ? toPercent(item['SJ_FIVE_STAR_RATE_PRO']) : '') + 
                                    (item['YS_FIVE_STAR_RATE_PRO'] ? toPercent(item['YS_FIVE_STAR_RATE_PRO']) : '') + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        }
    }
    //获取所有产业收入增幅和零售增幅的最大值
    function setMAX(data) {
        max = (data['690_cdwl_yhxw'][0]['MAX'] - 0).toFixed(2);
        beilv = 5 / max;
        //顶部柱状图
        getDateByCommonInterface('690_cdwl_z006', chartsParams, nextYear);
        getDateByCommonInterface('690_cdwl_z001', chartsParams, muBiao);
        //轮播柱状图
        getDateByCommonInterface('690_cdwl_z004', chartsParams, t1Month);
        getDateByCommonInterface('690_cdwl_z005', chartsParams, t2Month);
        getDateByCommonInterface('690_cdwl_z007', chartsParams, quarter1);
        getDateByCommonInterface('690_cdwl_z008', chartsParams, quarter2);
    }
    /**
     * 绘制柱状图，并转为图片
     * @param   data     请求得到的数据
     * @param   dataType 接口类型
     * @param   oDiv     目标元素id
     * @param   str      数据前缀（目标，预算，实际）
     */
    function createChart(data, dataType, oDiv, str) {
        let abledata = data[dataType];
        let barWidth = "30%";
        $(oDiv).empty();
        $(`${oDiv}_img`).css('background', 'none');
        if (!abledata[0]) {
            console.log(`${oDiv}无轮播图数据`);
        } else {
            var xdata = ['首位度', '收入增幅', '零售增幅'];
            var ydata = [abledata[0][`${str}_SWD`] / beilv, abledata[0][`${str}_SRZF`], abledata[0][`${str}_LSZF`]];
            if (dataType == '690_cdwl_331') {
                muBiaoData = [].concat(ydata);
            };            
            if (dataType == '690_cdwl_331' || dataType == '690_cdwl_332' || dataType == '690_cdwl_333') {
                barWidth = '35%';
            };
            $(oDiv).removeAttr('_echarts_instance_');
            let ec0001_bar = echarts.init($(oDiv)[0]);
            ec0001_bar.clear();
            ec0001_bar.setOption(opt_bar_vertical);
            ec0001_bar.setOption({
                animation: false,
                color: "#0083b3",
                grid: {
                    left: '-5%',
                    right: '8%',
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
                barWidth: barWidth,
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
                                    if (data.dataIndex === 0) {
                                        dataValue = (dataValue * beilv).toFixed(1);
                                    }
                                    if (data.dataIndex != 0) {
                                        dataValue = toPercent(dataValue);
                                    }
                                    if (dataType == '690_cdwl_332' || dataType == '690_cdwl_333') {
                                        if (muBiaoData[data.dataIndex] > data.data) {
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
            //转为图片
            paintEchartsImg(oDiv, `${oDiv}_img`);
        }
    }

    //初始化 
    function initSQ() {
        //默认参数，时间
        // let date = new Date(new Date() - 1000 * 60 * 60 * 24);
        let date = new Date(new Date());
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        if (day <= 4) {
            month--;
            if (month == 0) {
                month = 12;
                year--;
            }
            day = getLastDay(year, month);
        } else {
            day -= 1;
        }
        sqCode = "ALL";
        inCode = $("#select_indust").val();
        //下拉框参数sqCode
        //var wg_sqCode = 'ALL';
        // let selectTime = formatDate($("#dateIpt").val());
        // dateIpt = selectTime.replaceAll("-", "");
        if (localStorage.getItem('selectData')) {
            $("#dateIpt").val(selectData.dateIpt);
            havDate = selectData.dateIpt.split("-");
            year = havDate[0];
            month = havDate[1];
            day = havDate[2];
        } else {
            // if (day < 10) {
            //     $("#dateIpt").val(year + "-" + month + "-0" + day);
            // } else {
            //     $("#dateIpt").val(year + "-" + month + "-" + day);
            // }
            // $("#dateIpt").val(formatDate(date));
            $("#dateIpt").val(formatDate(`${year}-${month}-${day}`));
        }
        $('#thisMonth').text(`${month}月累计预实差`);
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        //判断本地存储
        if (localStorage.getItem('selectData')) {
            inCode = selectData.select_indust;
            sqCode = selectData.select_sq;
            var displayType = selectData.left_arrow;
            $('.left_arrow').css('display', displayType);
            if (displayType === 'none') {
                $('.right_arrow').css('display', 'block');
            }
        } else {
            $('.left_arrow').css('display', 'block');
        }
        //拼接参数
        params = `inCode::${inCode};;time::${dateIpt};;sqCode::${sqCode}`;
        chartsParams = `inCode::${inCode};;time::${dateIpt}`;
        sq_params = `inCode::ALL`;
        //触点网络-商圈下拉框
        getDateByCommonInterface('690_cdwl_sq', sq_params, setSQIndustryData);
        //日期显示
        setMonth(month, year, day);
    };
    //封装产业下拉框数据
    function setIndustryData(data) {
        var selectdata = data['690_cdwl_sw499'];
        var htmls = "";
        var isYes = true;
        if (selectdata.length == 0) {
            console.log('无产业下拉框数据');
        } else {
            $.each(data["690_cdwl_sw499"], function (index, item) {
                if (item.INDUSTRY_CODE == 'BAA') {
                    //默认产业
                    htmls = `<option value=${item.INDUSTRY_CODE} selected="selected">${item.INDUSTRY_NAME}</option>`;
                    isYes = false;
                } else {
                    htmls = `<option value=${item.INDUSTRY_CODE}>${item.INDUSTRY_NAME}</option>`;
                }
                $("#select_indust").append(htmls);
            });
            if (isYes) {
                $("#select_indust option").eq(0).attr('selected', 'selected');
            }
        }
        if (localStorage.getItem('selectData')) {
            //console.log('~~~~',localStorage.selectData)
            $("#select_indust").find("option").each(function (index, item) {
                if ($(item).val() === selectData.select_indust) {
                    $("#select_indust").val(selectData.select_indust);
                }
            })
        }
        //初始化
        initSQ();
    }

    //封装商圈下拉框
    function setSQIndustryData(data) {
        let abledata = data['690_cdwl_sq'];
        sqArr = abledata;
        if (abledata.length == 0) {
            console.log("无商圈下拉框数据");
        } else {
            $.each(abledata, function (index, item) {
                if (item.SQ_NAME == '青岛') {
                    $("#select_sq").prepend("<option value=" + item.SQ_CODE + " selected>" + item.SQ_NAME + "</option>")
                } else {
                $("#select_sq").append("<option value=" + item.SQ_CODE + ">" + item.SQ_NAME + "</option>")
                }
            });
            $("#select_sq").prepend(`<option value="ALL">全部商圈</option>`);
        }
        if (localStorage.getItem('selectData')) {
            console.log('~~~~', localStorage.selectData)
            $("#select_sq").find("option").each(function (index, item) {
                if ($(item).val() === selectData.select_sq) {
                    $("#select_sq").val(selectData.select_sq);
                }
            })
        }
        sqCode = $("#select_sq").val();
        //发送请求
        getData();
        // 单个商圈轮换
        rotateSQData();
    }

    //封装网格下拉框
    function setWGIndustryData(data) {
        var abledata = data['690_cdwl_wg'];
        $("#select_wg").html(`<option value='ALL'>全部网格</option>`);
        $.each(abledata, function (index, item) {
            $("#select_wg").append("<option value=" + item.WG_CODE + ">" + item.WG_NAME + "</option>")
        });
        // if (localStorage.getItem('selectData')) {
        //     console.log('~~~~', localStorage.selectData)
        //     $("#select_wg").find("option").each(function (index, item) {
        //         if ($(item).val() === selectData.select_wg) {
        //             $("#select_wg").val(selectData.select_wg);
        //         }
        //     })
        // }
    }

    //单个商圈轮换
    function rotateSQData() {
        wg_params = `inCode::ALL;;sqCode::${sqCode}`;
        //触点网络-网格下拉框
        getDateByCommonInterface('690_cdwl_wg', wg_params, setWGIndustryData);
        sqDataOpts = document.querySelectorAll('#select_sq option');
        rotate_timer = setInterval(rotate_interval, 4000);
    }
    // 单个商圈轮换定时器函数
    function rotate_interval() {
        if (rotate_index == sqDataOpts.length) {
            rotate_index = 1;
        }
        for (let i = 0; i < sqDataOpts.length; i++) {
            if (sqDataOpts[rotate_index] == sqDataOpts[i]) {
                sqDataOpts[i].setAttribute('selected', true);
            } else {
                sqDataOpts[i].removeAttribute('selected');
            }
        }
        rotate_index++;
        $("#select_sq").change();
        sqCode = $("#select_sq option:selected").val();
        params = "inCode::" + inCode + ";;time::" + dateIpt + ";;sqCode::" + sqCode;
        // 690触点网络-商圈-位置-年
        getDateByCommonInterface('690_cdwl_001', params, setYearData);
        // 690触点网络-商圈-位置-本月-目标
        getDateByCommonInterface('690_cdwl_002', params, setMonthMBData);
        // 690触点网络-商圈-位置-本月-承接
        getDateByCommonInterface('690_cdwl_003', params, setMonthYSxData);
        // 690触点网络-商圈-位置-本月-实际
        getDateByCommonInterface('690_cdwl_004', params, setMonthSJData);
        // 690触点网络-商圈-位置-T+1月
        getDateByCommonInterface('690_cdwl_005', params, setMonthT1Data);
        // 690触点网络-商圈-位置-T+2月
        getDateByCommonInterface('690_cdwl_006', params, setMonthT2Data);
        // 690触点网络-商圈-位置-Q1季度
        getDateByCommonInterface('690_cdwl_007', params, setQuarter1Data);
        // 690触点网络-商圈-位置-Q2季度
        getDateByCommonInterface('690_cdwl_008', params, setQuarter2Data);
        // 690触点网络-商圈-年-数量占比
        getDateByCommonInterface('690_cdwl_011', params, ltitle_1);
        // 690触点网络-商圈-月-承接-数量占比
        getDateByCommonInterface('690_cdwl_012', params, ltitle_2);
        // 690触点网络-商圈-月-目标-数量占比
        getDateByCommonInterface('690_cdwl_013', params, ltitle_3);
        // 690触点网络-商圈-月-实际-数量占比
        getDateByCommonInterface('690_cdwl_014', params, ltitle_4);
        // 690触点网络-商圈-T+1月-数量占比
        getDateByCommonInterface('690_cdwl_015', params, ltitle_5);
        // 690触点网络-商圈-T+2月-数量占比
        getDateByCommonInterface('690_cdwl_016', params, ltitle_6);
        // 690触点网络-商圈-Q1季度-数量占比
        getDateByCommonInterface('690_cdwl_017', params, ltitle_7);
        // 690触点网络-商圈-Q2季度-数量占比
        getDateByCommonInterface('690_cdwl_018', params, ltitle_8);
    }

    // 用上次的选择结果初始化时间控件
    if (localStorage.getItem('selectData')) {
        $("#dateIpt").val(selectData.dateIpt);
    }

})
// setTimeout(function () {
//     $('.block_tit').each(function (index,item) {
//         if($(item).text().indexOf('0/0').length>0){
//             $(item).hide();
//         }
//     })
// },3000)

