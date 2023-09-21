package com.alvin.admin.tiny.config;

import com.alvin.admin.tiny.modules.ums.model.UmsResource;
import com.alvin.admin.tiny.modules.ums.service.UmsAdminService;
import com.alvin.admin.tiny.modules.ums.service.UmsResourceService;
import com.alvin.admin.tiny.security.component.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 自定义配置，用于配置如何获取用户信息及动态权限
 */
@Configuration
public class AdminSecurityConfig {

  @Autowired
  private UmsAdminService adminService;

  @Autowired
  private UmsResourceService resourceService;

  @Bean
  public UserDetailsService userDetailsService() {
    //获取登录用户信息
    return username -> adminService.loadUserByUsername(username);
  }

  @Bean
  public DynamicSecurityService dynamicSecurityService() {
    return new DynamicSecurityService() {

      @Override
      public Map<String, ConfigAttribute> loadDataSource() {
        Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
        List<UmsResource> resourceList = resourceService.list();
        for (UmsResource resource : resourceList) {
          map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
        }
        return map;
      }
    };
  }
}
