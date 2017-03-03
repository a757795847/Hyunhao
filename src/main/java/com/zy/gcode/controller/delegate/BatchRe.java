package com.zy.gcode.controller.delegate;

import java.util.List;

/**
 * Created by admin5 on 17/2/13.
 */
public class BatchRe<T> extends CodeRe<List> {
    private List<String> errorList;
    private List<T> tlist;

    @Override
    public boolean isError() {
        if (tlist.isEmpty() || (!errorList.isEmpty())) {
            return false;
        }
        return true;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public List<T> getTlist() {
        return tlist;
    }

    public void setTlist(List<T> tlist) {
        this.tlist = tlist;
    }

    @Override
    public List getMessage() {
        return tlist;
    }
}
