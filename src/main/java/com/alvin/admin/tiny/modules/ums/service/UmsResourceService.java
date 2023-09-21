package com.alvin.admin.tiny.modules.ums.service;

import com.alvin.admin.tiny.modules.ums.model.UmsResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 */
public interface UmsResourceService extends IService<UmsResource> {

  /**
   * 分页查询资源
   */
  Page<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);
}
