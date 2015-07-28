package com.duowan.common.crud;

/**
 * 基本分页请求参数
 */
public class BasePagerRequest {
    private Integer pageSize=10;

    private Integer currPage=1;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }
}
