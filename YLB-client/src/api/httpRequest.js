import axios from "axios";
import qs from "qs"
import layx from 'vue-layx'

//设置axios的基地址和超时时间
axios.defaults.baseURL = "http://localhost:8000/api"
axios.defaults.timeout = 50000

function doGet(url,params){
    return axios({
        url:url,
        method:'get',
        params:params
    });
}


//post的传入参数是json数据
function doPostJson(url,params){
    return axios({
        url:url,
        method:'post',
        data:params
    });
}


//传入数据是键值对参数
function doPost(url,params){
    let requestData = qs.stringify(params)
    return axios.post(url,requestData);
}

//创建前端拦截器
axios.interceptors.request.use(function(config){
    //在需要用户登录后的操作之后，将token加入到url
    //判断访问服务器的url地址，需要提供身份信息，加入token

    if(config.url == '/v1/user/realname' || config.url == '/v1/user/center' || config.url == '/v1/recharge/query' || config.url == '/v1/invest/product'){
        let userInfo = window.localStorage.getItem('userInfo');
        let token = window.localStorage.getItem('token');
        if(userInfo && token){
            //将token加入到请求头中
            config.headers['Authorization'] = 'Bearer ' + token
            config.headers['uid'] = JSON.parse(userInfo).uid
        }
        
    }
    return config;

},function(err) {
    console.log("请求错误"+err);
})


//创建响应拦截器，统一对错误进行处理，code > 200 都是错误信息
axios.interceptors.response.use(function(resp){
    if(resp && resp.data.code > 200){
        //token无效
        if(resp.data.code == 300){
            window.location.href = '/page/user/login'
        }else{
            layx.msg(resp.data.msg,{dialogIcon:'warn',position:'ct'})
        }
    }
    return resp;
},function(err) {
    console.log("应答拦截器错误" + err)
})

export {doGet,doPost,doPostJson}