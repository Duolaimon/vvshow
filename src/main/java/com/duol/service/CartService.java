package com.duol.service;

import com.duol.common.ServerResponse;
import com.duol.vo.CartVO;

/**
 * @author Duolaimon
 * 18-7-15 下午8:49
 */
public interface CartService {
    ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVO> update(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVO> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVO> list (Integer userId);
    ServerResponse<CartVO> selectOrUnSelect (Integer userId, Integer productId, Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
