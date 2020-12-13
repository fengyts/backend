package com.backend.delayorder;

import com.backend.exception.ServiceException;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 订单到期处理组件
 *
 * @Author: fengyts
 * @Date: Created in 21:37 2019/8/15
 * @Description:
 */
@Component
@Lazy(false)
public class DelayOrderHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayOrderHandler.class);

//    @Autowired
//    private YiTongProperties yiTongProperties;
//    @Autowired
//    private FeeOrderMapper feeOrderMapper;
//    @Autowired
//    private WxPayService wxPayService;

    // 需要处理的订单队列
    private static final DelayQueue<DelayOrder> delayQueue = new DelayQueue<>();

    /**
     * 服务器重启初始化时 加载数据库中需处理的订单
     */
    @PostConstruct
    public void init() throws Exception {
        LOGGER.error(">>>>>>>> 系统启动，初始化数据库未支付订单到处理队列.....");
//        List<FeeOrder> orderList = feeOrderMapper.selectUnpaidOrderList();
        List<FeeOrder> orderList = Lists.newArrayList();
        for (FeeOrder feeOrder : orderList) {
            DelayOrder delayOrder = new DelayOrder(
                    feeOrder.getOrderNo(),
                    feeOrder.getOrderTime(),
                    getOrderExpireTime());
            this.add(delayOrder);
        }

        startHandlerOrder();

    }

    private Long getOrderExpireTime() {
        return 0L;
    }

    /**
     * 开启处理订单
     */
    private void startHandlerOrder() {
        //启动线程池来处理队列中的订单
        Executors.newCachedThreadPool().execute(() -> {
            DelayOrder delayOrder = null;
            FeeOrder feeOrder = null;
            String orderNo = "";
            while (true) {
                try {
                    delayOrder = delayQueue.take();
                    //处理到期的订单
//                    feeOrderMapper.updateExpiredOrder(delayOrder.getOrderNo());
//                    feeOrder = new FeeOrder();
//                    feeOrder.setState(FeeOrderStateEnum.PAY_TIMEOUT.getCode());
//                    orderNo = delayOrder.getOrderNo();
//                    feeOrderMapper.update(feeOrder,
//                            new UpdateWrapper<FeeOrder>()
//                                    .eq("order_no", orderNo)
//                                    .eq("state", FeeOrderStateEnum.WAIT_PAY.getCode()));

                    //微信关单
//                    wxPayService.closeOrder(orderNo);
                } catch (Exception e) {
                    LOGGER.error("orderNo:" + orderNo + ",订单到期处理异常:", e.getMessage());
                    throw new ServiceException("orderNo:" + orderNo + ",订单到期处理异常:" + e);
                }
            }
        });
    }

    /**
     * 提供加入消息到延迟队列方法，当下单的时候加入延迟消息队列。
     *
     * @param delayOrder
     * @return
     */
    public static boolean add(DelayOrder delayOrder) {
        return delayQueue.add(delayOrder);
    }


    /**
     * 提供删除延迟消息方法，当用户主动取消订单，或者支付成功后使用。
     *
     * @param orderNo
     */
    public static void remove(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return;
        }
        Iterator<DelayOrder> iterator = delayQueue.iterator();
        while (iterator.hasNext()) {
            DelayOrder delayOrder = iterator.next();
            if (orderNo.equals(delayOrder.getOrderNo())) {
                delayQueue.remove(delayOrder);
                break;
            }
        }
    }

    public static DelayOrder getDelayOrder(String orderNo){
        if (StringUtils.isBlank(orderNo)) {
            return null;
        }
        Iterator<DelayOrder> iterator = delayQueue.iterator();
        while (iterator.hasNext()) {
            DelayOrder delayOrder = iterator.next();
            if(orderNo.equals(delayOrder.getOrderNo())){
                return delayOrder;
            }
        }
        return null;
    }

    public static DelayQueue<DelayOrder> getAllDelayOrder() {
        return delayQueue;
    }

}