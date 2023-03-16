package com.tbc.ddd.infrastructure.user.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

/**
 * <p>
 * 用户账户信息，涉及金额 分佣机制
 * </p>
 *
 * @author Johnson.Jia
 * @since 2023-03-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@TableName("t_uc_account")
public class AccountPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 银行卡账户
     */
    @TableField("bank_card_no")
    private String bankCardNo;

    /**
     * 身份地位： 大鳄-大佬-舵主-达人-平民
     */
    @TableField("standing")
    private String standing;

    /**
     * 用户 排名
     */
    @TableField("user_rank")
    private Integer userRank;

    /**
     * 账户可提现余额 单位 分
     */
    @TableField("free_money")
    private Integer freeMoney;

    /**
     * 账户冻结金额 单位 分
     */
    @TableField("freeze_money")
    private Integer freezeMoney;

    /**
     * 累计提现 单位 分
     */
    @TableField("total_cash_out")
    private Integer totalCashOut;

    /**
     * 弟子人数，邀请总人数
     */
    @TableField("pupil_num")
    private Integer pupilNum;

    /**
     * 消费弟子人数，消费订单支付总人数
     */
    @TableField("consume_pupil_num")
    private Integer consumePupilNum;

    /**
     * 固定佣金百分比 ，小程序分享佣金 默认 5%
     */
    @TableField("fixed_brokerage")
    private Integer fixedBrokerage;

    /**
     * 固定弟子下单购买 累计收益金额 ，单位 分
     */
    @TableField("fixed_total_profit")
    private Integer fixedTotalProfit;

    /**
     * 消费佣金百分比，商品购买佣金 默认 5%
     */
    @TableField("consume_brokerage")
    private Integer consumeBrokerage;

    /**
     * 消费订单累计收益金额 单位 分
     */
    @TableField("consume_total_profit")
    private Integer consumeTotalProfit;

    /**
     * 固定弟子订单 总数量， 分佣总次数
     */
    @TableField("fixed_orders")
    private Integer fixedOrders;

    /**
     * 消费订单 总数量， 分佣总次数
     */
    @TableField("consume_orders")
    private Integer consumeOrders;

    /**
     * 累计推广总金额，单位 分
     */
    @TableField("spread_total_money")
    private Integer spreadTotalMoney;

    /**
     * 邀请者 用户id
     */
    @TableField("invited_user_id")
    private Long invitedUserId;

    /**
     * 创建时间 时间戳
     */
    @TableField("create_time")
    private Long createTime;
}
