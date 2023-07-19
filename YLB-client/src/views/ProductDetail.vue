<template>
    <div>
        <Header></Header>

        <div class="content clearfix">
            <div class="detail-left">
                <div class="detail-left-title" v-if="this.productInfo.productType == 0">新手宝（20230619期）</div>
                <div class="detail-left-title" v-else-if="this.productInfo.productType == 1">满月宝（20230619期）</div>
                <div class="detail-left-title" v-else="this.productInfo.productType == 2">年季宝（20230619期）</div>
                <ul class="detail-left-number">
                    <li>
                        <span>历史年化收益率</span>
                        <p><b>{{ productInfo.rate }}</b>%</p>
                        <span>历史年化收益率</span>
                    </li>
                    <li>
                        <span>募集金额（元）</span>
                        <p><b>{{ productInfo.productMoney }}</b>元</p>
                        <span>募集中&nbsp;&nbsp;剩余募集金额{{ productInfo.leftProductMoney }}元</span>
                    </li>
                    <li>
                        <span>投资周期</span>
                        <p v-if="productInfo.productType == 0"><b>{{ productInfo.cycle }}</b>天</p>
                        <p v-else><b>{{ productInfo.cycle }}</b>个月</p>
                    </li>

                </ul>
                <div class="detail-left-way">
                    <span>收益获取方式</span>
                    <span>收益返还：<i>到期还本付息</i></span>
                </div>
                <!--投资记录-->
                <div class="datail-record">
                    <h2 class="datail-record-title">投资记录</h2>
                    <div class="datail-record-list">
                        <table align="center" width="880" border="0" cellspacing="0" cellpadding="0">
                            <colgroup>
                                <col style="width: 72px" />
                                <col style="width: 203px" />
                                <col style="width: 251px" />
                                <col style="width: 354px" />
                            </colgroup>
                            <thead class="datail_thead">
                                <tr>
                                    <th>序号</th>
                                    <th>投资人</th>
                                    <th>投资金额（元）</th>
                                    <th>投资时间</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="item,index in investRecordInfoList" :key="item.id">
                                    <td>{{index+1}}</td>
                                    <td class="datail-record-phone">{{ item.phone }}</td>
                                    <td>{{ item.bidMoney }}</td>
                                    <td>{{ item.bidTime }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
            <!--右侧-->
            <div class="detail-right">
                <div class="detail-right-title">立即投资</div>
                <div class="detail-right-mode">
                    <h3 class="detail-right-mode-title">收益方式</h3>
                    <p class="detail-right-mode-p"><span>到期还本付息</span></p>
                    <h3 class="detail-right-mode-title">我的账户可用</h3>

                    <div class="detail-right-mode-rmb" v-if="logined == false">
                        <p>资金（元）：******</p>
                        <a href="javascript:void(0)" @click="goLink('/page/user/login')">请登录</a>
                    </div>

                    <div class="detail-right-mode-rmb" v-else>
                        <p>资金（元）：{{ money }}</p>
                    </div>

                    <h3 class="detail-right-mode-title">预计本息收入 {{ income }}（元）</h3>
                    <form action="" id="number_submit">
                        <p>请在下方输入投资金额</p>
                        <input type="text" placeholder="请输入日投资金额，应为100元整倍数" v-model="invest" @blur="checkInvest()" name="" class="number-money" >
                        <div class="err">{{ investErr }}</div>
                        <input type="button" value="立即投资" @click="investProduct()" class="submit-btn">                  
                    </form>

                </div>
            </div>

        
        </div>
        <Footer></Footer>
    </div>

</template>

<script>
import Header from "@/components/common/Header.vue"
import Footer from "@/components/common/Footer.vue"
import {doGet, doPost} from "@/api/httpRequest"
import layx from 'vue-layx'

export default {
    data() {
        return {
            productId:0,
            productInfo:{
                "productType":0,
                "rate": 0.00,
                "cycle": 0,
                "productMoney": 0.00,
                "leftProductMoney": 0.00,
            },
            investRecordInfoList:[{
                "phone": "***********",
                "bidMoney": 0.00,
                "bidTime": ""
            }],
            logined:false,
            money:0.0,
            invest: "",
            investErr:'',
            income:''
            
        }
    },
    components:{
        Header,
        Footer
    },
    
    mounted(){
        if(window.localStorage.getItem('userInfo')){
            this.logined = true;
        }
        // console.log(this.$route.query.productId)
        this.init()
    },
    methods: {
        init(){
            let productId = this.$route.query.productId
            this.productId = productId
            doGet('/v1/product/detail',{productId:this.productId}).then(res=>{
                // console.log(res.data.data)
                this.productInfo = res.data.data
                this.investRecordInfoList = res.data.list
            })

            doGet('/v1/user/center').then(res=>{
                if(res && res.data.code == 200){
                    this.money = res.data.data.money
                }
            })
        },
        goLink(url,params){
            this.$router.push({
                path: url,
                query: params
            })
        },
        checkInvest(){
            if(isNaN(this.invest)){
                this.investErr = '输入金额必须是数字'
            }else if(this.invest < 100){
                this.investErr = '输入金额最少是100元'
            }else if(parseFloat(this.invest) % 100 != 0){
                this.investErr = '输入金额必须是100元的整数倍'
            }else{
                this.investErr = ''
                //计算利息显示在页面
                //计算日利率
                let dayRate = this.productInfo.rate / 365 / 100
                let cycle = this.productInfo.cycle
                if(this.productInfo.productType == 0){
                    this.income = parseFloat(this.invest) * dayRate * cycle
                }else if(this.productInfo.productType == 1){
                    this.income = parseFloat(this.invest) * dayRate * cycle * 30
                }else{
                    this.income = parseFloat(this.invest) * dayRate * cycle * 365
                }

                this.income = this.income.toFixed(2)
            }
        },
        investProduct(){
            if(this.investErr == ''){
                let user = JSON.parse(window.localStorage.getItem('userInfo'))
                //判断是否已经登录
                if(user){
                    if(user.name != ''){
                        doPost('/v1/invest/product',{productId:this.productId,money:this.invest}).then(res=>{
                            if(res && res.data.code == 200){
                                this.init()
                            }else{
                                let msg = res.data.msg
                                layx.msg(msg,{dialogIcon:'warn',position:'ct'})
                            }
                        })
                    }else{
                        layx.msg("投资前请先实名认证",{dialogIcon:'warn',position:'ct'})
                    }   
                }else{
                    layx.msg("请先登录",{dialogIcon:'warn',position:'ct'})
                }
            }
        }
    }

}
</script>

<style>
.err{
    color:red;
    font-size: small;
}
</style>