package com.duol.service;

import com.duol.common.ServerResponse;
import com.duol.pojo.Shipping;
import com.github.pagehelper.PageInfo;

/**
 * @author Duolaimon
 * 18-7-16 上午9:41
 */
public interface ShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> del(Integer userId,Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
