$(function(){
	var cancel = getQueryString('cancel');
	var isEdit = cancel ? true : false;
	var getListUrl="/o2o/shopadmin/getshoplist";
	if(isEdit){
		getListUrl+='?cancel='+cancel;
	}
	var loginUrl='/o2o/shopadmin/login';
	var superAdministrator=false;
	getList();
	function getList(e){
		$.ajax({
			url:getListUrl,
			type:"get",
			dateType:"json",
			success:function(data){
				if(data.success){
					if(data.user.userId==2){
						superAdministrator=true;
					}else{
						superAdministrator=false;
					}
					handleList(data.shopList);
					handleUser(data.user);
				}else{
					window.location.href=loginUrl;
				}
			}
		});
	}
	function handleUser(data){
		$('#user-name').text(data.name);
	}
	
	function handleList(data){
		var html='';
		data.map(function(item,index){
			html+='<div class="row row-shop"><div class="col-40">'
				+item.shopName+'</div>'
				+shopStatus(item.enableStatus,item.shopId)
				+'<div class="col-20">'
				+goShop(item.enableStatus,item.shopId)+'</div></div>';
		});
		$('.shop-wrap').html(html);
	}
	
	function shopStatus(status,id){
		var state='';
		var contraryStatus = 0;
		if(status==0){
			state ='审核中';
			contraryStatus=1;
		}else if(status==-1){
			state= '店铺非法';
			contraryStatus=0
		}else if(status==1){
			state= '审核通过';
			contraryStatus=-1;
		}
		if(superAdministrator){
			return'<div class="col-40"> <a href="#" class="shop-state" data-id="'
			+ id
			+ '" data-status="'
			+ contraryStatus
			+ '">'
			+ state
			+ '</a> </div>';
		}else{
			return '<div class="col-40">' +state +'</div>';
		}
	}
	
	function goShop(status,id){
		if (superAdministrator){
			return '<a href="/o2o/shopadmin/shopmanagement?shopId='+id+'">进入</a>';
		}else{
			if(status==1){
				return '<a href="/o2o/shopadmin/shopmanagement?shopId='+id+'">进入</a>';
			}else{
				return '';
			}
		}
	}

	function changeItemStatus(id, enableStatus) {
		var shop = {};
		shop.shopId = id;
		shop.enableStatus = enableStatus;
		$.confirm('确定么?', function () {
			$.ajax({
				url: '/o2o/shopadmin/modifyshop',
				type: 'POST',
				data: {
					shopStr: JSON.stringify(shop),
					shopStatusChange: true//标识符，跳过验证码验证直接修改商品状态码
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

    $('.shop-wrap').on('click', '.row-shop .shop-state', function (e) {
        changeItemStatus(e.currentTarget.dataset.id,
            e.currentTarget.dataset.status);
    })
})