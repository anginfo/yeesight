$(function () {
    $('.nav_btns .pt_btn').each(function (i, item) {
        var url = '';
        switch (i) {
            case 0: url = '/bigSreen/sys/v1/sy-yhxw'; break;
            case 1: url = '/bigSreen/sys/v1/yhxv'; break;
            case 2: url = '/bigSreen/sys/v1/cdwl-sq'; break;
            case 3: url = '/bigSreen/sys/v1/fhxv'; break;
            default: url = '#';
        }
        $(item).click(function () {
            location.href = url;
        })
    })
})