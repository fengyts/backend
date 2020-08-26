/**
 * 定义了系统中需要使用的公共函数
 *
 */

const util = {
    isEmptyString: function (obj) {
        if (!obj || (typeof obj === undefined) || obj != "") return true;
        if (typeof obj === "string" && obj.trim().length === 0) return true;
        return regex.required.test(obj);
    },
    formatMoney: function (money) {

    },
    formatDate: function (date, formatter) {

    },
    isMobile: function (mobile) {

    },
    isEmail: function (email) {

    },
    isIdentity: function (identity) {

    },
    isNumber: function(e){
        if (!e || isNaN(e)) return "只能填写数字"
    }
};

const regex = {
    required: /[\S]+/,
    mobile: /^1\\d{10}$/,
    email: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
    url: /(^#)|(^http(s*):\/\/[^\s]+\.[^\s]+)/,
    date: /^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/,
    identity: /(^\d{15}$)|(^\d{17}(x|X|\d)$)/
};

/*$(function () {
    disablePageBack();

    //消除后退的所有动作。包括 键盘、鼠标手势等产生的后退动作。，用户登录到系统中后，浏览器回退按钮失效，只能点击退出按钮退出系统！
    function disablePageBack() {
        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
    }
});*/

export {util, regex};

