package com.xie.front.controller;

import com.xie.api.model.RechargeRecord;
import com.xie.front.vo.RechargeInfo;
import com.xie.front.vo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "充值功能")
@RestController
@RequestMapping("/v1")
public class RechargeController extends BaseController{

    @ApiOperation(value = "充值数据查询",notes = "根据uid来查询")
    @GetMapping("/recharge/query")
    public RespResult queryRechargeByUid(@RequestHeader("uid") Integer uid,
                                         @RequestParam(value = "pageNo",required = false,defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "6") Integer pageSize){

        RespResult respResult = RespResult.fail();
        //判断uid是否为空
        if (uid != null && uid > 0) {
            List<RechargeRecord> rechargeRecords = rechargeService.queryRechargeByUid(uid, pageNo, pageSize);
            //不能给用户所有的数据，给页面展示的就行
            if(rechargeRecords != null){
                respResult = RespResult.ok();
                respResult.setList(toView(rechargeRecords));
            }


        }


        return respResult;
    }

    private List<RechargeInfo> toView(List<RechargeRecord> rechargeRecords){
        List<RechargeInfo> rechargeInfoList = new ArrayList<>();
        rechargeRecords.forEach(rechargeRecord -> {
            rechargeInfoList.add(new RechargeInfo(rechargeRecord));
        });

        return rechargeInfoList;
    }
}
