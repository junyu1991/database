package com.yujun.database.mongodb.file.dao;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/17
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@AllArgsConstructor
public class QueryPage implements Pageable {

    /** 页数 **/
    private int pageNumber;
    /** 每页大小 **/
    private int pageSize;
    /** 排序方式 **/
    private Sort sort;

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getOffset() {
        return (getPageNumber()-1)*getPageSize();
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        this.pageNumber += 1;
        return this;
    }

    @Override
    public Pageable previousOrFirst() {
        this.pageNumber = this.pageNumber>1?this.pageNumber-1:1;
        return this;
    }

    @Override
    public Pageable first() {
        this.pageNumber = 1;
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return this.pageNumber > 1;
    }
}
