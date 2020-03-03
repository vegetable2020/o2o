/**
 *
 */
$(function () {
    var userId = getQueryString('userId');
    var isEdit = userId ? true : false;
    var registerPersonInfoUrl = '/o2o/shopadmin/registerpersoninfo';
    var personInfoUrl = "/o2o/shopadmin/getpersoninfobyid?userId=" + userId;
    var editPersonInfoUrl = '/o2o/shopadmin/modifypersoninfo';
    var modifySuccessUrl='/o2o/shopadmin/shoplist';
    if (isEdit) {
        getPersonInfo(userId);
    }

    function getPersonInfo(userId) {
        $.getJSON(personInfoUrl, function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '"selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disable', 'disable');
                $('#area').html(tempAreaHtml);
                $("#area option[data-id='" + shop.area.areaId + "']").attr("selected", "selected");
            }
        });
    }

    $('#submit').click(
        function () {
            var personInfo = {};
            if (isEdit) {
                personInfo.personInfoId = userId;
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
                        window.location.href=modifySuccessUrl;
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