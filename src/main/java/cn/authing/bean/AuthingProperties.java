package cn.authing.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "authing")
public class AuthingProperties {

  private AppProperties app;
  private UserPoolProperties userPool;

  @Data
  public static class AppProperties {
    private String appId;
    private String appHost;
    private String authingAppSecret;
  }

  @Data
  public static class UserPoolProperties {

    private String userPoolId;
    private String secret;
  }
}
