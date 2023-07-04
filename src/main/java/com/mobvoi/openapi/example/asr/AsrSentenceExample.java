package com.mobvoi.openapi.example.asr;

import com.mobvoi.openapi.client.asr.AsrSentenceClient;
import com.mobvoi.openapi.constant.AsrSentenceResultType;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.asr.AsrSentenceStartRequest;
import com.mobvoi.openapi.util.JsonUtil;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class AsrSentenceExample {

  public static void main(String[] args) {
    AsrSentenceClient client = new AsrSentenceClient(Constants.APP_KEY, Constants.APP_SECRET);

    AsrSentenceStartRequest request = AsrSentenceStartRequest.builder()
        .contentType("audio/x-wav;codec=pcm;bit=16;rate=16000")
        .partial_result("enable")
        .silence_detection("enable")
        .build();
    client.connect(request, new WebSocketListener() {
      @Override
      public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("onOpen");
      }

      @Override
      public void onMessage(WebSocket webSocket, String text) {
        log.info("asr sentence onMessage text={}", text);

        String type = JsonUtil.parseObject(text).getString("type");
        log.info("asr sentence onMessage type={}", type);
        // 识别结束后，记得关闭WebSocket
        if (AsrSentenceResultType.END.getName().equals(type)) {
          log.info("asr sentence onMessage closeWebsocket");
          client.closeWebsocket();
        }
      }

      @Override
      public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
      }

      @Override
      public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        log.info("onClosing");
      }

      @Override
      public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("onClosed");
        client.closeWebsocket();
      }

      @Override
      public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        log.error("onFailure", t);
        client.closeWebsocket();
      }
    });

    File file = new File("tts_sample.wav");
    client.send(file);

    log.info("asr sentence main exit");
  }

}
