package com.zy.gcode.utils;

/**
 * Created by admin5 on 17/1/16.
 */
public class Page {
    public static final String CURRENTPAGEINDEX = "currentPageIndex";
    public static final String PAGE_SIZE = "pageSize";


    private int currentPageIndex;
    private int pageSize = 30;
    private int count;
    private int pageCount;
    private int startIndex;

    public int getCurrentPageIndex() {
          if(currentPageIndex<1){
            return 1;
        }
        if(currentPageIndex > pageCount){
            return pageCount;
        }
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if(pageSize>0)
        this.pageSize = pageSize>30?30:pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageCount() {

        return count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStartIndex() {

        return (currentPageIndex - 1) * pageSize;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
