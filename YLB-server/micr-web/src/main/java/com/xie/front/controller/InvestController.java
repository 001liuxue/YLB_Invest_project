package com.xie.front.controller;

import com.xie.api.model.User;
import com.xie.common.constants.RedisConstants;
import com.xie.common.utils.Utils;
import com.xie.front.vo.InvestRankInfo;
import com.xie.front.vo.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Api(tags = "投资理财产品")
@RestController
@RequestMapping("/v1")
public class InvestController extends BaseController{


    @ApiOperation(value = "投资产品排行榜")
    @GetMapping("/invest/rank")
    public RespResult queryInvestRank(){
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate
                .boundZSetOps(RedisConstants.KEY_INVEST_RANK).reverseRangeWithScores(0, 2);

        //创建InvestRankInfo集合,后续存入RespResult对象中
        List<InvestRankInfo> list = new ArrayList<>();

        typedTuples.forEach(typedTuple -> {
//            typedTuple.getValue();//键值
//            typedTuple.getScore();//分数
            list.add(new InvestRankInfo(Utils.desensitizePhone(typedTuple.getValue()),typedTuple.getScore()));
        });

        RespResult respResult = RespResult.ok();
        respResult.setList(list);
        return respResult;
    }



    @ApiOperation(value = "投资产品")
    @PostMapping("/invest/product")
    public RespResult investProduct(@RequestHeader("uid") Integer uid,
                                    @RequestParam("productId") Integer productId,
                                    @RequestParam("money")BigDecimal money){
        RespResult respResult = RespResult.fail();
        //判断参数是否符合
        if ((uid != null && uid > 0) && (productId != null && productId > 0)
                && (money != null && money.intValue() % 100 == 0 && money.intValue() > 0)) {

            int result = investService.investProduct(uid,productId,money);
            if(result == 1){
                respResult = RespResult.ok();
                modifyInvestRank(uid, money);
            }else if(result == 2){
                respResult.setMsg("资金账号不存在");
            }else if(result == 3){
                respResult.setMsg("余额不足");
            }else {
                respResult.setMsg("理财产品不存在");
            }

        }
        return respResult;
    }


    //更新投资排行榜
    private void modifyInvestRank(Integer uid,BigDecimal money){
        User user = userService.queryById(uid);
        if (user != null) {
            String key = RedisConstants.KEY_INVEST_RANK;
            stringRedisTemplate.boundZSetOps(key).incrementScore(user.getPhone(), money.doubleValue());
        }
    }
}
