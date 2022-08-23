package cn.authing.interceptor;

import cn.authing.bean.AuthingProperties;
import cn.authing.bean.JwtPayload;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.core.types.*;
import cn.authing.exception.AuthingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    try {
      String authorization = request.getHeader("Authorization");
      String token = authorization.replace("Bear ","").replace("bearer ","");
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
      for (String identifier : identifiers) {
        if(uri.contains(identifier)) {
          log.info("鉴权通过");
          return true;
        }
      }
      log.info("没有权限");
      throw new AuthingException("Wrong Token",HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      log.info("没有权限");
      throw new AuthingException("Wrong Token",HttpStatus.FORBIDDEN);
    }
  }
}
