package com.xie.dataservice.service;

import com.xie.api.model.FinanceAccount;
import com.xie.api.model.RechargeRecord;
import com.xie.api.service.RechargeService;
import com.xie.common.constants.YLBConstants;
import com.xie.common.utils.Utils;
import com.xie.dataservice.mapper.FinanceAccountMapper;
import com.xie.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<RechargeRecord> queryRechargeByUid(Integer uid,Integer pageNo,Integer pageSize) {
        List<RechargeRecord> rechargeRecords = null;
        //判断uid是否为空并且是否大于0
        if (uid != null) {
            Integer defaultPageNo = Utils.defaultPageNo(pageNo);
            Integer defaultPageSize = Utils.defaultPageSize(pageSize);
            Integer offset = (defaultPageNo - 1) * defaultPageSize;

            rechargeRecords = rechargeRecordMapper.selectRechargeByUid(uid,offset,pageSize);
        }

        return rechargeRecords;
    }

    @Override
    public int addRechargeRecord(RechargeRecord record) {
        int rows = rechargeRecordMapper.insert(record);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int handleKQNotify(String orderId, String payAmount, String payResult) {
        int result = 0;
        int rows = 0;
        //1.查询订单是否存在
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectRechargeByOrderId(orderId);
        if(rechargeRecord != null){
            //2.判断当前订单是否正在被处理
            if(rechargeRecord.getRechargeStatus() == YLBConstants.RECHARGE_STATUS_PROCESSING){
                //3.判断金额是否正确
                String fen = rechargeRecord.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                if(fen.equals(payAmount)){
                    //4.判断返回结果是否为10
                    if("10".equals(payResult)){
                        //5.更新用户账户金额
                        rows = financeAccountMapper.updateRechargeAccount(rechargeRecord.getUid(),rechargeRecord.getRechargeMoney());
                        if(rows < 1){
                            throw new RuntimeException("更新充值数据库失败");
                        }

                        //更新订单表状态为充值成功
                        rows = rechargeRecordMapper.updateStatusById(rechargeRecord.getId(),YLBConstants.RECHARGE_STATUS_SUCC);
                        if(rows < 1){
                            throw new RuntimeException("更新订单状态失败");
                        }

                        result = 1;//充值成功
                    }else {
                        //更新订单表状态为充值成功
                        rows = rechargeRecordMapper.updateStatusById(rechargeRecord.getId(),YLBConstants.RECHARGE_STATUS_FAIL);
                        if(rows < 1){
                            throw new RuntimeException("更新订单状态失败");
                        }

                        result = 2;//充值失败
                    }
                }else {
                    result = 4;//金额不正确
                }
            }else {
                result = 3;//订单已经被处理过了
            }

        }

        return result;
    }
}
