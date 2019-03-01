// 声明登陆人员信息
let userInfo = {
    userID: '',
    dim_access: []
};

// 获取登陆人员权限信息
var ajax = new XMLHttpRequest();
ajax.open('get', '/bigSreen/sys/v1/menuAuthority');
ajax.send();
ajax.onreadystatechange = function () {
    if (ajax.readyState == 4 && ajax.status == 200) {
        var data = JSON.parse(ajax.responseText);
        var ableDate_user = data['data'];
//        console.log(ableDate_user);
        ableDate_user.map((item) => {
            userInfo.userID = item.user_code;
            userInfo.dim_access.push(Number(item.dim_value_code));
        });
     // 通过登录信息判断模块权限
//        console.log(userInfo.dim_access);
        userInfo.dim_access.forEach((item, i) => {
            $('.nav_btns .pt_btn').eq(item).css('display','block');
        })
    }
}

// 模拟获取数据
// var ableDate_user = [
//     {
//         "application_id": null,
//         "application_name": null,
//         "user_code": "A0026973",
//         "dim_type_name": null,
//         "dim_value_code": "2",
//         "dim_value": "触点网络"
//     },
//     {
//         "application_id": null,
//         "application_name": null,
//         "user_code": "A0026973",
//         "dim_type_name": null,
//         "dim_value_code": "1",
//         "dim_value": "用户小微"
//     },
//     {
//         "application_id": null,
//         "application_name": null,
//         "user_code": "A0026973",
//         "dim_type_name": null,
//         "dim_value_code": "3",
//         "dim_value": "孵化小微"
//     }
// ];
// 对数据进行筛选
// ableDate_user.map((item) => {
//     userInfo.userID = item.user_code;
//     userInfo.dim_access.push(Number(item.dim_value_code));
// });