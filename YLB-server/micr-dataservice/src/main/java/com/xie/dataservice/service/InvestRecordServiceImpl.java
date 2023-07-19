package com.xie.dataservice.service;

import com.xie.api.service.InvestRecordService;
import com.xie.api.vo.InvestRecordInfo;
import com.xie.common.utils.Utils;
import com.xie.dataservice.mapper.BidInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = InvestRecordService.class,version = "1.0")
public class InvestRecordServiceImpl implements InvestRecordService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Override
    public List<InvestRecordInfo> queryById(Integer productId, Integer pageNo, Integer pageSize) {
        List<InvestRecordInfo>  InvestRecordInfoList = new ArrayList<>();
        if (productId != null) {
            pageNo = Utils.defaultPageNo(pageNo);
            pageSize = Utils.defaultPageSize(pageSize);
            Integer offset = (pageNo - 1) * pageSize;

            InvestRecordInfoList = bidInfoMapper.selectById(productId,offset,pageSize);

        }
        return InvestRecordInfoList;
    }
}
