package com.duol.controller.portal;

import com.duol.common.ServerResponse;
import com.duol.pojo.Shipping;
import com.duol.service.ShippingService;
import com.duol.util.BaseVOUtil;
import com.duol.vo.ShippingVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams
            ({@ApiImplicitParam(name = "shipping", value = "地址信息", dataType = "ShippingVO"),
                    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "path")})
    public ServerResponse add(@RequestBody ShippingVO shipping, @PathVariable("userId") Integer userId) {
        return shippingService.add(userId, BaseVOUtil.parse(shipping,Shipping.class));
    }


    @DeleteMapping("{userId}")
    public ServerResponse del(Integer shippingId, @PathVariable("userId") Integer userId) {
        return shippingService.del(userId, shippingId);
    }

    @PutMapping("{userId}")
    public ServerResponse update(ShippingVO shipping, @PathVariable("userId") Integer userId) {
        return shippingService.update(userId, BaseVOUtil.parse(shipping,Shipping.class));
    }


    @GetMapping("{userId}/{shippingId}")
    public ServerResponse<ShippingVO> select(@PathVariable("shippingId") Integer shippingId,
                                           @PathVariable("userId") Integer userId) {
        ServerResponse response = shippingService.select(userId, shippingId);
        ShippingVO shippingVO = BaseVOUtil.parse(response.getData(),ShippingVO.class);
        return ServerResponse.createBySuccess(shippingVO);
    }


    @GetMapping("{userId}")
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @PathVariable("userId") Integer userId) {
        return shippingService.list(userId, pageNum, pageSize);
    }


}
