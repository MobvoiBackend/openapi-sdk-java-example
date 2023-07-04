package com.mobvoi.openapi.example.video;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mobvoi.openapi.client.video.VideoClient;
import com.mobvoi.openapi.constant.VideoComposeStatus;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.video.ComposeResultRequest;
import com.mobvoi.openapi.model.video.ComposeVideoRequest;
import com.mobvoi.openapi.model.video.GetManInfoRequest;
import com.mobvoi.openapi.model.video.Style;
import com.mobvoi.openapi.model.video.TypefaceRequest;
import com.mobvoi.openapi.util.JsonUtil;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class VideoExample {

  public static void main(String[] args) {
    VideoClient client = new VideoClient(Constants.APP_KEY, Constants.APP_SECRET);

    String fontName = null;//getFirstFontName(client);

    Long wetaSpeakerId = getaWetaSpeakerId(client);

    String videoId = composeVideo(client, wetaSpeakerId, true, false, fontName);

    //如果视频合成时，传入了callbackUrl，则此处就不用轮询结果了
    getComposeResult(client, videoId);
  }

  private static String getFirstFontName(VideoClient client) {
    TypefaceRequest request = TypefaceRequest.builder().build();
    String response = client.getTypeface(request);
    log.info("getTypeface response={}", response);
    String fontName = JsonUtil.parseObject(response).getJSONArray("data").getJSONObject(0).getString("name");
    log.info("getTypeface fontName={}", fontName);
    return fontName;
  }

  private static Long getaWetaSpeakerId(VideoClient client) {
    GetManInfoRequest request = GetManInfoRequest.builder()
        .customType("sys")
        .speakerName("元小宇")
        .build();
    String response = client.getManInfo(request);
    log.info("getaWetaSpeakerId response={}", response);
    Long wetaSpeakerId = JsonUtil.parseObject(response).getJSONObject("data").getJSONArray("records").getJSONObject(0).getLong("id");
    log.info("getaWetaSpeakerId wetaSpeakerId={}", wetaSpeakerId);
    return wetaSpeakerId;
  }

  private static void getComposeResult(VideoClient client, String videoId) {
    ComposeResultRequest getRequest = ComposeResultRequest.builder()
        .videoId(videoId)
        .build();

    // 轮询处理结果
    long seconds = 1 * 60;
    long interval = 5;
    while (seconds > 0) {
      String response = client.getComposeResult(getRequest);
      log.info("getComposeResult seconds={} response={}", seconds, response);

      JSONObject jsonObject = JsonUtil.parseObject(response);
      String status = null;
      if (jsonObject.containsKey("data")) {
        status =jsonObject.getJSONObject("data").getString("status");
      }
      log.info("getComposeResult status={}", status);
      // 生成成功或者失败
      if (VideoComposeStatus.SUCCESS.getName().equals(status)
          || VideoComposeStatus.FAIL.getName().equals(status)) {
        break;
      }

      try {
        seconds -= interval;
        TimeUnit.SECONDS.sleep(interval);
      } catch (InterruptedException e) {
        log.info("getComposeResult interrupted", e);
      }
    }
  }

  private static String composeVideo(VideoClient client, Long wetaSpeakerId, boolean withText,
      boolean genSrt, String fontName) {
    ComposeVideoRequest request;
    if (withText) {
      JSONObject ttsParam = new JSONObject();
      ttsParam.put("text", "你好，我是出门问问的数字合成人，很高兴见到你");
//        ttsParam.put("speaker", "caicai_meet_48k");
      ttsParam.put("audio_type", "wav");
      ttsParam.put("gen_srt", genSrt);
      ttsParam.put("ignore_limit", true);

      JSONObject material = new JSONObject();
      material.put("type", "subtitles");
      material.put("fontname", fontName);
      material.put("fontsize", "15");
      JSONArray materials = new JSONArray();
      materials.add(material);

      request = ComposeVideoRequest.builder()
          .wetaSpeakerId(wetaSpeakerId)
          .ttsParam(ttsParam)
          .style(Style.builder()
              .width(1000)
              .height(1900)
              .build())
          .materials(materials)
          .build();
    } else {
      request = ComposeVideoRequest.builder()
          .wetaSpeakerId(wetaSpeakerId)
          .audioUrl("http://mobvoi-backend-public.ufile.ucloud.cn/subtitles/wav/119c95791bceaeba51909b300169705f.wav")
          .style(Style.builder()
              .width(1000)
              .height(1900)
              .build())
          .build();
    }

    String response = client.composeVideo(request);
    log.info("composeVideo response={}", response);
    String videoId = JsonUtil.parseObject(response).getJSONObject("data").getString("videoId");
    log.info("composeVideo videoId={}", videoId);
    return videoId;
  }

}
