package com.duowan.common.crud;

import java.util.List;

/**
 *分页数据
 */
public class DataPage<T> {
    private int pageNo=1;//当前页数，1开始
    private int pageSize=10;//页显示大小
    private int totalRecord;//总记录数
    private List<T> results; //当前页记录数

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    /**
     * 返回总页数
     * @return
     */
    public int getTotalPages(){
        if(pageSize<=0) pageSize=1;
        return (int) Math.ceil( totalRecord*1.0d /  pageSize);
    }
}
