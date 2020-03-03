/**
 *
 */
$(function () {
    var loginUrl = '/o2o/shopadmin/getpersoninfo';
    var loginSuccessUrl='/o2o/shopadmin/shoplist';

    $('#submit').click(
        function () {
            var personInfo = {};
            personInfo.email = $('#email').val();
            personInfo.password = $('#password').val();
            var formData = new FormData();
            formData.append('loginStr', JSON.stringify(personInfo));
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append('verifyCodeActual', verifyCodeActual);
            $.ajax({
                url: loginUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        window.location.href=loginSuccessUrl;
                    }  else {
                        $.toast(data.errMsg);
                    }
                    $('#captcha_img').click();
                }
            });
        });
})