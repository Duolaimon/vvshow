package com.duol.service;

import com.duol.common.ServerResponse;
import com.duol.dto.order.QRCode;
import com.duol.vo.OrderProductVO;
import com.duol.vo.OrderVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author Duolaimon
 * 18-7-16 下午3:10
 */
public interface OrderService {
    ServerResponse<QRCode> pay(Long orderNo, Integer userId, String path);
    ServerResponse aliCallback(Map<String,String> params);
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
    ServerResponse<OrderVO> createOrder(Integer userId,Integer shippingId);
    ServerResponse<String> cancel(Integer userId,Long orderNo);
    ServerResponse<OrderProductVO> getOrderCartProduct(Integer userId);
    ServerResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo);
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);



    //backend
    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);
    ServerResponse<OrderVO> manageDetail(Long orderNo);
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
    ServerResponse<String> manageSendGoods(Long orderNo);

    ServerResponse<OrderVO> createOrder(Integer userId, Integer shippingId, String productIds);
}
