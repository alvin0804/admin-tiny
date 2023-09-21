package com.alvin.admin.tiny.modules.ums.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alvin.admin.tiny.common.exception.Asserts;
import com.alvin.admin.tiny.domain.AdminUserDetails;
import com.alvin.admin.tiny.modules.ums.dto.UmsAdminParam;
import com.alvin.admin.tiny.modules.ums.mapper.UmsAdminLoginLogMapper;
import com.alvin.admin.tiny.modules.ums.mapper.UmsResourceMapper;
import com.alvin.admin.tiny.modules.ums.model.UmsAdmin;
import com.alvin.admin.tiny.modules.ums.mapper.UmsAdminMapper;
import com.alvin.admin.tiny.modules.ums.model.UmsAdminLoginLog;
import com.alvin.admin.tiny.modules.ums.model.UmsResource;
import com.alvin.admin.tiny.modules.ums.service.UmsAdminCacheService;
import com.alvin.admin.tiny.modules.ums.service.UmsAdminService;
import com.alvin.admin.tiny.security.util.JwtTokenUtil;
import com.alvin.admin.tiny.security.util.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2023-09-21
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements UmsAdminService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UmsAdminLoginLogMapper loginLogMapper;
  @Autowired
  private UmsResourceMapper resourceMapper;


  @Override
  public UmsAdmin register(UmsAdminParam umsAdminParam) {
    UmsAdmin umsAdmin = new UmsAdmin();
    BeanUtils.copyProperties(umsAdminParam, umsAdmin);
    umsAdmin.setCreateTime(new Date());
    umsAdmin.setStatus(1);
    //查询是否有相同用户名的用户
    QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(UmsAdmin::getUsername,umsAdmin.getUsername());
    List<UmsAdmin> umsAdminList = list(wrapper);
    if (umsAdminList.size() > 0) {
      return null;
    }
    //将密码进行加密操作
    String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
    umsAdmin.setPassword(encodePassword);
    baseMapper.insert(umsAdmin);
    return umsAdmin;
  }

  @Override
  public String login(String username, String password) {
    String token = null;
    try {
      UserDetails userDetails = loadUserByUsername(username);
      if(!passwordEncoder.matches(password, userDetails.getPassword())) {
        Asserts.fail("密码不正确");
      }
      if(!userDetails.isEnabled()) {
        Asserts.fail("账户被禁用");
      }
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
      token = jwtTokenUtil.generateToken(userDetails);

      // 记录登录日志
      insertLoginLog(username);
    } catch (AuthenticationException e) {
      LOGGER.warn("登录异常:{}", e.getMessage());
    }
    return token;
  }

  private void insertLoginLog(String username) {
    UmsAdmin admin = getAdminByUsername(username);
    if(admin == null) return;

    UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
    loginLog.setAdminId(admin.getId());
    loginLog.setCreateTime(new Date());

    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    loginLog.setIp(request.getRemoteAddr());
    loginLogMapper.insert(loginLog);
  }

  @Override
  public List<UmsResource> getResourceList(Long adminId) {
    List<UmsResource> resourceList = getCacheService().getResourceList(adminId);
    if(CollUtil.isNotEmpty(resourceList)) {
      return resourceList;
    }
    resourceList = resourceMapper.getResourceList(adminId);
    if(CollUtil.isNotEmpty(resourceList)) {
      getCacheService().setResourceList(adminId, resourceList);
    }
    return resourceList;
  }

  @Override
  public UmsAdmin getAdminByUsername(String username) {
    UmsAdmin admin = getCacheService().getAdmin(username);
    if(admin != null) return admin;

    QueryWrapper<UmsAdmin> wrapper = new QueryWrapper<UmsAdmin>();
    wrapper.lambda().eq(UmsAdmin::getUsername, username);
    List<UmsAdmin> adminList = list(wrapper);
    if(adminList != null && adminList.size() > 0) {
      admin = adminList.get(0);
      getCacheService().setAdmin(admin);
      return admin;
    }
    return null;

  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    UmsAdmin admin = getAdminByUsername(username);

    if(admin != null) {
      List<UmsResource> resourceList = getResourceList(admin.getId());
      return new AdminUserDetails(admin,resourceList);
    }

    throw new UsernameNotFoundException("用户名或密码错误");
  }


  @Override
  public UmsAdminCacheService getCacheService() {
    return SpringUtil.getBean(UmsAdminCacheService.class);
  }
}
