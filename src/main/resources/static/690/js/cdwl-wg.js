$(function () {
    setTimeout(() => {
        setScroll();
    }, 5000);
    let selectData = localStorage.getItem('selectData') ? JSON.parse(localStorage.getItem('selectData')) : '';
    if (!selectData.fromClick) {
        console.log("手动刷新");
        localStorage.setItem('selectData', '')
    } else {
        console.log("鼠标 网格页", selectData);
        selectData.fromClick = '';
        localStorage.setItem('selectData', JSON.stringify(selectData))
    }

    var params = "";    //网格参数
    var chartsParams = "";    //柱状图参数
    var inCode = "";    //产业
    var sqCode = "";    //商圈
    var wgCode = "ALL";    //网格
    var dateIpt = "";    //时间
    var muBiaoData = [];    //690触点网络-网格-本月-目标数据
    var nowDay = '';    //时间控件日期
    var sq_params = '';    //商圈下拉框参数
    var wg_params = '';    //商圈下拉框参数
    var xj_params = '';    //星级参数
    var xd_incode = false;
    var industry_params = "UCode::" + userInfo.userID;    //产业下拉框参数

    //产业接口
    getDateByCommonInterface("690_cdwl_499", industry_params, setIndustryData);

    //查询
    $('#select').click(function () {
        $(".block_tit").text("");
        //获取参数
        inCode = $("#select_indust option:selected").val();
        sqCode = 'ALL';
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        let nowYear = dateIpt.substr(0, 4) - 0;
        let nowMonth = dateIpt.substr(4, 2) - 0;
        let nowDay = dateIpt.substr(6, 2) - 0;
        inCode = $("#select_indust option:selected").val();
        sqCode = $("#select_sq option:selected").val();
        wgCode = $("#select_wg option:selected").val();
        if(inCode == "ABA"){
            xd_incode = true;
        }
        setMonth(nowMonth, nowYear, nowDay);
        getData();
    })
    //后一个月
    $('.right_arrow').click(function () {
        // let selectTime = formatDate($("#dateIpt").val());
        // dateIpt = selectTime.replaceAll("-", "");
        // let year = dateIpt.substr(0, 4);
        // let month = dateIpt.substr(4, 2) - 0 + 1;
        let date = new Date(new Date() - 1000 * 60 * 60 * 24);
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        if (month == 13) {
            month = 1;
            year++;
        }
        //获取日期
        // let day = nowDay;
        $('.right_arrow').css({'display': 'none'});
        $('.left_arrow').css({'display': 'block'});
        $('#thisMonth').text(`${month}月累计预实差`);
        $("#dateIpt").val(year + '-' + month + '-' + day);
        inCode = $("#select_indust option:selected").val();
        sqCode = $("#select_sq option:selected").val();
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        setMonth(month, year, day);
        getData()
    })
    //前一个月
    $('.left_arrow').click(function(){
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
        $('.left_arrow').css({'display': 'none'});
        $('.right_arrow').css({'display': 'block'});
        // $('#thisMonth').text(`${month}月${lastDay}日累计预实差`);
        $("#dateIpt").val(year + '-' + month + '-' + lastDay);
        inCode = $("#select_indust option:selected").val();
        sqCode = $("#select_sq option:selected").val();
        selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        setMonth(month, year, lastDay);
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

    //网格级联
    $('#select_sq').change(function(){
        sqCode = $(this).val();
        wg_params = `inCode::ALL;;sqCode::${sqCode}`;
        getDateByCommonInterface('690_cdwl_wg', wg_params, setWGIndustryData);
    })

    //跳转点击事件
    $('body').on('click', '.pointer', function () {
        let wgname = $(this).data("wgname");
        let wgz = $(this).data("wgz");
        let wgcode = $(this).data("wgcode");
        let incode = $("#select_indust").val();
        let cyname = $("#select_indust option:selected").text();
        let selectTime = formatDate($("#dateIpt").val());
        let time = selectTime.replaceAll("-", "");
        wgname = encodeURI(encodeURI(wgname));
        wgz = encodeURI(encodeURI(wgz));
        cyname = encodeURI(encodeURI(cyname));
        localStorage.setItem('selectData', JSON.stringify({
            select_indust: $("#select_indust").val(),
            select_sq: $("#select_sq").val(),
            select_wg: $("#select_wg").val(),
            dateIpt: formatDate($("#dateIpt").val()),
            left_arrow: $('.left_arrow').css('display'),
            fromClick:true
        }));
        location.href = `/bigSreen/sys/v1/cdwl-wg-2?wgName=${wgname}&&WGZ=${wgz}&&wgCode=${wgcode}&&inCode=${incode}&&time=${time}&&cyName=${cyname}`;
    })

    //请求数据
    function getData() {
        params = "inCode::" + inCode + ";;time::" + dateIpt + ";;wgCode::" + wgCode + ";;sqCode::" + sqCode;
        chartsParams = "inCode::" + inCode + ";;time::" + dateIpt;
        xj_params = "inCode::" + inCode + ";;time::" + dateIpt + ";;xwCode::ALL";
        //获取产业星级数据
        getDateByCommonInterface("690_yhxw_yj_015", xj_params, levels);
        // 触点网络-网格-位置-年
        getDateByCommonInterface('690_cdwl_02', params, setYearData);
        // 触点网络-网格-位置-本月-目标
        getDateByCommonInterface('690_cdwl_05', params, setMonthMBData);
        // 触点网络-网格-位置-本月-预算
        getDateByCommonInterface('690_cdwl_04', params, setMonthYSxData);
        // 触点网络-网格-位置-本月-实际
        getDateByCommonInterface('690_cdwl_06', params, setMonthSJData);
        // 触点网络-网格-位置-T+1月
        getDateByCommonInterface('690_cdwl_07', params, setMonthT1Data);
        // 触点网络-网格-位置-T+2月
        getDateByCommonInterface('690_cdwl_08', params, setMonthT2Data);
        // 触点网络-网格-位置-Q1季度
        getDateByCommonInterface('690_cdwl_09', params, setQuarterT1Data);
        // 触点网络-网格-位置-Q2季度
        getDateByCommonInterface('690_cdwl_10', params, setQuarterT2Data);

        // 触点网络-网格-年-数量占比
        getDateByCommonInterface('690_cdwl_03', params, ltitle_1);
        // 触点网络-网格-本月-预算-数量占比
        getDateByCommonInterface('690_cdwl_13', params, ltitle_2);
        // 触点网络-网格-本月-目标-数量占比
        getDateByCommonInterface('690_cdwl_14', params, ltitle_3);
        // 触点网络-网格-本月-实际-数量占比
        getDateByCommonInterface('690_cdwl_15', params, ltitle_4);
        // 触点网络-网格-T+1月-数量占比
        getDateByCommonInterface('690_cdwl_16', params, ltitle_5);
        // 触点网络-网格-T+2月-数量占比
        getDateByCommonInterface('690_cdwl_17', params, ltitle_6);
        // 触点网络-网格-Q1季度-数量占比
        getDateByCommonInterface('690_cdwl_18', params, ltitle_7);
        // 触点网络-网格-Q2季度-数量占比
        getDateByCommonInterface('690_cdwl_19', params, ltitle_8);
        // 690触点网络-商圈-升级+行业地位
        getDateByCommonInterface('690_cdwl_25', chartsParams, setUpgrade);
        // 触点网络-网格-产业星级评论
        // getDateByCommonInterface('690_cdwl_01', 'inCode::001;;time::20181126;;sqCode::sq001;;wgCode::wg001', levels);
        // 触点网络-网格-弹出窗-年
        // getDateByCommonInterface('690_cdwl_009', 'inCode::001;;time::20181126;;sqCode::sq001', yearinsert);
        // 触点网络-网格-弹出窗-月
        // getDateByCommonInterface('690_cdwl_010', 'inCode::001;;time::20181126;;sqCode::sq001', yearinsert);
        // 690触点网络-商圈-位置-年 参数

        // 触点网络-网格-位置-年柱状图
        getDateByCommonInterface('690_cdwl_x001', chartsParams, chartData);
    }

    // 触点网络-网格-位置-年
    function setYearData(data) {
        //console.log(data)
        var abledata = data['690_cdwl_w02'];
        var arr = grouping(abledata);
        $('.area_1 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-目标
    function setMonthMBData(data) {
        var abledata = data['690_cdwl_w05'];
        var arr = grouping(abledata);
        $('.area_2 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-预算
    function setMonthYSxData(data) {
        console.log(data)
        var abledata = data['690_cdwl_w04'];
        var arr = grouping(abledata);
        $('.area_3 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-实际
    function setMonthSJData(data) {
        var abledata = data['690_cdwl_w06'];
        var arr = grouping(abledata);
        $('.area_4 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-T+1月
    function setMonthT1Data(data) {
        var abledata = data['690_cdwl_w07'];
        var arr = grouping(abledata);
        $('.area_5 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-T+2月
    function setMonthT2Data(data) {
        var abledata = data['690_cdwl_w08'];
        var arr = grouping(abledata);
        $('.area_6 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-Q+1月
    function setQuarterT1Data(data) {
        //console.log(data)
        var abledata = data['690_cdwl_w09'];
        var arr = grouping(abledata);
        $('.area_7 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-位置-本月-Q+2月
    function setQuarterT2Data(data) {
        var abledata = data['690_cdwl_w10'];
        var arr = grouping(abledata);
        $('.area_8 .block_scroll').each(function (i, item) {
            $(item).html(creatDiv(arr[i]));
        });
        // setScroll();
    }

    // 触点网络-网格-年-数量占比
    function ltitle_1(data) {
        var abledata = data['690_cdwl_w03'];
        setSLZB('.area_1', abledata);
    }

    // 触点网络-网格-月-预算-数量占比
    function ltitle_2(data) {
        var abledata = data['690_cdwl_w13'];
        setSLZB('.area_3', abledata);
    }

    // 触点网络-网格-月-目标-数量占比
    function ltitle_3(data) {
        var abledata = data['690_cdwl_w14'];
        setSLZB('.area_2', abledata);
    }

    // 触点网络-网格-月-实际-数量占比
    function ltitle_4(data) {
        var abledata = data['690_cdwl_w15'];
        setSLZB('.area_4', abledata);
    }

    // 触点网络-网格-T+1月-数量占比
    function ltitle_5(data) {
        var abledata = data['690_cdwl_w16'];
        setSLZB('.area_5', abledata);
    }

    // 触点网络-网格-T+2月-数量占比
    function ltitle_6(data) {
        var abledata = data['690_cdwl_w17'];
        setSLZB('.area_6', abledata);
    }

    // 触点网络-网格-Q1季度-数量占比
    function ltitle_7(data) {
        var abledata = data['690_cdwl_w18'];
        setSLZB('.area_7', abledata);
    }

    // 触点网络-网格-Q2季度-数量占比
    function ltitle_8(data) {
        var abledata = data['690_cdwl_w19'];
        setSLZB('.area_8', abledata);
    }

    // 触点网络-网格-星级评论
    /*function levels(data) {
        console.log(data);
        var abledata = data['690_cdwl_w01'];
        $('.s_lin1_name').text(abledata[0]['HYZ']);
        $('.s_lin1_one').html(levelStar(abledata[0]));
        $('.s_lin1_two').html(levelStar(abledata[1]));
        $('.s_lin1_three').html(levelStar(abledata[2]));
    }*/
    //690触点网络-商圈-升级+行业地位
    function setUpgrade (data){
        //console.log(data)
        var abledata = [];
        let abledataArr = ['690_cdwl_t0', '690_cdwl_t2', '690_cdwl_t3', '690_cdwl_t1', '690_cdwl_t4', '690_cdwl_t5', '690_cdwl_t6', '690_cdwl_t7'];
        $('.upgrade').each(function(i, item){
            $(item).html('');
        })
        $('#ZLMB').val('');
        $.each(abledataArr, function(i, item){
            let obj = {SJ: ''};
            if (data[item].length != 0) {
                obj.SJ = data[item][0].SJ;
            }
            abledata.push(obj);
        })
        for (var i = 0; i < 8; i++) {
            let htmls = `
                <span class='jielun_tit flex_center'>升级:</span>
                <input type="text" disabled="disabled" class="comment" value="${abledata[i].SJ ? abledata[i].SJ : ''}">
            `;
            $(`.upgrade_${i}`).html(htmls);
        }
        $('#ZLMB').val(data[`690_cdwl_t0`][0].ZLMB);
    }

    // 节点渲染
    // 定义变量
    var str;
    // 执行与判断
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
                    <div class="pointer" data-wgname=${item['WG_NAME']} data-wgz=${item['WGZ']} data-wgcode=${item['WG_CODE']}>
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['WG_NAME'] + 
                                (item['WGZ'] ? '(' + item['WGZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>目标 : 横` + 
                                toPercent(item['MB_LSZF']) + 
                                `, 纵` + 
                                toPercent(item['MB_CYFGL']) + `/` + 
                                toPercent(item['MB_DBL']) + `/` + 
                                toPercent(item['MB_WXZB']) + 
                            `</div>
                            <div>承接 : 横` + 
                                `<span style="color:` + (toPercent(item['LSZF']) < toPercent(item['MB_LSZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['LSZF']) + 
                                `</span>, 纵` + 
                                `<span style="color:` + (toPercent(item['CYFGL']) < toPercent(item['MB_CYFGL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['CYFGL']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['DBL']) < toPercent(item['MB_DBL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['DBL']) + 
                                `</span>/` + 
                                `<span style="color:` + (toPercent(item['WXZB']) < toPercent(item['MB_WXZB']) ? 'red' : '') + `;">` + 
                                    toPercent(item['WXZB']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        }else if (item['Q1'] || item['Q2']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer" data-wgname=${item['WG_NAME']} data-wgz=${item['WGZ']} data-wgcode=${item['WG_CODE']}>
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['WG_NAME'] + 
                                (item['WGZ'] ? '(' + item['WGZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>目标 : 横` + 
                                toPercent(item['LSZF']) + 
                                `, 纵` + 
                                toPercent(item['CYFGL']) + `/` + 
                                toPercent(item['DBL']) + `/` + 
                                toPercent(item['WXZB']) + 
                            `</div>
                            <div>承接 : 横` + 
                                `<span>` + 
                                    toPercent(item['LSZF']) + 
                                `</span>, 纵` + 
                                `<span>` + 
                                    toPercent(item['CYFGL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['DBL']) + 
                                `</span>/` + 
                                `<span>` + 
                                    toPercent(item['WXZB']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        }else if (item['YEAR']) {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer" data-wgname=${item['WG_NAME']} data-wgz=${item['WGZ']} data-wgcode=${item['WG_CODE']}>
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['WG_NAME'] + 
                                (item['WGZ'] ? '(' + item['WGZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>目标 : 横` + 
                                toPercent(item['MB_LSZF']) + 
                                `, 纵` + 
                                toPercent(item['MB_CYFGL']) + `/` + 
                                toPercent(item['MB_DBL']) + `/` + 
                                toPercent(item['MB_WXZB']) + 
                            `</div>
                            <div>承接 : 横` + 
                                `<span style="color:` + ((item['YS_LSZF']) < (item['MB_LSZF']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_LSZF'] - 0) + 
                                `</span>, 纵` + 
                                `<span style="color:` + ((item['YS_CYFGL']) < (item['MB_CYFGL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_CYFGL']) + 
                                `</span>/` + 
                                `<span style="color:` + ((item['YS_DBL']) < (item['MB_DBL']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_DBL']) + 
                                `</span>/` + 
                                `<span style="color:` + ((item['YS_WXZB']) < (item['MB_WXZB']) ? 'red' : '') + `;">` + 
                                    toPercent(item['YS_WXZB']) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        } else {
            str +=
                `<div class="roll flex_center">
                    <div class="pointer" data-wgname=${item['WG_NAME']} data-wgz=${item['WGZ']} data-wgcode=${item['WG_CODE']}>
                        <div class="p_on ` + xJudge(item) + `" >
                            <span class=` + starColor(item) + `></span>
                        </div>
                        <div>
                            <div class="p_on_tit">` + 
                                item['WG_NAME'] + 
                                (item['WGZ'] ? '(' + item['WGZ'] + ')' : '') + 
                                `<span class="` + (item['HQ_FLAG'] == '1' ? 'p_on_img' : '') + `"></span>
                                <span class="` + rise(item) + `"></span>
                            </div>
                            <div>横 :
                                <span style="color:` + (item['LSZF'] < item['MB_LSZF'] ? 'red' : '') + `;">` + 
                                    toPercent(item['LSZF'] - 0) + 
                                `</span>
                            </div>
                            <div>纵 :
                                <span style="color:` + (item['CYFGL'] < item['MB_CYFGL'] ? 'red' : '') + `;">` + 
                                    toPercent(item['CYFGL'] - 0) + 
                                `</span>/
                                <span style="color:` + (item['DBL'] < item['MB_DBL'] ? 'red' : '') + `;">` + 
                                    toPercent(item['DBL'] - 0) + 
                                `</span>/
                                <span style="color:` + (item['WXZB'] < item['MB_FIVE_STAR_RATE_PRO'] ? 'red' : '') + `;">` + 
                                    toPercent(item['WXZB'] - 0) + 
                                `</span>
                            </div>
                        </div>
                    </div>
                </div>`;
        }
    }
    //柱状图数据
    function chartData(data) {
        createChart(data, '690_cdwl_t5', '#ec00_bar');
        createChart(data, '690_cdwl_t6', '#ec01_bar');
        createChart(data, '690_cdwl_t7', '#ec02_bar');
        createChart(data, '690_cdwl_t0', '#ec03_bar');
        createChart(data, '690_cdwl_t1', '#ec04_bar');
        createChart(data, '690_cdwl_t2', '#ec05_bar');
        createChart(data, '690_cdwl_t3', '#ec06_bar');
        createChart(data, '690_cdwl_t4', '#ec07_bar');
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
        }else{
            var xdata = ['零售增幅', '覆盖率', '达标率'];
            var ydata = [abledata[0][`LSZF`] * 100, abledata[0][`FGL`] * 100, abledata[0][`DBL`] * 100];
            var max = xd_incode ? 1500 : 100;
            if (dataType == '690_cdwl_t6') {
                muBiaoData = [].concat(ydata);
            }
            if (dataType == '690_cdwl_331' || dataType == '690_cdwl_332' || dataType == '690_cdwl_333') {
                barWidth = '35%';
            };
            $(oDiv).removeAttr('_echarts_instance_');
            let ec0001_bar = echarts.init($(oDiv)[0]);
            ec0001_bar.clear();
            ec0001_bar.setOption(opt_bar_vertical);
            ec0001_bar.setOption({
                color: "#0083b3",
                animation:false,
                grid: {
                    left: '-5%',
                    right: '8%',
                    top: '25%',
                    bottom: '5%'
                },
                xAxis: {
                    data: xdata,
                    axisLabel: { //标签名称
                        margin: 2 * bodyScale,
                        fontSize: 12 * bodyScale,
                        color:'#0083b3',
                        interval:0
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#0083b3'
                        }
                    },
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
                                    let dataValue = data.data / 100;
                                    dataValue = toPercent(dataValue);
                                    if (dataType == '690_cdwl_t7' || dataType == '690_cdwl_t0') {
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
                    return $.extend(item, {type: 'bar'})
                }),
            });
            //转为图片
            paintEchartsImg(oDiv, `${oDiv}_img`);
        }
    }

    //初始化
    function initWG() {
        //默认参数，时间
        // let date = new Date(new Date() - 1000 * 60 * 60 * 24);
        let date = new Date(new Date());
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        if(day <= 4){
            month--;
            if(month == 0){
                month = 12;
                year--;
            }
            day = getLastDay(year, month);
        }else {
            day -= 1;
        }
        // let selectTime = formatDate($("#dateIpt").val());
        // dateIpt = selectTime.replaceAll("-", "");
        if (localStorage.getItem('selectData')) {
            $("#dateIpt").val(selectData.dateIpt);
            havDate = selectData.dateIpt.split("-");
            year = havDate[0];
            month = havDate[1];
            day = havDate[2];
        }else {
            // $("#dateIpt").val("2018-11-30");
            // if (day < 10) {
            //     $("#dateIpt").val(year + "-" + month + "-0" + day);
            // } else {
            //     $("#dateIpt").val(year + "-" + month + "-" + day);
            // }
            // $("#dateIpt").val(formatDate(date));
            $("#dateIpt").val(formatDate(`${year}-${month}-${day}`));
        }
        month = parseInt(month);
        $('#thisMonth').text(`${month}月累计预实差`);

        inCode = $("#select_indust").val();
        sqCode = "ALL";
        wgCode = "ALL";
        //下拉框参数sqCode
        var wg_sqCode = "ALL";
        let selectTime = formatDate($("#dateIpt").val());
        dateIpt = selectTime.replaceAll("-", "");
        if (localStorage.getItem('selectData')) {
            inCode = selectData.select_indust;
            sqCode = selectData.select_sq;
            wgCode = selectData.select_wg;
            wg_sqCode = selectData.select_sq;
            var displayType = selectData.left_arrow;
            $('.left_arrow').css('display', displayType);
            if (displayType === 'none') {
                $('.right_arrow').css('display', 'block');
            }
        }else {
            $('.left_arrow').css('display', 'block');
        }
        if(inCode == "ABA"){
            xd_incode = true;
        }
        //拼接参数
        params = `inCode::${inCode};;time::${dateIpt};;wgCode::${wgCode};;sqCode::${sqCode}`;
        chartsParams = `inCode::${inCode};;time::${dateIpt}`;
        sq_params = `inCode::ALL`;
        wg_params = `inCode::ALL;;sqCode::${wg_sqCode}`;
        //发送请求
        getData();
        //触点网络-商圈下拉框
        getDateByCommonInterface('690_cdwl_sq', sq_params, setSQIndustryData);
        //触点网络-网格下拉框
        getDateByCommonInterface('690_cdwl_wg', wg_params, setWGIndustryData);
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
        }else {
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
            if(isYes){
                $("#select_indust option").eq(0).attr('selected', 'selected');
            }
        }
        if (localStorage.getItem('selectData')) {
            console.log('~~~~',localStorage.selectData)
            $("#select_indust").find("option").each(function (index, item) {
                if ($(item).val() === selectData.select_indust) {
                    $("#select_indust").val(selectData.select_indust);
                }
            })
        }

        //初始化
        initWG();
    }

    //封装商圈下拉框
    function setSQIndustryData(data) {
        let abledata = data['690_cdwl_sq'];
        //let sqArr = [];
        if (abledata.length == 0) {
            //sqArr.push({sq_code: "ALL", sq_name: "全部商圈"})
            console.log("无商圈下拉框数据");
        }else{
            // sqArr.push({sq_code: abledata[0].SQ_CODE, sq_name: abledata[0].SQ_NAME});
            // for (let i = 1; i < abledata.length; i++) {
            //     let isYes = true;
            //     for (let j = 0; j < sqArr.length; j++) {
            //         if (abledata[i].SQ_CODE == sqArr[j].sq_code) {
            //             isYes = false;
            //         }
            //     }
            //     if (isYes) {
            //         sqArr.push({sq_code: abledata[i].SQ_CODE, sq_name: abledata[i].SQ_NAME});
            //     }
            // }
            $("#select_sq").append(`<option value="ALL">全部商圈</option>`);
            $.each(abledata, function (index, item) {
                $("#select_sq").append("<option value=" + item.SQ_CODE + ">" + item.SQ_NAME + "</option>")
            });
        }
        if (localStorage.getItem('selectData')) {
            console.log('~~~~', localStorage.selectData)
            $("#select_sq").find("option").each(function (index, item) {
                if ($(item).val() === selectData.select_sq) {
                    $("#select_sq").val(selectData.select_sq);
                }
            })
        }
    }

    //封装网格下拉框
    function setWGIndustryData(data) {
        // let wgArr = [];
        // wgArr.push({wg_code: abledata[0].WG_CODE, wg_name: abledata[0].WG_NAME});
        // for (let i = 1; i < abledata.length; i++) {
        //     let isYes = true;
        //     for (let j = 0; j < wgArr.length; j++) {
        //         if (abledata[i].WG_NAME == wgArr[j]) {
        //             isYes = false;
        //         }
        //     }
        //     if (isYes) {
        //         wgArr.push({wg_code: abledata[i].WG_CODE, wg_name: abledata[i].WG_NAME});
        //     }
        // }
        var abledata = data['690_cdwl_wg'];
        $("#select_wg").html(`<option value='ALL'>全部网格</option>`);
        $.each(abledata, function (index, item) {
            $("#select_wg").append("<option value=" + item.WG_CODE + ">" + item.WG_NAME + "</option>")
        });
        if (localStorage.getItem('selectData')) {
            console.log('~~~~', localStorage.selectData)
            $("#select_wg").find("option").each(function (index, item) {
                if ($(item).val() === selectData.select_wg) {
                    $("#select_wg").val(selectData.select_wg);
                }
            })
        }
    }
    // 用上次的选择结果初始化时间控件
    if (localStorage.getItem('selectData')) {
        $("#dateIpt").val(selectData.dateIpt);
    }

})