package cn.authing.interceptor;

import cn.authing.bean.AuthingProperties;
import cn.authing.bean.JwtPayload;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.core.types.*;
import cn.authing.exception.AuthingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthingHandlerInterceptor implements HandlerInterceptor {
  private final ManagementClient managementClient;
  private final AuthingProperties authingProperties;
  private final ObjectMapper objectMapper;

  public AuthingHandlerInterceptor(ManagementClient managementClient, AuthingProperties authingProperties, ObjectMapper objectMapper) {
    this.managementClient = managementClient;
    this.authingProperties = authingProperties;
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String header = request.getHeader("Authorization");
    String token = header.replace("Bearer ", "").replace("bearer ", "");
    if (ObjectUtils.isEmpty(token)) {
      throw new AuthingException("未登录", HttpStatus.FORBIDDEN);
    }
    try {
      String s = token.split("\\.")[1];
      byte[] decode = Base64.getDecoder().decode(s);
      JwtPayload jwtPayload = objectMapper.readValue(decode, JwtPayload.class);
      String userId = jwtPayload.getSub();

      ListAuthorizedResourcesOptions resourcesOptions = new ListAuthorizedResourcesOptions(ResourceType.API);
      PaginatedAuthorizedResources resources = managementClient.acl().listAuthorizedResources(PolicyAssignmentTargetType.USER, userId, authingProperties.getApp().getAppId(), resourcesOptions).execute();
      List<String> identifiers = resources.getList().stream()
        .map(AuthorizedResource::getApiIdentifier)
        .collect(Collectors.toList());

      String uri = request.getRequestURI();
      for (String r : identifiers) {
        if (uri.contains(r)) {
          log.info("鉴权通过");
          return true;
        }
      }
      log.info("无权限");
      return false;

    } catch (Exception e) {
      throw new AuthingException("token 错误", HttpStatus.FORBIDDEN);
    }
  }
}
