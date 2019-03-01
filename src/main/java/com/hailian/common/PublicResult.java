package com.hailian.common;

import com.hailian.base.BaseResult;
import com.hailian.enums.PublicResultConstant;


/**
 * @author zuoqb
 * @since 2018-05-03
 */
public class PublicResult <T> extends BaseResult<T> {

    public static final String DEFAULT_CODE = "90000003";
    public PublicResult(PublicResultConstant publicResultConstant, T data) {
        super(publicResultConstant.getResult(), publicResultConstant.getMsg(), data);
    }
    public PublicResult(PublicResultConstant publicResultConstant,String errorMsg, T data) {
        super(publicResultConstant.getResult(), publicResultConstant.getMsg(),errorMsg, data);
    }
    public PublicResult(String message, T data) {
        super(DEFAULT_CODE, message, data);
    }
}
