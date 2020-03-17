/**
 *
 */
$(function () {
    var userId = getQueryString('userId');
    var isEdit = userId ? true : false;
    var registerPersonInfoUrl = '/o2o/shopadmin/registerpersoninfo';
    var personInfoUrl = "/o2o/shopadmin/getpersoninfobyid?userId=" + userId;
    var editPersonInfoUrl = '/o2o/shopadmin/modifypersoninfo';
    if (isEdit) {
        getPersonInfo();
    }

    function getPersonInfo() {
        $.getJSON(personInfoUrl, function (data) {
            if (data.success) {
                var user = data.personInfo;
                $('#name').val(user.name);
                $('#email').val(user.email);
            }
        });
    }

    $('#submit').click(
        function () {
            var personInfo = {};
            if (isEdit) {
                personInfo.userId = userId;
            }
            personInfo.name = $('#name').val();
            personInfo.email = $('#email').val();
            personInfo.password = $('#password').val();
            var formData = new FormData();
            formData.append('personInfoStr', JSON.stringify(personInfo));
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append('verifyCodeActual', verifyCodeActual);
            $.ajax({
                url: (isEdit ? editPersonInfoUrl : registerPersonInfoUrl),
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                            $.toast('提交成功！');
                    } else if (data.exist) {
                        $.toast('电子邮箱已被使用！');
                    } else {
                        $.toast('提交失败！' + data.errMsg);
                    }
                    $('#captcha_img').click();
                }
            });
        });
})