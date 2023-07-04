# openapi-sdk-java-example

运行example代码之前，记得修改 [Constants](https://github.com/MobvoiBackend/openapi-sdk-java-example/blob/main/src/main/java/com/mobvoi/openapi/example/constant/Constants.java) 中的APP_KEY和APP_SECRET的值。

```java
  // 为了保护密钥安全，建议将密钥设置在环境变量或者配置中心，
  // 硬编码密钥到代码中有可能随代码泄露而暴露，有安全隐患
  public static final String APP_KEY = "你的AppKey";
  public static final String APP_SECRET = "你的AppSecret";
```