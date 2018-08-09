package com.duol.controller.backend;

import com.duol.common.ServerResponse;
import com.duol.service.OrderService;
import com.duol.vo.OrderVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Duolaimon
 * 18-7-17 下午3:41
 */
@Controller
@RequestMapping("/manage")
public class OrderManageController {

    private final OrderService orderService;

    @Autowired
    public OrderManageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return orderService.manageList(pageNum, pageSize);
    }

    @GetMapping("/orders/detail")
    @ResponseBody
    public ServerResponse<OrderVO> orderDetail(Long orderNo) {
        return orderService.manageDetail(orderNo);
    }

    /**
     *  根据订单号获取订单中所有商品的订单信息
     * @param orderNo  订单号
     */
    @GetMapping("orders/{orderNo}")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(@PathVariable("orderNo") Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return orderService.manageSearch(orderNo, pageNum, pageSize);
    }


    @PutMapping("orders/{orderNo}")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(@PathVariable("orderNo") Long orderNo) {
        return orderService.manageSendGoods(orderNo);
    }

}
