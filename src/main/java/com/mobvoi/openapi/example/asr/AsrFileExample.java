package com.mobvoi.openapi.example.asr;

import com.mobvoi.openapi.client.asr.AsrFileClient;
import com.mobvoi.openapi.constant.AsrFileTaskStatus;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.asr.AsrFileCreateTaskRequest;
import com.mobvoi.openapi.model.asr.AsrFileGetTaskRequest;
import com.mobvoi.openapi.util.JsonUtil;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class AsrFileExample {

  public static void main(String[] args) {
    AsrFileClient client = new AsrFileClient(Constants.APP_KEY, Constants.APP_SECRET);

    Long taskId = createTask(client);

    //如果创建任务时，传入了callbackUrl，则此处就不用轮询结果了
    getTask(client, taskId);
  }

  private static void getTask(AsrFileClient client, Long taskId) {
    AsrFileGetTaskRequest getRequest = AsrFileGetTaskRequest.builder()
        .taskId(taskId)
        .build();

    // 轮询处理结果
    long seconds = 1 * 60;
    long interval = 5;
    while (seconds > 0) {
      String response = client.getTask(getRequest);
      log.info("asr file getTask response={}", response);

      Integer status = JsonUtil.parseObject(response).getJSONObject("data").getIntValue("status");
      // 生成成功或者失败
      if (AsrFileTaskStatus.SUCCESS.getCode().equals(status)
          || AsrFileTaskStatus.FAIL.getCode().equals(status)) {
        break;
      }

      try {
        seconds -= interval;
        TimeUnit.SECONDS.sleep(interval);
      } catch (InterruptedException e) {
        log.info("asr file getTask interrupted", e);
      }
    }
  }

  private static Long createTask(AsrFileClient client) {
    AsrFileCreateTaskRequest request = AsrFileCreateTaskRequest.builder()
        .audio_url("http://mobvoi-backend-public.ufile.ucloud.cn/subtitles/wav/119c95791bceaeba51909b300169705f.wav")
        .language("zh_cn")
        .enable_punctuation(true)
        .build();
    String response = client.createTask(request);
    log.info("asr file createTask response={}", response);
    Long taskId = JsonUtil.parseObject(response).getJSONObject("data").getLong("task_id");
    log.info("asr file createTask taskId={}", taskId);
    return taskId;
  }

}
