package com.duol.vo;

import com.duol.common.Const;
import com.duol.dao.CartMapper;
import com.duol.dao.ProductMapper;
import com.duol.pojo.Cart;
import com.duol.pojo.Product;
import com.duol.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * @author Duolaimon
 * 18-7-15 下午8:50
 */
public class CartProductVO {
    //结合了产品和购物车的一个抽象对象

    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//购物车中此商品的数量
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    private Integer productChecked;//此商品是否勾选

    private String limitQuantity;//限制数量的一个返回结果


    public static CartProductVO getCartProductVo(Integer userId, CartMapper cartMapper, ProductMapper productMapper, Cart cartItem) {
        CartProductVO cartProductVo = new CartProductVO();
        cartProductVo.setId(cartItem.getId());
        cartProductVo.setUserId(userId);
        cartProductVo.setProductId(cartItem.getProductId());

        Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
        if (product != null) {
            cartProductVo.setProductMainImage(product.getMainImage());
            cartProductVo.setProductName(product.getName());
            cartProductVo.setProductSubtitle(product.getSubtitle());
            cartProductVo.setProductStatus(product.getStatus());
            cartProductVo.setProductPrice(product.getPrice());
            cartProductVo.setProductStock(product.getStock());
            //判断库存
            int buyLimitCount;
            if (product.getStock() >= cartItem.getQuantity()) {
                //库存充足的时候
                buyLimitCount = cartItem.getQuantity();
                cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
            } else {
                buyLimitCount = product.getStock();
                cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                //购物车中更新有效库存
                updateQuantity(cartItem, buyLimitCount, cartMapper);
            }
            cartProductVo.setQuantity(buyLimitCount);
            //计算总价
            cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
            cartProductVo.setProductChecked(cartItem.getChecked());
        }
        return cartProductVo;
    }

    private static void updateQuantity(Cart cartItem, int buyLimitCount, CartMapper cartMapper) {
        Cart cartForQuantity = new Cart();
        cartForQuantity.setId(cartItem.getId());
        cartForQuantity.setQuantity(buyLimitCount);
        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductChecked() {
        return productChecked;
    }

    public void setProductChecked(Integer productChecked) {
        this.productChecked = productChecked;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }
}
