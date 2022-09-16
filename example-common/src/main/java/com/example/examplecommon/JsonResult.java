package com.example.examplecommon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult implements Serializable {

    private static final long serialVersionUID = -4800793124936904868L;
    public static final int SUCCESS = 200;
    public static final int ERROR = 201;

    /**
     * 返回是否成功的状态,200表示成功,
     * 201或其他值 表示失败
     */
    private Integer state;
    /**
     * 成功时候,返回的JSON数据
     */
    private Object data;
    /**
     * 是错误时候的错误消息
     */
    private String message;

    public JsonResult(Throwable e) {
        state = ERROR;
        data = null;
        message = e.getMessage();
    }

    public JsonResult(Object data) {
        state = SUCCESS;
        this.data = data;
        message = "";
    }

    public static JsonResult failed() {
        return new JsonResult(ERROR, "", "fail");
    }

    public static JsonResult failed(String message) {
        return new JsonResult(ERROR, "", message);
    }

    public static JsonResult success() {
        return new JsonResult("OK");
    }

    public static JsonResult success(Object data) {
        return new JsonResult(data);
    }

}