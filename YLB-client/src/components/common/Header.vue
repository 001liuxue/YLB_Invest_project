<template>
  <!--头部-->
  <div class="public-head">
        <div class="public-head-nav">
            <div class="public-head-left">
                <h1 class="public-head-logo"><a href="javascript:;">
                    <img src="@/assets/image/logo.png" alt="">
                </a></h1>
                <ul class="public-head-list">
                    <li><a href="javascript:void(0)" @click="goLink('/')">主页</a></li>
                    <li class="public-head-hover">
                        <a href="javascript:void(0);">我要投资</a>
                        <!--二级导航-->
                        <div class="two-title">
                            <a href="javascript:void(0)" class="public-title-more" @click="goLink('/page/product/list',{pType:1})">优选类产品</a>
                            <a href="javascript:void(0)" class="public-title-more" @click="goLink('/page/product/list',{pType:2})">散标类产品</a>
                        </div>
                    </li>
                    <li><a href="user_center.html" target="_blank">借款人信息</a></li>
                    <li><a href="javascript:void(0);">信息披露</a></li>
                    <li><a href="javascript:void(0);">安全计划</a></li>
                </ul>
            </div>
            <div class="public-head-right" v-if="logined">
                <a href="javascript:void(0)" @click="goLink('/page/user/center')">个人中心</a>
                <a href="javascript:void(0)" @click="goBack()">退出</a>
            </div>
            <div class="public-head-right" v-else>
                <a href="javascript:void(0)" @click="goLink('/page/user/login')">登录</a>
                <a href="javascript:void(0)" @click="goLink('/page/user/register')">注册</a>
            </div>
        </div>
    </div>
    <!--end-->
</template>

<script>
export default {
    data() {
        return {
            logined:false
        }
    },
    mounted () {
        if(window.localStorage.getItem('userInfo')){
            this.logined = true
        }
    },
    methods:{
        goLink(url,params){
            this.$router.push({
                path: url,
                query: params
            })
        },
        goBack(){
            localStorage.clear()
            this.goLink("/")  
        }
    },
}
</script>

<style>

</style>