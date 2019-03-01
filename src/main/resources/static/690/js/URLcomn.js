//var domain="http://localhost:9999/api/v1/common/interface";
var domain = "http://${ctx}/api/v1/common/interface";
var clientUrl = domain + "/getByDataType";//接口地址
var insetUrl = domain + "/insertDate";
var userCode = "A0007773";//用户编码
/*监控屏幕变化 刷新数据*/
window.onresize = function () {
    // 针对火狐浏览器
    if (navigator.userAgent.indexOf("Firefox") > 0) {
        setTimeout("location.reload();", 500);
    } else {
        window.location.reload();
    }
}
/**
 * 通过统一接口请求接口数据
 * @param dataType:统一接口dataType
 * @param params：接口参数
 * @param callBack：回调函数
 */
function getDateByCommonInterface(dataType, params, successCallBack, failureCallBack) {
    if (!params) {
        params += "userCode::" + userCode;//拼接统一用户信息
    } else {
        params += ";;userCode::" + userCode;//拼接统一用户信息
    };
    $.get(clientUrl, { "dataType": dataType, "params": params }, function (data, status) {
        if (status == "success") {
            var jsonData = JSON.parse(data);
            if (jsonData.result == "00000000") {
                //数据请求成功
                successCallBack(jsonData.data);
            } else {
                if (failureCallBack) {
                    failureCallBack(jsonData.data);
                };
                console.error(dataType + "请求异常，" + jsonData.errorMsg);
            }
        }
    })
};
/**
 * @param params
 * 拼接参数
 */
function joinParams(params, key, value) {
    if (!params) {
        params += key + "::" + value;
    } else {
        params += ";;" + key + "::" + value;
    };
    return params;
};
function getGuzhiRongsu() {
    guZhiRongsu = $(".switch_2").text();
}
function getDateByCommonInterfaceByParam(dataType, params, successCallBack, failureCallBack, callBackParams) {
    if (!params) {
        params += "userCode::" + userCode;//拼接统一用户信息
    } else {
        params += ";;userCode::" + userCode;//拼接统一用户信息
    };
    $.get(clientUrl, { "dataType": dataType, "params": params }, function (data, status) {
        if (status == "success") {
            var jsonData = JSON.parse(data);
            if (jsonData.result == "00000000") {
                //数据请求成功
                successCallBack(jsonData.data, callBackParams);
            } else {
                if (failureCallBack) {
                    failureCallBack(jsonData.data, callBackParams);
                };
                console.error(dataType + "请求异常，" + jsonData.errorMsg);
            }
        }
    })
};

function insetDateToServer(dataType, params, callBack) {
    if (!params) {
        params += "userCode::" + userCode;//拼接统一用户信息
    } else {
        params += ";;userCode::" + userCode;//拼接统一用户信息
    };
    $.post(insetUrl, { "dataType": dataType, "params": params }, function (data, status) {
        if (status == "success") {
            var jsonData = JSON.parse(data);
            console.log(jsonData)
            if (jsonData.result == "00000000") {
                //数据请求成功
                callBack(jsonData);
            } else {
                alert(dataType + "请求异常，" + jsonData.errorMsg);
            }
        }
    })
}

// 转百分比
function toPercent(point) {
    var str = Number(point * 100).toFixed(0);
    str += "%";
    return str;
}

// 自定义方法 -- add by lijianlong
(function initTools() {
    $.fn.extend({
        /**
         * 将文本转为数字并保留相应小数位数
         * @param n 小数位数
         * @param power 数据缩放到10的多少次方
         * @param str 后面可以跟上个字符串，比如‘%’
         */
        str2NumFixed: function (n, power, str = '') {
            $.each($(this), function () {
                $(this).text(parseFloat($(this).text() + 'e' + power).toFixed(n) + str);
            })
        }
    })
})();
