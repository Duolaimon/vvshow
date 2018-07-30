package com.duol.controller.portal;

import com.duol.common.ServerResponse;
import com.duol.pojo.Shipping;
import com.duol.service.ShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Duolaimon
 * 18-7-16 上午9:41
 */
@RestController
@RequestMapping("/shipping/")
public class ShippingController {

    private final ShippingService shippingService;

    @Autowired
    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }


    @PostMapping("{userId}")
    @ResponseBody
    public ServerResponse add(Shipping shipping, @PathVariable("userId") Integer userId) {
        return shippingService.add(userId, shipping);
    }


    @DeleteMapping("{userId}")
    @ResponseBody
    public ServerResponse del(Integer shippingId, @PathVariable("userId") Integer userId) {
        return shippingService.del(userId, shippingId);
    }

    @PutMapping("{userId}")
    @ResponseBody
    public ServerResponse update(Shipping shipping, @PathVariable("userId") Integer userId) {
        return shippingService.update(userId, shipping);
    }


    @GetMapping("{userId}/{shippingId}")
    @ResponseBody
    public ServerResponse<Shipping> select(@PathVariable("shippingId") Integer shippingId,
                                           @PathVariable("userId") Integer userId) {
        return shippingService.select(userId, shippingId);
    }


    @GetMapping("{userId}")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @PathVariable("userId") Integer userId) {
        return shippingService.list(userId, pageNum, pageSize);
    }


}
