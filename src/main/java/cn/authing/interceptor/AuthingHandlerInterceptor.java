package cn.authing.interceptor;

import cn.authing.bean.AuthingProperties;
import cn.authing.core.auth.AuthenticationClient;
import cn.authing.core.graphql.GraphQLException;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.core.types.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthingHandlerInterceptor implements HandlerInterceptor {
    private final ManagementClient managementClient;
    private final AuthenticationClient authenticationClient;
    private final AuthingProperties authingProperties;
    
    public AuthingHandlerInterceptor(ManagementClient managementClient, AuthenticationClient authenticationClient, AuthingProperties authingProperties) {
        this.managementClient = managementClient;
        this.authenticationClient = authenticationClient;
        this.authingProperties = authingProperties;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        String token = header.replace("Bearer ", "").replace("bearer ", "");
        authenticationClient.setToken(token);
        try {
            User user = authenticationClient.getCurrentUser().execute();
            String userId = user.getId();
            
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
           
        } catch (IOException | GraphQLException e) {
            return false;
        }
    }
}
