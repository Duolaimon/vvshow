package com.duol.controller.portal;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.service.CartService;
import com.duol.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Duolaimon
 * 18-7-15 下午9:30
 */
@RestController
@RequestMapping("/carts/")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/{userId}")
    public ServerResponse<CartVO> list(@PathVariable("userId") Integer userId) {
        return cartService.list(userId);
    }

    @PostMapping("{userId}")
    public ServerResponse<CartVO> add(@PathVariable("userId") Integer userId, Integer count, Integer productId) {
        return cartService.add(userId, productId, count);
    }


    @PutMapping("{userId}")
    public ServerResponse<CartVO> update(@PathVariable("userId") Integer userId, Integer count, Integer productId) {
        return cartService.update(userId, productId, count);
    }

    @DeleteMapping("{userId}")
    public ServerResponse<CartVO> deleteProduct(@PathVariable("userId") Integer userId, String productIds) {
        return cartService.deleteProduct(userId, productIds);
    }

    /**
     * 全选
     */
    @GetMapping("{userId}/checked")
    public ServerResponse<CartVO> selectAll(@PathVariable("userId") Integer userId) {
        return cartService.selectOrUnSelect(userId, null, Const.Cart.CHECKED);
    }

    /**
     * 全反选
     */
    @GetMapping("{userId}/unchecked")
    public ServerResponse<CartVO> unSelectAll(@PathVariable("userId") Integer userId) {
        return cartService.selectOrUnSelect(userId, null, Const.Cart.UN_CHECKED);
    }

    /**
     * 单独选
     */
    @GetMapping("{userId}/checked/{productId}")
    public ServerResponse<CartVO> select(@PathVariable("userId") Integer userId, @PathVariable("productId") Integer productId) {
        return cartService.selectOrUnSelect(userId, productId, Const.Cart.CHECKED);
    }

    /**
     * 单独反选
     */
    @GetMapping("{userId}/unchecked/{productId}")
    public ServerResponse<CartVO> unSelect(@PathVariable("userId") Integer userId, Integer productId) {
        return cartService.selectOrUnSelect(userId, productId, Const.Cart.UN_CHECKED);
    }

    /**
     * 查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.
     */
    @GetMapping("{userId}/counts")
    public ServerResponse<Integer> getCartProductCount(@PathVariable("userId") Integer userId) {
        return cartService.getCartProductCount(userId);
    }


}
