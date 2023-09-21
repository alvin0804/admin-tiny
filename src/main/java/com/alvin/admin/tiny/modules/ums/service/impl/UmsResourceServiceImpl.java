package com.alvin.admin.tiny.modules.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alvin.admin.tiny.modules.ums.model.UmsResource;
import com.alvin.admin.tiny.modules.ums.mapper.UmsResourceMapper;
import com.alvin.admin.tiny.modules.ums.service.UmsResourceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2023-09-21
 */
@Service
public class UmsResourceServiceImpl extends ServiceImpl<UmsResourceMapper, UmsResource> implements UmsResourceService {

  @Override
  public Page<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {

    Page<UmsResource> page = new Page<UmsResource>(pageNum, pageSize);
    QueryWrapper<UmsResource> wrapper = new QueryWrapper<>();
    LambdaQueryWrapper<UmsResource> lambda = wrapper.lambda();

    if(categoryId != null) {
      lambda.eq(UmsResource::getCategoryId,categoryId);
    }
    if(StrUtil.isNotEmpty(nameKeyword)){
      lambda.like(UmsResource::getName,nameKeyword);
    }
    if(StrUtil.isNotEmpty(urlKeyword)){
      lambda.like(UmsResource::getUrl,urlKeyword);
    }

    return page(page,wrapper);
  }
}
