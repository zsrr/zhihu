package com.stephen.zhihu.dto;

public class PaginationResponse {
    Pagination pagination;

    public PaginationResponse(Pagination pagination) {
        this.pagination = pagination;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
