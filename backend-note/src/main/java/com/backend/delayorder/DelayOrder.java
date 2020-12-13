package com.backend.delayorder;

import com.backend.utils.DateUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fengyts
 * @Date: Created in 21:38 2019/8/15
 * @Description:
 */
@Data
public class DelayOrder implements Delayed {

    //订单到期时间
    private final static long EXPIRE_TIME = DelayOrderConstants.EXPIRE_TIME;

    // 订单号
    private final String orderNo;
    // 订单开始时间
    private final long startTime;
    // 到期时间
    private final long expireTime;
    //订单其他信息JSON方式保存，备用字段
    private final String otherMsg;

    /**
     * @param orderNo
     * @param startTime    订单下单时间
     * @param delaySeconds 指定过期时间, 单位：秒
     */
    public DelayOrder(String orderNo, LocalDateTime startTime, long delaySeconds, String otherMsg) {
        super();
        this.orderNo = orderNo;
        this.startTime = DateUtil.getTime(startTime);
        this.expireTime = this.startTime + (delaySeconds * 1000);
        this.otherMsg = otherMsg;
    }

    /**
     * @param orderNo
     * @param startTime     订单下单时间
     * @param delaySeconds  指定过期时间, 单位：秒
     */
    public DelayOrder(String orderNo, LocalDateTime startTime, long delaySeconds) {
        this(orderNo, startTime, delaySeconds, null);
    }

    public DelayOrder(String orderNo, LocalDateTime startTime, String otherMsg) {
        this(orderNo, startTime, EXPIRE_TIME, otherMsg);
    }

    public DelayOrder(String orderNo, LocalDateTime startTime) {
        this(orderNo, startTime, EXPIRE_TIME, null);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }


}