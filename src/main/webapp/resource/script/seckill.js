/**
 * Created by think on 2016-05-28-0028.
 */
//存放主要交互逻辑js代码
//javascript 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL : {
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution : function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },
    validatePhone : function (phone) {
        if (phone && phone.length == 11  && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    handleSeckillKill : function (seckillId, node) {
        //获取秒杀地址，控制显示逻辑，执行秒杀
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回调函数中，执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killURL = seckill.URL.execution(seckillId,md5);
                    console.log('killURL:'+killURL);
                    //绑定一次点击事件，不用click
                    $('#killBtn').one('click',function () {
                        //执行秒杀请求
                        //1:先禁用按钮
                        $(this).addClass('disabled');
                        //2:发送秒杀请求
                        $.post(killURL,{},function (result) {
                            console.log('result:'+result);
                            if(result && result['success']){
                              var killResult = result['data'];
                                console.log('killResult:'+killResult);
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }else{
                                node.html('<span class="label label-success">'+result['error']+'</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开始秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countdown_now(seckillId,now,start,end);
                }
            } else {
              console.log('result:'+result);
            }
        });
        
        
        
    },
    countdown_now: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        console.log('BOX = ' + seckillBox);
        console.log('now:'+nowTime+',start:'+startTime+',end:'+endTime);
        //时间判断
        if(nowTime > endTime){
            //秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime){
            //秒杀未开始，开始计时
            var killTime = new Date(startTime + 1000);
            console.log('killTime = ' + killTime);
            seckillBox.countdown(
                {until: killTime, format: 'DHMS', description: '后秒杀开始！',
                    onExpiry:function () {//倒计时完毕后，进入秒杀
                        seckill.handleSeckillKill(seckillId,seckillBox);
                    }
                }
            );
        } else {
            //秒杀开始
            seckill.handleSeckillKill(seckillId,seckillBox);
        }

    },
    //详情页秒杀逻辑
    detail : {
        init : function (params) {
            //手机验证和登录。计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //cookie中没有phone，就绑定phone
                var killPhoneModal = $("#killPhoneModal");
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//进制位置关闭
                    keyboard: false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if(seckill.validatePhone(inputPhone)){
                        //电话写入cookie
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        //刷新页面
                        window.location.reload();
                    }else{
                        //先隐藏，在put内容后，再显示，这样更友好点
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            //已经登录了
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //计时交互 ajax
            $.get(seckill.URL.now(),{}, function (result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    console.log('nowTime = ' + nowTime);
                    //时间判断,计时交互
                    seckill.countdown_now(seckillId,nowTime,startTime,endTime);
                } else {
                    console.log('result: ' + result);
                }
            });
        }
    }
}