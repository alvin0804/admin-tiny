package com.alvin.admin.tiny.modules.ums.controller;


import com.alvin.admin.tiny.common.api.CommonResult;
import com.alvin.admin.tiny.modules.ums.dto.UmsAdminLoginParam;
import com.alvin.admin.tiny.modules.ums.dto.UmsAdminParam;
import com.alvin.admin.tiny.modules.ums.model.UmsAdmin;
import com.alvin.admin.tiny.modules.ums.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author alvin
 * @since 2023-09-21
 */
@RestController
@Api(tags = "UmsAdminController")
@Tag(name = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {

  @Value("${jwt.tokenHeader}")
  private String tokenHeader;
  @Value("${jwt.tokenHead}")
  private String tokenHead;

  @Autowired
  private UmsAdminService adminService;

  @ApiOperation(value = "用户注册")
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
    UmsAdmin umsAdmin = adminService.register(umsAdminParam);
    if (umsAdmin == null) {
      return CommonResult.failed();
    }
    return CommonResult.success(umsAdmin);
  }

  @ApiOperation(value = "登录以后返回token")
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
    String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
    if(token == null) {
      return CommonResult.validateFailed("用户名或密码错误");
    }

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("token", token);
    tokenMap.put("tokenHead", tokenHead);
    return CommonResult.success(tokenMap);
  }
}

