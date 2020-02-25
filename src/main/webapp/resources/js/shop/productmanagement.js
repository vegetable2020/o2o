$(function () {
    var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=9999&shopId='
    var statusUrl = '/o2o/shopadmin/modifyproduct';
    getList();

    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml = '';
                /**
                 * 遍历每条商品信息，拼接成一行显示，列信息包括：
                 * 商品名称，优先级，上架\下架（含productId），编辑按钮（含productId）
                 * 预览（含productId）
                 */
                productList.map(function (item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        //状态码为0，说明是下架商品，操作变为上架
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-40">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-40">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-20">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="delete" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(tempHtml);
            }
        });
    }

    function changeItemStatus(id, enableStatus) {
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('确定么?', function () {
            $.ajax({
                url: statusUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true//标识符，跳过验证码验证直接修改商品状态码
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        $.toast('操作成功！');
                        getList();//刷新列表
                    } else {
                        $.toast('操作失败！');
                    }
                }
            });
        });
    }

    $('.product-wrap')
        .on('click', 'a', function (e) {
            var target = $(e.currentTarget);
            if (target.hasClass('edit')) {
                //进入店铺信息编辑
                window.location.href = '/o2o/shopadmin/productoperation?productId='
                    + e.currentTarget.dataset.id;
            } else if (target.hasClass('delete')) {
                //商品上/下架功能
                changeItemStatus(e.currentTarget.dataset.id,
                    e.currentTarget.dataset.status);
            } else if (target.hasClass('preview')) {
                //商品预览
                window.location.href = '/o2o/frontend/productdetail?productId='
                    + e.currentTarget.dataset.id;
            }
        });
    // $('#new').click(function() {
    //     window.location.href = '/o2o/shopadmin/productoperation';
    // });
});