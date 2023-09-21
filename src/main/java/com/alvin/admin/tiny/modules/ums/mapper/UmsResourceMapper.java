package com.alvin.admin.tiny.modules.ums.mapper;

import com.alvin.admin.tiny.modules.ums.model.UmsResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2023-09-21
 */
public interface UmsResourceMapper extends BaseMapper<UmsResource> {
  /**
   * 获取用户所有可访问资源
   * @param adminId
   * @return
   */
  List<UmsResource> getResourceList(@Param("adminId") Long adminId);
}
