package com.duol.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.service.OrderService;
import com.duol.vo.OrderProductVO;
import com.duol.vo.OrderVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Duolaimon
 * 18-7-17 上午9:14
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @ApiOperation("创建订单")
    @PostMapping("/{shippingId}/{userId}")
    @ApiImplicitParam(name = "productIds",value = "用逗号分割要生成订单的产品id",paramType = "query")
    public ServerResponse<OrderVO> create(@PathVariable("shippingId") Integer shippingId,
                                          @PathVariable("userId") Integer userId,
                                          @RequestParam("productIds") String productIds) {
        return orderService.createOrder(userId, shippingId, productIds);
    }


    @ApiOperation("取消订单")
    @DeleteMapping("/{orderNo}/{userId}")
    public ServerResponse<String> cancel(@PathVariable("orderNo") Long orderNo,
                                 @PathVariable("userId") Integer userId) {
        return orderService.cancel(userId, orderNo);
    }


    @ApiOperation("从购物车中获取数据")
    @GetMapping("/carts/{userId}")
    public ServerResponse<OrderProductVO> getOrderCartProduct(@PathVariable("userId") Integer userId) {
        return orderService.getOrderCartProduct(userId);
    }


    @ApiOperation("查询订单信息")
    @GetMapping("/{orderNo}/{userId}")
    public ServerResponse detail(@PathVariable("orderNo") Long orderNo,
                                 @PathVariable("userId") Integer userId) {
        return orderService.getOrderDetail(userId, orderNo);
    }

    @ApiOperation("查询所有订单")
    @GetMapping("/{userId}")
    public ServerResponse list(@PathVariable("userId") Integer userId,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return orderService.getOrderList(userId, pageNum, pageSize);
    }


    @ApiOperation("支付")
    @PutMapping("/{orderNo}/{userId}")
    @ResponseBody
    public ServerResponse pay(@PathVariable("orderNo") Long orderNo, HttpServletRequest request, @PathVariable("userId") Integer userId) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(orderNo, userId, path);
    }

    @ApiOperation("回调")
    @GetMapping("/alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

        //todo 验证各种数据


        //
        ServerResponse serverResponse = orderService.aliCallback(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }


    @ApiOperation("查询支付状态")
    @GetMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(Long orderNo, @RequestParam("userId") Integer userId) {
        ServerResponse serverResponse = orderService.queryOrderPayStatus(userId, orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


}
