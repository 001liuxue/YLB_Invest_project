package com.xie.front.controller;

import com.xie.api.vo.BaseInfo;
import com.xie.front.vo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "平台信息功能")
@RestController
@RequestMapping("/v1")
public class BaseInfoController extends BaseController {

    @ApiOperation(value = "平台三项基本功能",notes = "平均利率,用户总数,累计金额")
    @GetMapping("/plat/info")
    public RespResult queryBaseInfo(){
        BaseInfo baseInfo = baseInfoService.queryBaseInfo();
        RespResult respResult = new RespResult();

        respResult.setCode(200);
        respResult.setMsg("信息查询成功");
        respResult.setData(baseInfo);

        return respResult;
    }
}
