$(function () {
    var productId = getQueryString('productId');
    var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
        + productId;

    $.getJSON(
        productUrl,
        function (data) {
            if (data.success) {
                var product = data.product;
                var productImgList=data.productImgs;
                $('#product-img').attr('src', "..\\resources\\image" + product.imgAddr);
                // $('#product-time').text(
                //     new Date(product.lastEditTime)
                //         .Format("yyyy-MM-dd"));
                $('#product-name').text("商品名称："+product.productName);
                $('#product-desc').text("商品描述："+product.productDesc);
                var imgListHtml = '';
                productImgList.map(function (item, index) {
                    imgListHtml += '<div> <img src="..\\resources\\image'
                        + item.imgAddr + '"/></div>';
                });
				$('#imgList').html(imgListHtml);
            }
        });
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
