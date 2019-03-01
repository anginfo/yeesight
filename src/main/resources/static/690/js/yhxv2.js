
//获取评论数据
function getCommentData(inCode,time) {
    getCurrentDate();
    if (inCode) {
        var params = "inCode::" + inCode + ";;time::" + time;
    } else {
        time = formatDate($("#chanyeTime").val()).replace(/-/g, "");
        var params = "inCode::BAA;;time::"+time;
    }
    getDateByCommonInterfaceByParam("690_yhxw_p01", params, setCommentData, setCommentData, 0);
    getDateByCommonInterfaceByParam("690_yhxw_p02", params, setCommentData, setCommentData, 1);
    getDateByCommonInterfaceByParam("690_yhxw_p04", params, setCommentData, setCommentData, 2);
    getDateByCommonInterfaceByParam("690_yhxw_p03", params, setCommentData, setCommentData, 3);
    getDateByCommonInterfaceByParam("690_yhxw_p05", params, setCommentData, setCommentData, 4);
    getDateByCommonInterfaceByParam("690_yhxw_p06", params, setCommentData, setCommentData, 5);
    getDateByCommonInterfaceByParam("690_yhxw_p07", params, setCommentData, setCommentData, 6);
    getDateByCommonInterfaceByParam("690_yhxw_p08", params, setCommentData, setCommentData, 7);
};
//封装评论数据
function setCommentData(data, dataIndex) {
    // for (let t in data) {
    //     for (let v in data[t][0]) {
    //         if (data[t][0][v] == null) {
    //             data[t][0][v] = '';
    //         }
    //     }
    // }
    //console.log(data)
    if (dataIndex == 0) {
        if (data["690_yhxw_p1"][0]) {
            $("#comment_0").val(data["690_yhxw_p1"][0].MB_DW);
            $("#comment_1").val(data["690_yhxw_p1"][0].MB_SJMB);
        }else{
            $("#comment_0").val("");
            $("#comment_1").val("");
        }
    } else if (dataIndex == 1) {
        if (data["690_yhxw_p2"][0]) {
            //  console.log(data["690_yhxw_p2"])
            $("#comment_2").val(data["690_yhxw_p2"][0].MB_HYDW);
            $("#comment_3").val( data["690_yhxw_p2"][0].MB_SJ);
        }else{
            $("#comment_2").val("");
            $("#comment_3").val("");
        }
        
    } else if (dataIndex == 2) {
        if (data["690_yhxw_p4"][0]) {
            $("#comment_4").val( data["690_yhxw_p4"][0].YS_HYDW);
            $("#comment_5").val( data["690_yhxw_p4"][0].YS_SJ);
        }else{
            $("#comment_4").val("");
            $("#comment_5").val("");
        }
       
    } else if (dataIndex == 3) {
        if (data["690_yhxw_p3"][0]) {
            $("#comment_6").val( data["690_yhxw_p3"][0].SJ_HYDW);
            $("#comment_7").val( data["690_yhxw_p3"][0].SJ_SJ);
        }else{
            $("#comment_6").val("");
            $("#comment_7").val("");
        } 
    } else if (dataIndex == 4) {
        if (data["690_yhxw_p5"][0]) {
            $("#comment_8").val( data["690_yhxw_p5"][0].YS_HYDW);
            $("#comment_9").val( data["690_yhxw_p5"][0].YS_SJ);
        }else{
            $("#comment_8").val("");
            $("#comment_9").val("");
        } 
    } else if (dataIndex == 5) {
        if (data["690_yhxw_p6"][0]) {
            $("#comment_10").val( data["690_yhxw_p6"][0].YS_HYDW);
            $("#comment_11").val( data["690_yhxw_p6"][0].YS_SJ);
        }else{
            $("#comment_10").val("");
            $("#comment_11").val("");
        } 
    } else if (dataIndex == 6) {
        if (data["690_yhxw_p7"][0]) {
            //Q1
            $("#comment_12").val(data["690_yhxw_p7"][0].MB_HYDW);
            $("#comment_13").val( data["690_yhxw_p7"][0].MB_SJ);
        }else{
            $("#comment_12").val("");
            $("#comment_13").val("");
        } 
    } else if (dataIndex == 7) {
        if (data["690_yhxw_p8"][0]) {
            //Q2
            $("#comment_14").val(data["690_yhxw_p8"][0].MB_HYDW);
            $("#comment_15").val(data["690_yhxw_p8"][0].MB_SJ);
        }else{
            $("#comment_14").val("");
            $("#comment_15").val("");
        }
    }
}