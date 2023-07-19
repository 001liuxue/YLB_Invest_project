package com.xie.front.controller;

import com.xie.api.model.ProductInfo;
import com.xie.api.service.ProductInfoService;
import com.xie.api.vo.InvestRecordInfo;
import com.xie.api.vo.MultProductInfo;
import com.xie.common.constants.YLBConstants;
import com.xie.common.enums.Rcode;
import com.xie.common.utils.Utils;
import com.xie.front.vo.PageInfo;
import com.xie.front.vo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductInfoController extends BaseController{

    //显示产品信息
    @ApiOperation(value = "首页三类产品列表", notes = "一个新手宝，三个优选，三个散标")
    @GetMapping("/plat/index")
    public RespResult queryIndexPageProducts() {
        MultProductInfo multProductInfo = productInfoService.queryIndexPageProducts();

        //创建RespResult对象加入数据返回
        RespResult respResult = new RespResult();
        respResult.setCode(200);
        respResult.setMsg("查询首页产品信息成功");
        respResult.setData(multProductInfo);

        return respResult;
    }


    @ApiOperation(value = "产品更多信息")
    //根据产品类型分页查询
    @GetMapping("/product/list")
    public RespResult queryProductByType(@RequestParam("pType") Integer pType,
                                         @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {

        RespResult respResult = RespResult.fail();
        if (pType == 0 || pType == 1 || pType == 2) {
            pageNo = Utils.defaultPageNo(pageNo);
            pageSize = Utils.defaultPageSize(pageSize);

            //分页处理,记录总数
            Integer recordSum = productInfoService.queryProductSumByType(pType);
            if (recordSum > 0) {
                List<ProductInfo> productInfos = productInfoService.queryByTypeLimit(pType, pageNo, pageSize);
                respResult = RespResult.ok();
                respResult.setList(productInfos);

                PageInfo page = new PageInfo(pageNo, pageSize, recordSum);
                respResult.setPageInfo(page);
            }

        } else {
            //请求参数有误
            respResult.setRcode(Rcode.PRODUCT_PTYPE_ERR);
        }

        return respResult;

    }


    @ApiOperation(value = "查看产品详情")
    //根据产品类型分页查询
    @GetMapping("/product/detail")
    public RespResult queryProductById(@RequestParam("productId") Integer id) {
        RespResult respResult = RespResult.fail();
        //id不为null,从数据库中获取数据
        if (id != null && id > 0) {
            ProductInfo productInfo = productInfoService.queryById(id);

            //产品存在
            if (productInfo != null) {
                respResult = RespResult.ok();
                respResult.setData(productInfo);
                List<InvestRecordInfo> investRecordInfoList = investRecordService.queryById(id, 1, 5);
                respResult.setList(investRecordInfoList);
            }else {
                respResult.setRcode(Rcode.PRODUCT_OFFLINE);
            }
        }


        return respResult;
    }
}