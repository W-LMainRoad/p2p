package com.zc.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:PageinatinoVO
 * Package:com.zc.p2p.model.vo
 * Description:
 *
 * @date:2019/10/30 9:31
 * @author:youxiang@163.com
 */
public class PageinatinoVO<T> implements Serializable {
    /**
     * 总记录条数
     */
    private Long total;

    /**
     * 显示数据
     */
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
