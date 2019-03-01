// 通用函数

// 执行与判断
// 1.数据按纵轴指标分组
function grouping(data) {
    var arr = [[], [], [], [], []];
    if (data) {
        data.forEach(function (item, i) {
            switch (item['XJ']) {
                case '5':
                    arr[0].push(item);
                    break;
                case '4':
                    arr[1].push(item);
                    break;
                case '3':
                    arr[2].push(item);
                    break;
                case '2':
                    arr[3].push(item);
                    break;
                case '1':
                    arr[4].push(item);
                    break;
                default:
                    console.log("无星级数据");
                    break;
            }
        });
    }

    return arr;
}

// 4.判断纵轴指标定义点的位置
function xJudge(item) {
    var xclass;
    switch (item['XZZB']) {
        case '1':
            xclass = '';
            break;
        case '2':
            xclass = 'p_on_2';
            break;
        case '3':
            xclass = 'p_on_3';
            break;
        case '4':
            xclass = 'p_on_4';
            break;
        case '5':
            xclass = 'p_on_5';
            break;
    }
    return xclass;
}

// 5.判断星级定义点的颜色
function starColor(item) {
    var starlv = '';
    switch (item['XJ']) {
        case '1':
            starlv = 'one';
            break;
        case '2':
            starlv = 'two';
            break;
        case '3':
            starlv = 'three';
            break;
        case '4':
            starlv = 'four';
            break;
        case '5':
            starlv = 'five';
            break;
    }
    return starlv;
}

// 6.判断增幅趋势图表
function rise(item) {
    var zf = '';
    switch (item['XJ_ZF']) {
        case '1':
            zf = 'sheng';
            break;
        case '0':
            zf = 'ping';
            break;
        case '-1':
            zf = 'jiang';
            break;
        default:
            zf = '';
    }
    return zf;
}

// 星级评价
function levelStar(data) {
    var str = '';
    var stars = '';
    for (let i = 0; i < data['XJ']; i++) {
        stars += '★';
    }
    str = data['PROJECT'] + '&nbsp:<span>' + stars + '</span>';
    return str;
}

//数量占比
function setSLZB(toId, abledata) {
    var sum = 0;
    var count = 0;
    let htmls = '';
    $(`${toId} .block_tit`).each(function (i, item) {
        var sl = 'SL' + (i + 1);
        var zb = 'ZB' + (i + 1);
        if (abledata[0] && abledata[0][sl] !== '0') {
            $(item).html(abledata[0][sl] + '/' + toPercent(abledata[0][zb]));
            let num = abledata[0][zb] - 0;
            sum += num;
            count = i;
        } else {
            $(item).html('');
        }
    });
    htmls = abledata[0][`SL${count + 1}`] + '/' + toPercent(1 - (sum - abledata[0][`ZB${count + 1}`]));
    $(toId).find('.block .block_tit').eq(count).html(htmls);
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
    return (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();//获取当月最后一天日期
}


//设置页面T+1,T+2,Q+1,Q+2显示的日期
function setMonth(month, year, day) {
    let nowYear = year;
    let nowMonth = month;
    let nowDay = day;
    if (month > 9) {
        $("#yearMB").html((nowYear - 0) + 1 + "年目标");
    } else {
        $("#yearMB").html((nowYear - 0) + "年目标");
    }
    $("#thisMonth").html(nowMonth + "月累计预实差");
    if (nowMonth < 10) {
        nowMonth = '0' + nowMonth;
    }
    if (nowMonth == 12) {
        nowMonth = 0;
        nowYear++;
    }
    $('.t1_tit').text(`${nowYear}年${++nowMonth}月`);
    if (nowMonth == 12) {
        nowMonth = 0;
        nowYear++;
    }
    $('.t2_tit').text(`${nowYear}年${nowMonth + 1}月`);
    inCode = $("#select_indust option:selected").val();
    sqCode = $("#select_sq option:selected").val();
    dateIpt = $("#dateIpt").val().replace(/-/g, "");
    //下两个季度
    let Q = getJiduDate(year, month);
    $("#NextJidu1").html(Q[0] + "季度");
    $("#NextJidu2").html(Q[1] + "季度");
    //下两个月
    let NextMonth1 = getNextMonth(year + "-" + month + "-" + day);
    $("#NextMonth1").html(NextMonth1.substr(0, 4) + "年" + Number(NextMonth1.substr(5, 8)) + "月")
    let NextMonth2 = getNextMonth(NextMonth1);
    $("#NextMonth2").html(NextMonth2.substr(0, 4) + "年" + Number(NextMonth2.substr(5, 8)) + "月")
}

//获取下一个季度和下两个季度
function getJiduDate(year, month) {

    var n = parseInt(parseInt(month) / 3);//当前月属于第几季度
    var yu = parseInt(month) % 3;
    if (yu > 0) {
        n++;
    }
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
    }
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
}

//将产业星级数据写入页面
function levels(data) {
    $('#CYZ').text("");
    $('.s_lin1_one').html("");
    $('.s_lin1_two').html("");
    $('.s_lin1_three').html("");
    var abledata = data['690_yhxw_015'];
    $('#CYZ').text(abledata[0]['INDUSTRY_Z']);
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