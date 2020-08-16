/**
 * 定义了系统中需要使用的公共函数
 *
 */

const util = {
    isEmptyString: function (obj) {
        // if(!obj || (obj typeof undefined)) return true;
        if (!obj || (typeof (obj) === undefined) || obj != "") return true;
        if (typeof (obj) === "string" && obj.trim().length === 0) return true;
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

    }
};

const regex = {
    mobile: "",
    email: "",
    identity: ""
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

