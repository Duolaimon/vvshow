package com.duol.controller.portal;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.service.CartService;
import com.duol.vo.CartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Duolaimon
 * 18-7-15 下午9:30
 */
@RestController
@RequestMapping("/carts/")
@Api(value = "购物车")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @ApiOperation("获取购物车")
    @GetMapping("/{userId}")
    public ServerResponse<CartVO> list(@PathVariable("userId") Integer userId) {
        return cartService.list(userId);
    }

    @ApiOperation("加入购物车")
    @PostMapping("{userId}/{productId}")
    public ServerResponse<CartVO> add(@PathVariable("userId") Integer userId,
                                      @RequestParam("count") Integer count,
                                      @PathVariable("productId") Integer productId) {
        return cartService.add(userId, productId, count);
    }


    @ApiOperation("修改购物车")
    @PutMapping("{userId}/{productId}")
    public ServerResponse<CartVO> update(@PathVariable("userId") Integer userId,
                                         @RequestParam("count") Integer count,
                                         @PathVariable("productId")Integer productId) {
        return cartService.update(userId, productId, count);
    }

    @ApiOperation("删除购物车内指定商品")
    @DeleteMapping("{userId}")
    @ApiImplicitParam(name = "productIds",defaultValue = "21,23,34",value = "用逗号分割要删除的产品id",paramType = "query")
    public ServerResponse<CartVO> deleteProduct(@PathVariable("userId") Integer userId,
                                                @RequestParam("productIds")String productIds) {
        return cartService.deleteProduct(userId, productIds);
    }

    /**
     * 全选
     */
    @ApiOperation("全选购物车内商品条例")
    @GetMapping("{userId}/checked")
    public ServerResponse<CartVO> selectAll(@PathVariable("userId") Integer userId) {
        return cartService.selectOrUnSelect(userId, null, Const.Cart.CHECKED);
    }

    /**
     * 全反选
     */
    @ApiOperation("全反选购物车内商品条例")
    @GetMapping("{userId}/unchecked")
    public ServerResponse<CartVO> unSelectAll(@PathVariable("userId") Integer userId) {
        return cartService.selectOrUnSelect(userId, null, Const.Cart.UN_CHECKED);
    }

    /**
     * 单独选
     */
    @ApiOperation("单选购物车内商品条例")
    @GetMapping("{userId}/checked/{productId}")
    public ServerResponse<CartVO> select(@PathVariable("userId") Integer userId,
                                         @PathVariable("productId") Integer productId) {
        return cartService.selectOrUnSelect(userId, productId, Const.Cart.CHECKED);
    }

    /**
     * 单独反选
     */
    @ApiOperation("单反选购物车内商品条例")
    @GetMapping("{userId}/unchecked/{productId}")
    public ServerResponse<CartVO> unSelect(@PathVariable("userId") Integer userId,
                                           @PathVariable("productId")Integer productId) {
        return cartService.selectOrUnSelect(userId, productId, Const.Cart.UN_CHECKED);
    }

    /**
     * 查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.
     */
    @ApiOperation("查询购物车内产品数量")
    @GetMapping("{userId}/counts")
    public ServerResponse<Integer> getCartProductCount(@PathVariable("userId") Integer userId) {
        return cartService.getCartProductCount(userId);
    }


}
