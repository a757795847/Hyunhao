package com.zy.gcode.controller.delegate;

import org.springframework.util.StringUtils;

import javax.naming.OperationNotSupportedException;

/**
 * Created by admin5 on 17/1/18.
 */
public class CodeRe<T> {
    private boolean isError = false;
    private String error;
    private T message;

    public boolean isError() {
        return isError;
    }

    public static<A> CodeRe<A> correct(A t){
        return new CodeRe(t);
    }

    public static CodeRe error(String message){
        return new CodeRe(message);
    }


    public CodeRe() {
    }

    public CodeRe(String error) {
        this.setError(error);
    }

    public CodeRe(T message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        if(!StringUtils.isEmpty(error)){
            this.isError=true;
        }
    }

    public T getMessage() {
        if(isError()){
            throw new IllegalStateException();
        }

        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
