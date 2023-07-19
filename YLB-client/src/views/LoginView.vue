<template>
  <div>
    <Header></Header>
    <div class="login-content">
      <div class="login-flex">
          <div class="login-left">
              <h3>加入盈利宝</h3>
              <p>坐享<span>{{platInfo.avgRate}}%</span>历史年化收益</p>
              <p>平台用户<span>{{ platInfo.userCount }}</span>位  </p>
              <p>累计成交金额<span>{{platInfo.sumBidMoney}}</span>元</p>
          </div>
          <!---->
          <div class="login-box">
              <h3 class="login-title">欢迎登录</h3>
              <form action="" id="login_Submit">
                  <div class="alert-input">
                      <!--<input class="form-border user-name" name="username" type="text" placeholder="您的姓名">
                      <p class="prompt_name"></p>-->
                      <input type="text" class="form-border user-num" v-model="phone" @blur="checkPhone()" name="mobile" placeholder="请输入11位手机号">
                      <div class="err">{{ phoneErr }}</div>
                      <p class="prompt_num"></p>
                      <input type="password" placeholder="请输入登录密码" class="form-border user-pass" v-model="password" @blur="checkPassword()" autocomplete name="password">
                      <div class="err">{{ passwordErr }}</div>
                      <p class="prompt_pass"></p>
                      <div class="form-yzm form-border">
                          <input class="yzm-write" type="text" v-model="code" placeholder="输入短信验证码">
                          <input class="yzm-send" type="button" @click="getYzm()" :value="yzmText" id="yzmBtn">
                      </div>
                      <div class="err">{{ codeErr }}</div>
                      <p class="prompt_yan"></p>
                  </div>
                  <div class="alert-input-btn">
                      <input type="button" @click="login()" class="login-submit" value="登录">
                  </div>
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
import {doGet, doPost} from '@/api/httpRequest'
import layx from 'vue-layx'
import md5 from "js-md5"
import qs from 'qs'

export default {
  components:{
        Header,
        Footer
    },
    data() {
      return {
        platInfo:{
                "avgRate": 0.00,
                "sumBidMoney": 0.00,
                "userCount": 0
            },
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
    mounted(){
      doGet("/v1/plat/info").then(res=>{
            // console.log('从服务器获得的数据'+ res.data.data)
            this.platInfo = res.data.data
        })
    },
    methods:{

        checkPhone(){
            if(this.phone == '' || this.phone == undefined){
                this.phoneErr = '手机号不能为空'
            }else if(this.phone.length != 11){
                this.phoneErr = '手机号长度必须是11位'
            }else if(!/^1[1-9]\d{9}$/.test(this.phone)){
                this.phoneErr = '手机号格式不正确'
            }else{
                this.phoneErr = ''  //重置phoneErr
            }
        },

        checkPassword(){
            if(this.password == '' || this.password == undefined){
                this.passwordErr = '密码不能为空'
            }else{
                this.passwordErr = ''
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
                    this.passwordErr = '请输入密码'
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
                    doGet('/v1/code/login',{phone:this.phone}).then(res=>{
                        if(res && (res.data.code == 200 || res.data.code == 206)){
                            layx.msg("验证码已发送",{dialogIcon:'success',position:'ct'})
                        }
                    })
                }
            }
        },

        login(){
            this.checkPhone()
            this.checkPassword()
            this.checkCode()

            if(this.phoneErr == '' && this.passwordErr == '' && this.codeErr == ''){
                let params = {
                    phone:this.phone,
                    pwd:md5(this.password),
                    code:this.code
                }
                doPost("/v1/user/login",params).then(res=>{
                    // console.log(res.data)
                    if(res.data.code == 200){
                        //存放token
                        window.localStorage.setItem("token",res.data.jwtToken)
                        //存放userInfo
                        let userInfo = JSON.stringify(res.data.data)
                        window.localStorage.setItem("userInfo",userInfo)

                        if(res.data.data.name == null || res.data.data.name == ''){
                            this.$router.push({
                                path:'/page/user/realname'
                            })
                        }else{
                            this.$router.push({
                                path:'/page/user/center'
                            })
                        }
                    }
                })
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