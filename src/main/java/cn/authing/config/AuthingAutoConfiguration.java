package cn.authing.config;

import cn.authing.bean.AuthingProperties;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.interceptor.AuthingHandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(AuthingProperties.class)
public class AuthingAutoConfiguration implements WebMvcConfigurer {

  @Autowired
  AuthingProperties authingProperties;
  @Autowired
  ObjectMapper objectMapper;

  @Bean
  public ManagementClient managementClient() {
    return new ManagementClient(authingProperties.getUserPool().getUserPoolId(), authingProperties.getUserPool().getSecret()
    );
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthingHandlerInterceptor(managementClient(), authingProperties, objectMapper))
      .addPathPatterns("/**").excludePathPatterns(authingProperties.getExcludePaths());;
  }
}
