package org.shawngao.bc.queryapi.annotation;

import java.lang.reflect.Method;

public class RequestObject {

    private Class<?> clazz;
    private Method method;

    public RequestObject(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getMethod() {
        return method;
    }
}
