package com.xie.dataservice.service;

import com.xie.api.service.BaseInfoService;
import com.xie.api.vo.BaseInfo;
import com.xie.dataservice.mapper.BidInfoMapper;
import com.xie.dataservice.mapper.ProductInfoMapper;
import com.xie.dataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@DubboService(interfaceClass = BaseInfoService.class,version = "1.0")
public class BaseInfoServiceImpl implements BaseInfoService {
    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseInfo queryBaseInfo() {
        //查询累计金额、用户总数、产品平均利率
        BigDecimal sumBidMoney = bidInfoMapper.querySumBidMoney();
        BigDecimal aveRate = productInfoMapper.queryAveRate();
        int userCount = userMapper.countUser();

        //创建BaeInfo对象
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setAvgRate(aveRate);
        baseInfo.setSumBidMoney(sumBidMoney);
        baseInfo.setUserCount(userCount);
        return baseInfo;
    }
}
