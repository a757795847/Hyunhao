package com.zy.gcode.controller.delegate;

/**
 * Created by admin5 on 17/1/18.
 */
public class CodeRe<T> {
    private boolean isError = false;
    private String error;
    private T message;

    public CodeRe() {
    }

    public CodeRe(String error) {
        this.setError(error);
    }

    public CodeRe(T message) {
        this.message = message;
    }

    public static <A> CodeRe<A> correct(A t) {
        return new CodeRe(t);
    }

    public static CodeRe error(String message) {
        return new CodeRe(message);
    }

    public boolean isError() {
        return isError;
    }

    public void setError(String error) {
        this.error = error;
        this.isError = true;
    }

    public String getErrorMessage() {
        return error;
    }

    public T getMessage() {
        if (isError()) {
            throw new IllegalStateException();
        }

        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CodeRe{" +
                "isError=" + isError +
                ", error='" + error + '\'' +
                ", message=" + message +
                '}';
    }
}
