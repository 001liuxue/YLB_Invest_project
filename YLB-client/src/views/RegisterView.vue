<template>
  <div>
    <Header></Header>
    <div class="login-content">
    <div class="login-flex">
        <div class="login-left">
            <p>万民用户知心托付&nbsp;&nbsp;&nbsp;&nbsp;<span>{{avgRate}}%</span>历史年化收益</p>
            <p>千万级技术研发投入&nbsp;&nbsp;&nbsp;&nbsp;亿级注册资本平台  </p>
        </div>
        <!---->
        <div class="login-box">
            <h3 class="login-title">用户注册</h3>
            <form action="" id="register_Submit">
                <div class="alert-input">
                    <input type="text" class="form-border user-num" name="mobile" @blur="checkPhone()" v-model="phone" placeholder="请输入11位手机号">
                    <div class="err">{{ phoneErr }}</div>
                    <p class="prompt_num"></p>
                    <input type="password" placeholder="请输入6-20位英文和数字混合密码" class="form-border user-pass" @blur="checkPassword()" v-model="password" autocomplete name="password">
                    <div class="err">{{ passwordErr }}</div>
                    <p class="prompt_pass"></p>
                    <div class="form-yzm form-border">
                        <input class="yzm-write" type="text" name="" placeholder="输入短信验证码" v-model="code" @blur="checkCode()">
                        <input class="yzm-send" type="button" @click="getYzm()" :value="yzmText"  id="yzmBtn" readonly="readonly" >
                    </div>
                    <div class="err">{{ codeErr }}</div>
                    <p class="prompt_yan"></p>
                </div>
                <div class="alert-input-agree">
                    <input type="checkbox" v-model="agree">我已阅读并同意<a href="javascript:;" target="_blank">《盈利宝注册服务协议》</a>
                </div>
                <div class="alert-input-btn">
                    <input type="button" class="login-submit" @click="register()" value="注册">
                </div>
            </form>
            <div class="login-skip">
                已有账号？ <a href="javascript:void(0)" @click="goLink('/page/user/login')">登录</a>
            </div>
        </div>

    </div>
</div>

    <Footer></Footer>
  </div>
</template>

<script>
import Header from "@/components/common/Header.vue"
import Footer from "@/components/common/Footer.vue"
import {doGet,doPost} from "@/api/httpRequest"
import layx from 'vue-layx'
import md5 from "js-md5"

export default {
    data() {
        return {
            avgRate: 0.0,
            phone: '',
            phoneErr: '',
            password: '',
            passwordErr: '',
            code: '',
            codeErr: '',
            yzmText: '获取验证码',
            isClick: true,
            agree: false
        }
    },
    components:{
        Header,
        Footer
    },
    mounted () {
        doGet("/v1/plat/info").then(res=>{
            this.avgRate = res.data.data.avgRate
        })
    },
    methods:{
        goLink(url,params){
            this.$router.push({
                path: url,
                query: params
            })
        },
        checkPhone(){
            if(this.phone == '' || this.phone == undefined){
                this.phoneErr = '手机号不能为空'
            }else if(this.phone.length != 11){
                this.phoneErr = '手机号长度必须是11位'
            }else if(!/^1[1-9]\d{9}$/.test(this.phone)){
                this.phoneErr = '手机号格式不正确'
            }else{
                this.phoneErr = ''  //重置phoneErr
                doGet('/v1/user/exist',{phone:this.phone}).then(res=>{
                    if(res.data.code == 200){
                        console.log("该手机号可以注册")
                    }else if(res.data.code == 204){
                        this.phoneErr = '手机号格式不正确'
                    }else{
                        this.phoneErr = '该手机号已经注册过'
                    }
                })
            }
        },

        checkPassword(){
            if(this.password == '' || this.password == undefined){
                this.passwordErr = '密码不能为空'
            }else if(this.password.length < 6 || this.password.length > 20){
                this.passwordErr = '密码长度在6-20位之间'
            }else if(!/^[0-9a-zA-Z]+$/.test(this.password)){
                this.passwordErr = '密码只能是数字和大小写英文字母'
            }else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(this.password)){
                this.passwordErr = '密码应同时包含英文和数字'
            }else{
                this.passwordErr = ''
                console.log("密码可以使用")
            }
        },

        checkCode(){
            if(this.code == '' || this.code == undefined){
                this.codeErr = '验证码不能为空'
            }else if(this.code.length != 6){
                this.codeErr = '请输入正确的验证码'
            }else if(!/^[0-9]+$/.test(this.code)){
                this.codeErr = '请输入正确的验证码'
            }else{
                this.codeErr = ''
            }
        },

        getYzm(){
            //isClick 为true，可以点击，此时yzmText值为"获取验证码", 为false，不可以点击，此时处于倒计时中
            if(this.isClick == true){
                if(this.phone == '' || this.phone == undefined || this.phoneErr != ''){
                    this.phoneErr = '请输入正确手机号'
                    this.checkPhone()
                }else if(this.password == '' || this.password == undefined || this.passwordErr != ''){
                    this.passwordErr = '请输入6-20位只包含数字或大小写英文字母密码'
                    this.checkPassword()
                }else{
                    this.isClick = false;
                    let second = 60; // 从60秒后逐渐递减
                    let interval = setInterval(() => {
                        if(second > 0){
                            this.yzmText = second + '秒后重新发送'
                            second = second - 1
                        }else{
                            this.yzmText = "获取验证码"
                            this.isClick = true
                            clearInterval(interval)
                        }
                        
                    }, 1000);

                    //异步进行验证码发送
                    doGet('/v1/code/register',{phone:this.phone}).then(res=>{
                        if(res && (res.data.code == 200 || res.data.code == 206)){
                            layx.msg("验证码已发送",{dialogIcon:'success',position:'ct'})
                        }
                    })
                }
            }
        },

        register(){
            if(this.phoneErr == '' && this.passwordErr == '' && this.codeErr == ''){
                if(this.agree){
                    //密码加密
                    let pwd = md5(this.password)
                    doPost('/v1/user/register',{
                        phone:this.phone,
                        pwd:pwd,
                        code:this.code
                    }).then(res=>{
                        if(res && res.data.code == 200){
                            layx.msg("注册成功,请重新登录",{dialogIcon:'success',position:'ct'})
                            this.$router.push({
                                path:'/page/user/login',
                            })
                        }
                    })
                }else{
                    layx.msg("请先阅读服务协议",{dialogIcon:'warn',position:'ct'})
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