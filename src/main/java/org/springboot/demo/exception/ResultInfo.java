package org.springboot.demo.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 组合返回结果
 */
public class ResultInfo {

    // 描述信息
    private String message;
    // 返回数据
    private Object data = "";

    public ResultInfo() {

    }

    public ResultInfo(Object data) {
        this.data = data;

    }

    public ResultInfo(String msg) {
        this.message = msg;
    }

    public ResultInfo(String msg, Object data) {
        this.message = msg;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
