package com.alvin.admin.tiny.modules.ums.service;

import com.alvin.admin.tiny.modules.ums.model.UmsAdmin;
import com.alvin.admin.tiny.modules.ums.model.UmsResource;

import java.util.List;

/**
 * 后台用户缓存管理Service
 */
public interface UmsAdminCacheService {

  /**
   * 获取缓存后台用户信息
   */
  UmsAdmin getAdmin(String username);

  /**
   * 设置缓存后台用户信息
   */
  void setAdmin(UmsAdmin admin);

  /**
   * 获取缓存后台用户资源列表
   */
  List<UmsResource> getResourceList(Long adminId);

  /**
   * 设置后台后台用户资源列表
   */
  void setResourceList(Long adminId, List<UmsResource> resourceList);
}
