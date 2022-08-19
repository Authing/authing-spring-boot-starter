package cn.authing.bean;

import lombok.Data;

@Data
public class JwtPayload {

  private String sub;
  private String aud;
  private String scope;
  private float iat;
  private float exp;
  private String jti;
  private String iss;
}
