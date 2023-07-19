package com.xie.dataservice.service;

import com.xie.api.model.BidInfo;
import com.xie.api.model.IncomeRecord;
import com.xie.api.model.ProductInfo;
import com.xie.api.service.IncomeService;
import com.xie.common.constants.YLBConstants;
import com.xie.dataservice.mapper.BidInfoMapper;
import com.xie.dataservice.mapper.FinanceAccountMapper;
import com.xie.dataservice.mapper.IncomeRecordMapper;
import com.xie.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    //生成收益计划
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void generateIncomePlan() {
        //获取当前时间
        Date cur = new Date();

        //开始时间
        Date beginTime = DateUtils.truncate(DateUtils.addDays(cur, -1), Calendar.DATE);
        //结束时间
        Date endTime = DateUtils.truncate(cur, Calendar.DATE);

        //查询所有满足满标状态并需要计算生成收益的产品列表
        List<ProductInfo> productInfoList = productInfoMapper.selectByTime(beginTime,endTime);

        BigDecimal income = null;
        BigDecimal rate = null;
        BigDecimal cycle = null;

        //到期时间
        Date incomeDate = null;

        int rows = 0;

        //遍历当前产品列表,计算收益,并更新收益表
        for(ProductInfo productInfo : productInfoList){

            //计算当前产品的日利率
            rate = productInfo.getRate().divide(new BigDecimal("360"),10, RoundingMode.HALF_UP)
                                        .divide(new BigDecimal(100),10,RoundingMode.HALF_UP);

            //获取当前产品的周期并计算发放收益日期
            if(productInfo.getProductType() == YLBConstants.PRODUCT_TYPE_XINSHOUBAO){
                cycle = new BigDecimal(productInfo.getCycle());
                incomeDate = DateUtils.addDays(productInfo.getProductFullTime(),(1+productInfo.getCycle()));
            }else if(productInfo.getProductType() == YLBConstants.PRODUCT_TYPE_YOUXUAN){
                //获取当前产品的周期
                cycle = new BigDecimal(productInfo.getCycle() * 30);
                incomeDate = DateUtils.addDays(productInfo.getProductFullTime(),(1+productInfo.getCycle() * 30));

            }else {
                //获取当前产品的周期
                cycle = new BigDecimal(productInfo.getCycle() * 365);
                incomeDate = DateUtils.addDays(productInfo.getProductFullTime(),(1+productInfo.getCycle() * 365));
            }

            //首先根据产品id来查询所有的投资记录列表
            //给每一个投资记录生成收益
            List<BidInfo> bidInfoList = bidInfoMapper.selectByProductId(productInfo.getId());
            //遍历投资记录列表
            for(BidInfo bidInfo:bidInfoList){
                //计算收益:
                //利息 = 本金 * 利率 * 周期
                income = bidInfo.getBidMoney().multiply(rate).multiply(cycle);

                //生成收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(income);
                incomeRecord.setProdId(productInfo.getId());
                incomeRecord.setIncomeStatus(YLBConstants.INCOME_SEND_PLAN);
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());

                //加入收益记录表
                incomeRecordMapper.insertSelective(incomeRecord);
            }

            //还需要更新产品表里的产品状态值
            rows = productInfoMapper.updateByProductId(productInfo.getId());
            if (rows < 1){
                throw new RuntimeException("更新产品状态值失败");
            }
        }


    }

    //返还本息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backIncome(){
        //获取当前时间
        Date cur = new Date();
        Date yesDay = DateUtils.truncate(DateUtils.addDays(cur,-1),Calendar.DATE);
        //获取当天前一天的要返还收益的收益记录列表
        List<IncomeRecord> incomeRecordList =  incomeRecordMapper.selectByTimeAndStatus(yesDay);

        int rows = 0;
        //遍历
        for(IncomeRecord incomeRecord : incomeRecordList){
            //返还收益 : 本金 + 收益
            //根据用户id来返还
            rows = financeAccountMapper.updateAccountByUid(incomeRecord.getUid(),incomeRecord.getBidMoney(),incomeRecord.getIncomeMoney());
            if(rows < 1){
                throw new RuntimeException("更新用户资金账户失败");
            }

            //更新收益返还表
            //将状态改为1
            rows = incomeRecordMapper.updateStatusById(incomeRecord.getId());
            if(rows < 1){
                throw new RuntimeException("更新收益记录状态失败");
            }
        }
    }
}
