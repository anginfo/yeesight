// 时间显示
var spans = document.querySelectorAll('.time span');
var time;
var industryCode;
var guZhiRongsu;
setInterval(function () {
    var date = new Date();
    // console.log(date);
    var year = date.getFullYear();
    // console.log(year);
    // month 0-11 获取的月份时 0 - 11
    var month = date.getMonth() + 1;
    // console.log(month);
    var day = date.getDate();
    // console.log(day);
    var hour = date.getHours();
    // console.log(hour);
    var minute = date.getMinutes();
    // console.log(minute);
    var second = date.getSeconds();
    // console.log(second);
    var millisecond = date.getMilliseconds();
    // console.log(millisecond);

    var timeArr = [];
    timeArr.push(year);
    timeArr.push(month);
    timeArr.push(day);
    timeArr.push(hour);
    timeArr.push(minute);
    timeArr.push(second);

    for (let i = 0; i < spans.length; i++) {
        if (timeArr[i] < 10) {
            spans[i].innerHTML = '0' + timeArr[i];
        } else {
            spans[i].innerHTML = timeArr[i];
        }
    }
}, 1000)

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return "";
}
//左上按钮组
$("#back_btn").on('click', function () {
    // history.back(-1);
    location.href = `/bigSreen/sys/v1/fhxv?time=${time}&industryCode=${industryCode}&guZhiRongsu=${guZhiRongsu}&xwset=${xwset}`;
})
$("#nav_top_btn").on('click', function () {
    //获取时间
    var date = new Date(new Date().getTime() - 1000 * 60 * 60 * 24);
    var year = date.getFullYear();
    var month = (date.getMonth() + 1 < 10) ? (date.getMonth() + 1 + '0') : (date.getMonth() + 1);
    var day = (date.getDate() - 1 < 10) ? ('0' + date.getDate()) : date.getDate();
    var preDate = year + '/' + month + '/' + day;
    //接收xwcode
    var xw_code = getQueryString("xwCode");
})
function initParams() {
    time = getQueryString("time");
    industryCode = getQueryString("industryCode");
    guZhiRongsu = getQueryString("guZhiRongsu");
    xwset = getQueryString("xwset");
}

$(function () {
    $('#ec01_line').trigger("hover");
    initParams();
})