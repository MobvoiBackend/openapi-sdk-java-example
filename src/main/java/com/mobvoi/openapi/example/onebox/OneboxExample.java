package com.mobvoi.openapi.example.onebox;

import com.mobvoi.openapi.client.onebox.OneboxClient;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.onebox.SearchRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class OneboxExample {

  public static void main(String[] args) {
    OneboxClient client = new OneboxClient(Constants.APP_KEY, Constants.APP_SECRET);
    SearchRequest request = SearchRequest.builder()
        .query("一千乘以一百等于多少")
        .address("中国,上海市,上海市,杨浦区,武东路,,31.308912471391192,121.49982640557423")
        .output("lite")
        .version("43000")
        .user_id("222100f9-ac2e-4f6b-95b4-8230eee1bdd7")
        .build();
    String response = client.search(request);
    log.info("response={}", response);
  }

}
