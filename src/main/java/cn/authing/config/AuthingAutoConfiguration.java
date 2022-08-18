package cn.authing.config;


import cn.authing.bean.AuthingProperties;
import cn.authing.core.auth.AuthenticationClient;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.interceptor.AuthingHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({AuthingProperties.class})
public class AuthingAutoConfiguration implements WebMvcConfigurer {
    
    @Autowired
    AuthingProperties authingProperties;
    
    @Bean
    public ManagementClient managementClient() {
        return new ManagementClient(authingProperties.getUserPool().getUserPoolId(), authingProperties.getUserPool().getSecret()
        );
    }
    
    @Bean
    public AuthenticationClient authenticationClient() {
        return new AuthenticationClient(authingProperties.getApp().getAppId(), authingProperties.getApp().getAppHost());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthingHandlerInterceptor(managementClient(), authenticationClient(), authingProperties)).addPathPatterns("/**");
    }
}
