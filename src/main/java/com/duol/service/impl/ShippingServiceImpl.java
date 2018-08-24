package com.duol.service.impl;

import com.duol.common.ServerResponse;
import com.duol.dao.ShippingMapper;
import com.duol.pojo.Shipping;
import com.duol.service.ShippingService;
import com.duol.util.BaseVOUtil;
import com.duol.vo.ShippingVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Duolaimon
 * 18-7-16 上午9:42
 */
@Service
public class ShippingServiceImpl implements ShippingService {
    private final ShippingMapper shippingMapper;

    @Autowired
    public ShippingServiceImpl(ShippingMapper shippingMapper) {
        this.shippingMapper = shippingMapper;
    }

    @Override
    public ServerResponse<Integer> add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insertSelective(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("新建地址成功",shipping.getId());
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        List<ShippingVO> shippingVOList = shippingList.stream()
                .map(shipping -> BaseVOUtil.parse(shipping,ShippingVO.class))
                .collect(Collectors.toList());
        PageInfo<ShippingVO> pageInfo = new PageInfo<>(shippingVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
