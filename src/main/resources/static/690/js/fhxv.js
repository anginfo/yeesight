// 右大模块轮播
var timer;
function wfscroll() {
    $('.m_right_scroll .screen').stop().animate({ marginLeft: '-100%' }, 600, "linear", function () {
        var a = $('.m_right_scroll .screen div:first').clone(true);
        scroll_again_2(a);
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
function scroll_again_2(area) {
    var lunboitem2 = area.find('.block_scroll_screen');
    // console.log(lunboitem2);
    lunboitem2.each(lunbo2);
}
function lunbo2(i, item) {
    var timers;
    function wfscroll() {
        if ($('.alertbox').data('id') == $(item).data('id') && $('.alertbox').css('visibility') == 'visible') {
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
    timers = setInterval(wfscroll, 4000 + i * 1000);
}

//跳转点击事件
/**
 * 01Z0  雷神  03H0  一数
   0CA0  塔波尔  0BC0  社区洗
   0N80	云裳羽衣 0FA0	国际教育
   0DG0	卫玺 0AP0	智慧教育
   0QH0	极家云  05C0	云厨
   90D0	优悦  0MD0	酒知道
   0MD1	酒咔嚓  90K0	零微科技
   展示 雷神 一数 塔波尔 社区洗 卫玺 智慧教育
 * @type {[*]}
 */
var intentXwCode = ["01Z0", "03H0", "0CA0", "0BC0", "0DG0", "0AP0"];
$('body').on('click', '.pointer', function () {
    var XW_CODE = $(this).attr("xw_code");
    var xwset = $("#select_xwcode").val();
    if (!xwset) {
        xwset = "ALL";
    }
    if (intentXwCode.indexOf(XW_CODE) != -1) {
        initVarParams();
        var type = 1;
        if (guZhiRongsu == "估值") {
            type = 0;
        }
        // 判断箭头指向
        if ($('.right_arrow').css('display') == 'block') {
            localStorage.setItem('cutArrow', '1');
        } else {
            localStorage.setItem('cutArrow', '0');
        }
        location.href = `/bigSreen/sys/v1/fhxv-2?xwCode=${XW_CODE}&time=${vdate}&industryCode=${industryCode}&guZhiRongsu=${type}&xwset=${xwset}`;
    }
    localStorage.setItem('selectDataFHXV', JSON.stringify({
        dateIpt: $("#dateIpt").val(),
        fromClick: true
    }));
    // 用上次的选择结果初始化时间控件
    if (localStorage.getItem('selectData')) {
        $("#dateIpt").val(selectData.dateIpt);
    }
})

/**
 * 控制左右切换
 */
function cutArrow() {
    $('.left_arrow').click(function () {
        $(this).hide();
        $('.right_arrow').show();
        var time = formatDate($("#dateIpt").val());
        time = time.split("-");
        var year = time[0];
        var month = time[1];
        if (month == 1) {
            month = 12;
            year = year - 1;
        } else {
            month = month - 1;
        }
        var myDate = new Date(year, month, 0);
        var lastDay = year + "-" + month + "-" + myDate.getDate();
        $("#dateIpt").val(lastDay);
        searchFunction();
    })
    $('.right_arrow').click(function () {
        $(this).hide();
        $('.left_arrow').show();
        $("#dateIpt").val(formatDate(new Date() - 1000 * 60 * 60 * 24));
        searchFunction();
    })
}
cutArrow();