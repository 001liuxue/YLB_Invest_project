<template>
  <div>
    <Header></Header>
    <div class="login-content">
    <div class="login-flex">
          <div class="login-left"></div>
          <!---->
          <div class="login-box">
              <h3 class="login-title">实名认证</h3>
              <form action="" id="renZ_Submit">
                  <div class="alert-input">
                      <input type="text" class="form-border user-name" name="username" v-model="realName" @blur="checkRealName" placeholder="请输入您的真实姓名">
                      <p class="prompt_name">{{ realNameErr }}</p>
                      <input type="text" class="form-border user-sfz" name="sfz" v-model="idCard" @blur="checkIdCard" placeholder="请输入15位或18位身份证号">
                      <p class="prompt_sfz">{{ idCardErr }}</p>
                      <input type="text" class="form-border user-num" name="mobile" v-bind:value="phone" placeholder="请输入11位手机号">
                      <p class="prompt_num"></p>
                      <div class="form-yzm form-border">
                          <input class="yzm-write" type="text" v-model="code" @blur="checkCode" placeholder="输入短信验证码">
                          <input class="yzm-send" type="text" value="获取验证码" disabled="disabled" id="yzmBtn" readonly="readonly" >
                      </div>
                      <p class="prompt_yan">{{ codeErr }}</p>
                  </div>
                  <div class="alert-input-agree">
                      <input type="checkbox" v-model="agree">我已阅读并同意<a href="javascript:;" target="_blank">《动力金融网注册服务协议》</a>
                  </div>
                  <div class="alert-input-btn">
                      <input type="button" @click="requestRealName" class="login-submit" value="认证">
                  </div>
              </form>
              <div class="login-skip">
                  暂不认证？ <a href="javascript:void(0)" @click="goLink('/page/user/center')">跳过</a>
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
import {doPostJson} from '@/api/httpRequest'
import layx from 'vue-layx'

export default {
  components:{
    Header,
    Footer,
  },
  data() {
    return {
      phone:'',
      realName:'',
      realNameErr:'',
      idCard:'',
      idCardErr:'',
      code:'',
      codeErr:'',
      agree:false
    }
  },
  mounted () {
    let userInfo = window.localStorage.getItem('userInfo')
    if(userInfo != null || userInfo != ''){
      this.phone = JSON.parse(userInfo).phone
    }
  },
  methods: {
    checkRealName(){
      if(this.realName == '' || this.realName == undefined){
        this.realNameErr = '名字不能为空'
      }else if(this.realName.length <= 1){
        this.realNameErr = '名字不正确'
      }else if(!/^[\u4e00-\u9fa5]{0,}$/.test(this.realName)){
        this.realNameErr = '名字格式不正确'
      }else{
        this.realNameErr = ''
      }
    },

    checkIdCard(){
      if(this.idCard == '' || this.idCard == undefined){
        this.idCardErr = '身份证号码不能为空'
      }else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(this.idCard)){
        this.idCardErr = '身份证号码格式不正确'
      }else{
        this.idCardErr = ''
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

    requestRealName(){
      if(this.agree == false){
        layx.msg("请阅读相关协议",{dialogIcon:'warn',position:'ct'})
      }else{
        this.checkRealName()
        this.checkIdCard()
        this.checkCode()

        if(this.realNameErr == '' && this.idCardErr == '' && this.codeErr == ''){
            let params = {
              name:this.realName,
              idCard:this.idCard,
              phone:this.phone,
              code:this.code
            }
            doPostJson('/v1/user/realname',params).then(res=>{
              console.log(res)
              if(res && res.data.code == 200){
                //跳转到用户中心
                this.$router.push({
                  path:'/page/user/center'
                })
              }
            })
        }
      }
        
    },

    goLink(url,params){
      this.$router.push({
        path:url,
        query:params
      })
    }
  }
}
</script>

<style>

</style>