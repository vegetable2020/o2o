$(function () {
    var url = '/o2o/frontend/listmainpageinfo';

    $.getJSON(url, function (data) {
        if (data.success) {
            var headLineList = data.headLineList;
            var swiperHtml = '';
            headLineList.map(function (item, index) {
                swiperHtml += ''
                    + '<div class="swiper-slide img-wrap">'
                    + '<img class="banner-img" src="..\\resources\\image' + item.lineImg + '" alt="' + item.lineName + '">'
                    + '</div>';
            });
            //将轮播图组赋值给前端HTML控件
            $('.swiper-wrapper').html(swiperHtml);
            //将轮播图组轮换时间为3秒
            $(".swiper-container").swiper({
                //轮换时间
                autoplay: 3000,
                //操作时是否停止
                autoplayDisableOnInteraction: false
            });
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                    + '<div class="col-25 shop-classify" data-category=' + item.shopCategoryId + '>'
                    + '<div class="word">'
                    + '<p class="shop-title">' + item.shopCategoryName + '</p>'
                    + '<p class="shop-desc">' + item.shopCategoryDesc + '</p>'
                    + '</div>'
                    + '<div class="shop-classify-img-warp">'
                    + '<img class="shop-img" src="..\\resources\\image' + item.shopCategoryImg + '">'
                    + '</div>'
                    + '</div>';
            });
            $('.row').html(categoryHtml);
        }
    });

    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });
});
