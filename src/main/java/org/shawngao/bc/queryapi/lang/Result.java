package org.shawngao.bc.queryapi.lang;

import java.io.Serializable;

public class Result implements Serializable {

    private int code;
    private Object data;

    public Result() {
        //
    }

    public Result(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
