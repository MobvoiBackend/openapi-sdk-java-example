package com.mobvoi.openapi.example.nlp;

import com.mobvoi.openapi.client.nlp.NlpClient;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.nlp.NlpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class NlpExample {

  public static void main(String[] args) {
    NlpClient client = new NlpClient(Constants.APP_KEY, Constants.APP_SECRET);
    NlpRequest request = NlpRequest.builder()
        .query("北京到上海的火车")
        .build();
    String response = client.nlp(request);
    log.info("response={}", response);
  }

}
