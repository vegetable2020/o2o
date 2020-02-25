$(function () {
    getList();

    function getList() {
        $.ajax({
            url: "/o2o/shopadmin/getproductcategorylist",
            type: "get",
            dateType: "json",
            success: function (data) {
                if (data.success) {
                    handleList(data.data);
                }
            }
        });
    }

    function handleList(data) {
        var html = '';
        data.map(function (item, index) {
            html += '<div class="row row-product-category now"><div class="col-40">'
                + item.productCategoryName + '</div><div class="col-40">'
                + item.priority
                + '</div><div class="col-20">'
                + '<a href="#" class="button delete" data-id="'
                + item.productCategoryId
                + '">删除</a>' + '</div></div>';
        });
        $('.category-wrap').html(html);
    }

    $('#new')
        .click(
            function () {
                var tempHtml = '<div class="row row-product-category temp">'
                    + '<div class="col-40"><input class="category-input category" type="text" placeholder="分类名"></div>'
                    + '<div class="col-40"><input class="category-input priority" type="number" placeholder="优先级"></div>'
                    + '<div class="col-20"><a href="#" class="button delete">删除</a></div>'
                    + '</div>';
                $('.category-wrap').append(tempHtml);
            });
    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function (index, item) {
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj);
            }
        });
        $.ajax({
            url: '/o2o/shopadmin/addproductcategorys',
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            success: function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                    getList();
                } else {
                    $.toast('提交失败！');
                }
            }
        });
    });
    $('.category-wrap').on('click', '.row-product-category.temp .delete',
        function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();
        });
    $('.category-wrap').on('click', '.row-product-category.now .delete',
        function (e) {
            var target = e.currentTarget;
            $.confirm('确定么?', function () {
                $.ajax({
                    url: '/o2o/shopadmin/removeproductcategory',
                    type: 'POST',
                    data: {
                        productCategoryId: target.dataset.id
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            $.toast('删除成功！');
                            getList();
                        } else {
                            $.toast('删除失败！');
                        }
                    }
                });
            });
        });
});
