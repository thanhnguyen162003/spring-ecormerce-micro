package com.app.productservice.Common.Pagination;

import lombok.Data;

@Data
public class ProductPagination {
    public int page;
    public int pageSize;
    public int total;
    public int totalPages;
}
