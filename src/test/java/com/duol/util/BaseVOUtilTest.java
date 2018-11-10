package com.duol.util;

import com.duol.pojo.Order;
import com.duol.vo.OrderVO;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Duolaimon
 * 18-8-24 下午8:21
 */
public class BaseVOUtilTest {

    @Test
    public void OrderVoTest() {
        Order order = new Order();
        order.setStatus(0);
        order.setId(111);
        OrderVO orderVO = BaseVOUtil.parse(order, OrderVO.class);
    }
}