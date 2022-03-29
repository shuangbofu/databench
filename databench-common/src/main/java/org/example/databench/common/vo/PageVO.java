package org.example.databench.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    /**
     * 当前页
     */
    private Long pageNum;
    /**
     * 每页的数量
     */
    private Long pageSize;
    /**
     * 总记录数
     */
    private Long totalSize;
    /**
     * 总页数
     */
    private Long totalPage;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
