package com.mobvoi.openapi.example.asr;

import com.mobvoi.openapi.client.asr.AsrClient;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.asr.AsrRequest;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class AsrExample {

  public static void main(String[] args) {
    AsrClient client = new AsrClient(Constants.APP_KEY, Constants.APP_SECRET);
    AsrRequest request = AsrRequest.builder()
        .audioFile(new File("tts_sample.mp3"))
        .type("audio/x-mp3;rate=16000")
        .device_id("222100f9-ac2e-4f6b-95b4-8230eee1bdd7")
        .build();
    String response = client.asr(request);
    log.info("response={}", response);
  }

}
