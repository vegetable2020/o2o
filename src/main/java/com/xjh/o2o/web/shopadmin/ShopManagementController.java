package com.xjh.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjh.o2o.dto.ImageHolder;
import com.xjh.o2o.dto.PersonInfoExecution;
import com.xjh.o2o.dto.ShopExecution;
import com.xjh.o2o.entity.Area;
import com.xjh.o2o.entity.PersonInfo;
import com.xjh.o2o.entity.Shop;
import com.xjh.o2o.entity.ShopCategory;
import com.xjh.o2o.enums.PersonInfoStateEnum;
import com.xjh.o2o.enums.ShopStateEnum;
import com.xjh.o2o.exceptions.ShopOperationException;
import com.xjh.o2o.service.AreaService;
import com.xjh.o2o.service.PersonInfoService;
import com.xjh.o2o.service.ShopCategoryService;
import com.xjh.o2o.service.ShopService;
import com.xjh.o2o.util.CodeUtil;
import com.xjh.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private PersonInfoService personInfoService;

    //只有已经登录的用户才能访问该网页
    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)//获取店铺信息
    @ResponseBody
    private Map<String, Object> getShopManageMentInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)//获取所有店铺
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        String cancel = request.getParameter("cancel");
        Map<String, Object> modelMap = new HashMap<String, Object>();
        PersonInfo user;
        if (cancel != null) {
            request.getSession().invalidate();
        }
        user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            try {
                Shop shopCondition = new Shop();
                if (user.getUserId() != 2) {
                    shopCondition.setOwner(user);
                }
                ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
                if (se.getState() == ShopStateEnum.INNER_ERROR.getState()) {
                    modelMap.put("success", false);
                } else {
                    modelMap.put("shopList", se.getShopList());
                    modelMap.put("user", user);
                    modelMap.put("success", true);
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)//根据id查询店铺
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("shopCategoryList", shopCategoryList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)//获取创建店铺初始化信息（返回店铺分类和区域分类）
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList ;
        List<Area> areaList = new ArrayList<Area>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)//注册店铺
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commosMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commosMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // 2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                se = shopService.addShop(shop, imageHolder);
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    // 该用户可以操作的店铺列表
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商铺信息");
            return modelMap;
        }

    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)//修改店铺信息
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,
                "shopStatusChange");
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commosMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commosMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        // 2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se;
            try {
                if (shopImg == null) {
                    ImageHolder imageHolder = new ImageHolder(null, null);
                    se = shopService.modifyShop(shop, imageHolder);//传null会空指针异常
                } else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                    se = shopService.modifyShop(shop, imageHolder);
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商铺id");
        }
        return modelMap;
    }

    /////////////////////////////////////////////////用户信息操作///////////////////////////////////////////////////////////
    @RequestMapping(value = "/registerpersoninfo", method = RequestMethod.POST)//注册用户
    @ResponseBody
    private Map<String, Object> registerPersonInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String personInfoStr = HttpServletRequestUtil.getString(request, "personInfoStr");
        ObjectMapper mapper = new ObjectMapper();
        PersonInfo personInfo = null;
        try {
            personInfo = mapper.readValue(personInfoStr, PersonInfo.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if (personInfo != null) {
            PersonInfoExecution se;
            try {
                se = personInfoService.addPersonInfo(personInfo);
                if (se.getState() == PersonInfoStateEnum.EXIST.getState()) {
                    modelMap.put("success", false);
                    modelMap.put("exist", true);
                } else if (se.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("exist", false);
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    @RequestMapping(value = "/getpersoninfobyid", method = RequestMethod.GET)//根据id查询用户
    @ResponseBody
    private Map<String, Object> getPersonInfoById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long userId = HttpServletRequestUtil.getLong(request, "userId");
        if (userId > -1) {
            try {
                PersonInfo personInfo = personInfoService.getPersonInfoByUserId(userId);
                modelMap.put("personInfo", personInfo);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty userId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getpersoninfo", method = RequestMethod.POST)//登录
    @ResponseBody
    private Map<String, Object> login(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String loginStr = HttpServletRequestUtil.getString(request, "loginStr");//获取帐号密码json串
        ObjectMapper mapper = new ObjectMapper();
        PersonInfo personInfoCondition = null;
        try {
            personInfoCondition = mapper.readValue(loginStr, PersonInfo.class);//转换为PersonInfo类型
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if (personInfoCondition != null) {
            PersonInfoExecution se;
            try {
                se = personInfoService.getPersonInfo(personInfoCondition);//根据帐号密码查询用户
                if (se.getState() == PersonInfoStateEnum.INNER_ERROR.getState()) {//用户不存在
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "帐号密码错误");
                } else {
                    request.getSession().setAttribute("user", se.getPersonInfo());
                    modelMap.put("success", true);
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifypersoninfo", method = RequestMethod.POST)//修改用户信息
    @ResponseBody
    private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String personInfoStr = HttpServletRequestUtil.getString(request, "personInfoStr");
        ObjectMapper mapper = new ObjectMapper();
        PersonInfo personInfo = null;
        try {
            personInfo = mapper.readValue(personInfoStr, PersonInfo.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        if (personInfo != null) {
            PersonInfoExecution se;
            try {
                se = personInfoService.modifyPersonInfo(personInfo);
                if (se.getState() == PersonInfoStateEnum.EXIST.getState()) {
                    modelMap.put("success", false);
                    modelMap.put("exist", true);
                } else if (se.getState() == PersonInfoStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("exist", false);
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }
}