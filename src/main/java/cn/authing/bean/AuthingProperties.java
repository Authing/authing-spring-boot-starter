package cn.authing.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
@Data
@ConfigurationProperties(prefix = "authing")
public class AuthingProperties {

  private AppProperties app;
  private UserPoolProperties userPool;
  private List<String> excludePaths;

  @Data
  public static class AppProperties{
    private String appId;
    private String appHost;
  }

  @Data
  public static class UserPoolProperties{
    private String userPoolId;
    private String secret;
  }
}
