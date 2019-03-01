// init datepaker
if ($('#dateIpt')[0]) {
    $('#dateIpt').date_input();
}
// 时间显示
var spans = document.querySelectorAll('.time span');
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

// // 侧栏菜单的显示
// $('.show_btn').click(function () {
//     var menu_w = $('.leftnav').css('width');
//     if (menu_w == '0px') {
//         var menu_w = $('.leftnav').css('width', '17%');
//     } else {
//         var menu_w = $('.leftnav').css('width', '0');
//     }
// })
// // 侧栏菜单的次级菜单
// $('.hav_sec').click(function () {
//     var menu_h = $('.menu_item_in').css('height');
//     if (menu_h == '0px') {
//         var menu_h = $('.menu_item_in').css('height', '12%');
//     } else {
//         var menu_h = $('.menu_item_in').css('height', '0');
//     }
// })

// // 重置提交
// $('.submitbox_btn').click(function () {
//     var menu_w = $('.submitbox').css('width');
//     if (menu_w == '0px') {
//         var menu_w = $('.submitbox').css('width', '10%');
//         $('.submitbox_btn').text('▶')
//     } else {
//         var menu_w = $('.submitbox').css('width', '0');
//         $('.submitbox_btn').text('◀')
//     }
// })

// 右模块轮播
function rightScroll() {
    var timer;
    function wfscroll() {
        $('.column_scroll .column_screen').stop().animate({ marginLeft: '-100%' }, 600, "linear", function () {
            var a = $('.column_scroll .column_screen div:first').clone(true);
            scroll_again(a);
            $('.column_scroll .column_screen div:first').remove();
            a.appendTo('.column_scroll .column_screen');
            $('.column_scroll .column_screen').css('marginLeft', '0');
        })
    }
    timer = setInterval(wfscroll, 15000);
    $('.column_scroll').hover(function () {
        clearInterval(timer);
    }, function () {
        timer = setInterval(wfscroll, 15000);
    })
    $('.btn_next').click(function () {
        wfscroll();
    })
}
function rightScroll2() {
    var timer;
    function wfscroll() {
        $('.column_scroll .column_screen').stop().animate({ marginLeft: '-100%' }, 600, "linear", function () {
            var a = $('.column_scroll .column_screen div:first').clone(true);
            scroll_again2(a);
            $('.column_scroll .column_screen div:first').remove();
            a.appendTo('.column_scroll .column_screen');
            $('.column_scroll .column_screen').css('marginLeft', '0');
        })
    }
    timer = setInterval(wfscroll, 16000);
    $('.column_scroll').hover(function () {
        clearInterval(timer);
    }, function () {
        timer = setInterval(wfscroll, 16000);
    })
    $('.btn_next').click(function () {
        wfscroll();
    })
}
/**
 * 小轮播延迟版
 */
function setScroll() {
    var lunboitem = document.querySelectorAll('.block_scroll_screen');
    lunboitem.forEach(lunbo);
}
function lunbo(item, i) {
    // console.log(item,i);
    var timers;
    function wfscroll() {
        // console.log(this);
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

/**
 * 从一个绘制好的echarts图表(visibility: hidden;)元素生成图片载入到目标元素中
 * @param fromId 图表元素id
 * @param toId 目标元素id
 * @param delayTime 延迟时间，为了避免动画带来的绘制未完成就转成图片的问题
 *        可关闭动画：option：{animation:false,}
 */
let paintEchartsImg = (fromId, toId, delayTime = 200) => {
    let $fromId = $(fromId);
    //将第一个画布作为基准。
    let baseCanvas = $fromId.find("canvas").first()[0];
    if (!baseCanvas) {
        return false;
    }
    let [width, height] = [baseCanvas.width, baseCanvas.height];
    let ctx = baseCanvas.getContext("2d");
    //遍历，将后续的画布添加到在第一个上
    $fromId.find("canvas").each(function (i, canvasObj) {
        if (i > 0) {
            let canvasTmp = $(canvasObj)[0];
            ctx.drawImage(canvasTmp, 0, 0, width, height);
        }
    });
    //获取base64位的url
    let base64URL = baseCanvas.toDataURL();
    setTimeout(function () {
        if (['#ec04_bar', '#ec05_bar', '#ec06_bar', '#ec07_bar', '#ec08_bar', '#ec09_bar', '#ec10_bar', '#ec11_bar', '#ec12_bar', '#ec13_bar'].includes(fromId)) {
            $fromId.empty();
            $(toId).css({
                background: `url("${base64URL}")`,
                backgroundSize: 'contain'
            });
            $fromId.removeAttr('_echarts_instance_')
        }
        $(toId).css('visibility', 'initial')

    }, delayTime)
};

// 右模块轮播控制触点网络小轮播
// 小轮播
function scroll_again(area) {
    var lunboitem2 = area.find('.block_scroll_screen');
    // console.log(lunboitem2);
    lunboitem2.each(lunbo1);
}
function lunbo1(i, item) {
    var timers;
    function wfscroll() {
        if ($('.alertbox_year').data('id') == $(item).data('id') && $('.alertbox_year').css('visibility') == 'visible') {
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
    $(item).parent().hover(function () {
        clearInterval(timers);
    }, function () {
        timers = setInterval(wfscroll, 4000 + i * 1000);
    })
}
// 右模块轮播控制用户小微小轮播
// 小轮播
function scroll_again2(area) {
    var lunboitem2 = area.find('.block_scroll_screen');
    // console.log(lunboitem2);
    lunboitem2.each(lunbo_r);
}
function lunbo_r(i, item) {
    var timers;
    function wfscroll() {
        if ($('.alertbox_year').data('id') == $(item).data('id') && $('.alertbox_year').css('visibility') == 'visible') {
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
    timers = setInterval(wfscroll, 4000);
    $(item).parent().hover(function () {
        clearInterval(timers);
    }, function () {
        timers = setInterval(wfscroll, 4000);
    })
}

