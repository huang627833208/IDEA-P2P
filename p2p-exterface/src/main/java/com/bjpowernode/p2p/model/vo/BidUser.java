package com.bjpowernode.p2p.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName:BidUser
 * Package:com.bjpowernode.p2p.model.vo
 * Description:TODO
 *
 * @date:2019/9/5 12:28
 * @author:guoxin
 */
@Data
public class BidUser implements Serializable {

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 分数
     */
    private Double score;
}
