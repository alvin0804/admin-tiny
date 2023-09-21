package com.alvin.admin.tiny.modules.ums.service;

import com.alvin.admin.tiny.modules.ums.dto.UmsAdminParam;
import com.alvin.admin.tiny.modules.ums.model.UmsAdmin;
import com.alvin.admin.tiny.modules.ums.model.UmsResource;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author macro
 * @since 2023-09-21
 */
public interface UmsAdminService extends IService<UmsAdmin> {
  /**
   * 注册功能
   */
  UmsAdmin register(UmsAdminParam umsAdminParam);

  /**
   * 登录功能
   * @param username 用户名
   * @param password 密码
   * @return 生成的JWT的token
   */
  String login(String username,String password);


  /**
   * 获取指定用户的可访问资源
   */
  List<UmsResource> getResourceList(Long adminId);

  /**
   * 根据用户名获取后台管理员
   * @param username
   * @return
   */
  UmsAdmin getAdminByUsername(String username);

  /**
   * 获取用户信息
    * @param username
   * @return
   */
  UserDetails loadUserByUsername(String username);

  /**
   * 获取缓存服务
   */
  UmsAdminCacheService getCacheService();
}
