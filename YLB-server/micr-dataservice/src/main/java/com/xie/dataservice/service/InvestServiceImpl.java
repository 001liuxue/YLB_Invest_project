package com.xie.dataservice.service;

import com.xie.api.model.BidInfo;
import com.xie.api.model.FinanceAccount;
import com.xie.api.model.ProductInfo;
import com.xie.api.service.InvestService;
import com.xie.common.constants.YLBConstants;
import com.xie.common.utils.Utils;
import com.xie.dataservice.mapper.BidInfoMapper;
import com.xie.dataservice.mapper.FinanceAccountMapper;
import com.xie.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = InvestService.class,version = "1.0")
public class InvestServiceImpl implements InvestService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int investProduct(Integer uid,Integer productId, BigDecimal money) {
        //默认
        int result = 0;

        int rows = 0;

        if ((uid != null && uid > 0) && (productId != null && productId > 0)
                && (money != null && money.intValue() % 100 == 0 && money.intValue() > 0)) {

            //根据uid来查询用户的余额与要充值的金额相比，是否满足充值条件
            FinanceAccount financeAccount = financeAccountMapper.selectByUid(uid);
            if (Utils.ge(financeAccount.getAvailableMoney(),money)) {
                //余额足够，可以充值
                //检查产品剩余资金和最小充值，最大充值条件是否满足
                ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
                if(productInfo != null && productInfo.getProductStatus() == YLBConstants.PRODUCT_STATUS_SELLING){
                    if (Utils.ge(productInfo.getLeftProductMoney(),money)
                            && Utils.ge(money,productInfo.getBidMinLimit())
                            && Utils.ge(productInfo.getBidMaxLimit(),money)) {
                        //条件满足，可以购买
                        //更新用户的余额账户
                        rows = financeAccountMapper.updateByUid(uid,money);
                        if(rows < 1){
                            //更新失败
                            throw new RuntimeException("更新账户失败");
                        }

                        rows = productInfoMapper.updateById(productId,money);
                        if(rows < 1){
                            //更新失败
                            throw new RuntimeException("更新产品信息失败");
                        }

                        //更新成功后，创建投资记录
                        BidInfo bidInfo = new BidInfo();
                        bidInfo.setBidMoney(money);
                        bidInfo.setUid(uid);
                        bidInfo.setProdId(productId);
                        bidInfo.setBidTime(new Date());
                        bidInfo.setBidStatus(YLBConstants.INVEST_STATUS_SUCC);
                        //更新bid表
                        bidInfoMapper.insertSelective(bidInfo);

                        //要根据产品还能投资的金额来判断产品目前的状态是否为满标状态
                        ProductInfo dbProductInfo = productInfoMapper.selectByPrimaryKey(productId);
                        if(dbProductInfo.getLeftProductMoney().compareTo(new BigDecimal("0")) == 0){
                            //如果是，说明已满标
                            rows = productInfoMapper.updateSelled(productId);
                            if(rows < 1){
                                //更新失败
                                throw new RuntimeException("满标更新失败");
                            }
                        }
                        result = 1;
                    }
                }else {
                    //理财产品不存在
                    result = 4;
                }
            }else {
                //余额不足
                result = 3;
            }
        }else {
            //资金账号不存在
            result = 2;
        }


        return result;
    }
}
