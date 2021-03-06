package com.xjh.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	//商品详情
	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	private String showProductDetail() {
		return "frontend/productdetail";
	}
	//店铺详情
	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	private String showShopDetail() {
		return "frontend/shopdetail";
	}
	//店铺查询
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String showShopList() {
		return "frontend/shoplist";
	}
	//主页
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}


}
