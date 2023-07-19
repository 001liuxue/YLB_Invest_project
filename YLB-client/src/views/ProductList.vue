<template>
    <Header></Header>
    <div class="content clearfix">
        <!--排行榜-->
        <ul class="rank-list">
            <li v-for="rank,index in rankList" :key="rank.id">
                <img src="@/assets/image/list-rank1.png" alt="" v-if="index == 0">
                <img src="@/assets/image/list-rank2.png" alt="" v-else-if="index == 1">
                <img src="@/assets/image/list-rank3.png" alt="" v-else="index == 2">
                <p class="rank-list-phone">{{rank.phone}}</p>
                <span>{{rank.money}}元</span>
            </li>
        </ul>
        <!--产品列表-->
        <ul class="preferred-select clearfix">
            <li v-for="product,index in productList" :key="product.id">
                <h3 class="preferred-select-title">
                    <span>{{ product.productName }}</span>
                    <img src="@/assets/image/1-bg1.jpg" alt="" v-if="index%4 == 0">
                    <img src="@/assets/image/1-bg2.jpg" alt="" v-else-if="index%4 == 1">
                    <img src="@/assets/image/1-bg3.jpg" alt="" v-else-if="index%4 == 2">
                    <img src="@/assets/image/1-bg4.jpg" alt="" v-else="index%4 == 3">
                </h3>
                <div class="preferred-select-number">
                    <p><b>{{ product.rate }}</b>%</p>
                    <span>历史年化收益率</span>
                </div>
                <div class="preferred-select-date">
                    <div>
                        <span>投资周期</span>
                        <p><b>{{ product.cycle }}</b>个月</p>
                    </div>
                    <div>
                        <span>剩余可投资金额</span>
                        <p><b>{{ product.leftProductMoney }}</b>元</p>
                    </div>
                </div>
                <p class="preferred-select-txt">
                    优选计划项目，投资回报周期{{ product.cycle }}个月，起点低，适合短期资金周转、对流动性要求高的投资人。
                </p>
                <a href="javascript:void(0)" @click="goLink('/page/product/detail',{productId:product.id})" class="preferred-select-btn">立即投资</a>
            </li>
            
        </ul>

        <!--分页-->
        <div class="page_box">
            <ul class="pagination">
                <li><a href="javascript:void(0)" @click="first">首页</a></li>
                <li><a href="javascript:void(0)" @click="pre">上一页</a></li>
                <li class="active"><span>{{ page.pageNo }}</span></li>
                <li><a href="javascript:void(0)" @click="next">下一页</a></li>
                <li><a href="javascript:void(0)" @click="last">尾页</a></li>
                <li class="totalPages"><span>共{{page.totalPage}}页</span></li>
            </ul>
        </div>

    <Footer></Footer>

</div>
  
</template>

<script>
import Header from "@/components/common/Header.vue"
import Footer from "@/components/common/Footer.vue"
import {doGet} from "@/api/httpRequest"
import layx from 'vue-layx'


let ptype = 0;
export default {
    components:{
        Header,
        Footer
    },
    data() {
        return {
            rankList:[{
                "phone": "***********",
                "money": 0.00
            }],
            productList:[{
                "id": 0,
                "productName": "",
                "rate": 0.00,
                "cycle": 0,
                "releaseTime": 0,
                "productType": 0,
                "productNo": "",
                "productMoney": 0.00,
                "leftProductMoney": 0.00,
                "bidMinLimit": 0.00,
                "bidMaxLimit": 0.00,
                "productStatus": 0,
                "productFullTime": "",
                "productDesc": "",
                "version": 0
            }],
            page:{
                "pageNo": 1,
                "pageSize": 9,
                "totalPage": 1,
                "recordSum": 0
            }
        }
    },
    mounted(){
        ptype = this.$route.query.pType
        this.init(ptype,1,9)

        //获取排行榜
        doGet('/v1/invest/rank').then(res=>{
                // console.log(res)
                this.rankList = res.data.list
            })
    },
    methods:{
        init(ptype,pageNo,pageSize){
            //获取产品列表
            doGet('/v1/product/list',{pType:ptype,pageNo:pageNo,pageSize:pageSize}).then(res=>{
                // console.log(res)
                this.productList = res.data.list
                this.page = res.data.pageInfo
            })
        },
        first(){
            if(this.page.pageNo == 1){
                // alert("当前页已经是第一页")
                layx.msg("当前页已经是第一页",{dialogIcon:'warn',position:'ct'}) //使用layx组件
            }else{
                this.init(ptype,1,this.page.pageSize)
            }
        },
        pre(){
            if(this.page.pageNo == 1){
                layx.msg("当前页已经是第一页",{dialogIcon:'warn',position:'ct'})
            }else{
                this.init(ptype,this.page.pageNo - 1,this.page.pageSize)
            }
        },
        next(){
            if(this.page.pageNo == this.page.totalPage){
                layx.msg("当前页已经是最后一页",{dialogIcon:'warn',position:'ct'})
            }else{
                this.init(ptype,this.page.pageNo + 1,this.page.pageSize)
            }
        },
        last(){
            if(this.page.pageNo == this.page.totalPage){
                layx.msg("当前页已经是最后一页",{dialogIcon:'warn',position:'ct'})
            }else{
                this.init(ptype,this.page.totalPage,this.page.pageSize)
            }
        },
        goLink(url,params){
            this.$router.push({
                path: url,
                query: params
            })
        }
    }

}
</script>

<style>

</style>