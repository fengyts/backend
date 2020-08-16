$(function () {

    layui.use(['form', 'util'], function () {
        var form = layui.form;
        var util = layui.util;
        //监听提交
        form.on('submit(login-form)', function (data) {
            let userName = data.field['userName'].trim();
            let passwd = data.field['passwd'];
            let smsCode = data.field['smsCode'].trim();
            let kaptcha = data.field['kaptcha'].trim();
            if (!userName || !passwd || userName.length < 1 || passwd.length < 1) {
                layer.msg("用户名/密码不能为空", {icon: 2, time: 1500});
                return false;
            }
            if (!smsCode || !kaptcha || smsCode.length < 1 || kaptcha.length < 1) {
                layer.msg("验证码不能为空", {icon: 2, time: 1500});
                return false;
            }
            var _param = {
                userName: userName,
                passwd: passwd,
                smsCode: smsCode,
                kaptcha: kaptcha,
                rememberMe: data.field['rememberMe'] === '1' ? true : false
            };
            let _paramS = JSON.stringify(_param);
            $.ajax({
                url: '/login',
                type: 'POST',
                dataType: 'json',
                cache: false,
                contentType: 'application/json',
                data: _paramS,
                success: function (res) {
                    if (res && res.code != 200) {
                        layer.msg(res.msg, {time: 2000});
                    } else {
                        window.location.href = res.msg;
                    }
                },
                error: function (res) {
                    layer.msg(res.msg);
                }
            });
            return false;
        });

        // 短信验证码倒计时
        $("#sms-code-btn").on('click', function (e) {
            var _tc = 60; // 倒计时秒数
            timeCountDownLayui(util, this, _tc); // layui自带的倒计时
            // timeCountDownCustom(this, _tc); // 自己实现的倒计时
        });
    });

    function timeCountDownLayui(util, _this, _tc) {
        var now = new Date(), startTime = new Date();
        var getEndTime = (c) => {
            c.setSeconds(c.getSeconds() + _tc);
            return c;
        };
        var endTime = getEndTime(now);
        var _that = _this, s = 0, _c = _tc;
        util.countdown(endTime, startTime, function (date, serverTime, timer) {
            if (_c-- === 0) { // 倒计时完成，清除倒计时
                // timer.clearTimeout;
                _that.disabled = false;
                layui.$('#sms-code-btn').html('重新发送');
                return;
            }
            s = date[3] === 0 ? _tc : date[3];
            if (s > 0) {
                _that.disabled = true;
                layui.$('#sms-code-btn').html(s + '秒后重发');
            } else {
                _that.disabled = false;
                layui.$('#sms-code-btn').html('重新发送');
            }
        });
    }

    function timeCountDownCustom(_this, _tc) {
        var _that = _this;
        _that.disabled = true;
        _that.innerHTML = _tc + "秒后重发";
        var timer = setTimeout(fn, 1000);

        function fn() {
            _tc--;
            if (_tc > 0) {
                _that.innerHTML = _tc + "秒后重发";
                setTimeout(fn, 1000);
            } else {
                _that.innerHTML = "重新发送";
                _that.disabled = false;
            }
        }
    }

    function changeKaptcha() {
        $("#kaptcha_img").attr("src", "/kaptcha/render?" + Math.random());
    }

    $("#kaptcha_img").on('click', function () {
        $("#kaptcha_img").attr("src", "/kaptcha/render?" + Math.random());
    });

    // iframe页面出现登录页的解决方案
    iframeLoginPage();

    // iframe页面出现登录页的解决方案
    function iframeLoginPage() {
        // 这种方案页可以实现
        // if(top.location != location) {
        //     top.location.href = location.href;
        // }
        if (window.top != window.self) {
            top.location.href = location.href;
        }
    }

});