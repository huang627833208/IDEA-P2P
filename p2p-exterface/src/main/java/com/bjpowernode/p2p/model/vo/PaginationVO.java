package com.bjpowernode.p2p.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:PaginationVO
 * Package:com.bjpowernode.p2p.model.vo
 * Description:TODO
 *
 * @date:2019/8/30 10:44
 * @author:guoxin
 */
@Data
public class PaginationVO<T> implements Serializable {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> dataList;



}
