package com.mobvoi.openapi.example.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class Constants {

  // 为了保护密钥安全，建议将密钥设置在环境变量或者配置中心，
  // 硬编码密钥到代码中有可能随代码泄露而暴露，有安全隐患
  public static final String APP_KEY = "你的AppKey";
  public static final String APP_SECRET = "你的AppSecret";

}
