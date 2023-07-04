package com.mobvoi.openapi.example.tts;

import com.mobvoi.openapi.client.tts.TtsClient;
import com.mobvoi.openapi.example.constant.Constants;
import com.mobvoi.openapi.model.tts.TtsRequest;
import com.mobvoi.openapi.util.DateUtil;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mobvoi
 * @date 2023/06/08
 **/
@Slf4j
public class TtsExample {

  public static void main(String[] args) {
    TtsClient client = new TtsClient(Constants.APP_KEY, Constants.APP_SECRET);

    String text = "出门问问成立于2012年，是一家以语音交互和软硬结合为核心的人工智能公司，为全球40多个国家和地区的消费者、企业提供人工智能产品和服务。";
    String ssml = "<speak version=\"1.0\" xml:lang=\"zh-CN\" xmlns=\"http://www.w3.org/2001/10/synthesis\">9月10日，庆祝2019年<w phoneme=\"jiao4 shi1 jie2\">教师节</w>暨全国教育系统先进集体和先进个人表彰大会在京举行。<break time=\"500ms\" />习近平总书记在人民大会堂亲切会见受表彰代表，<break time=\"500ms\" />向受到表彰的先进集体和先进个人表示热烈祝贺，<break time=\"500ms\" />向全国广大<p phoneme=\"jiao4\">教</p>师和教育工作者致以节日的问候。</speak>";

    // 文字转语音
    sample(client, text, false);

    // 文字转语音，带srt字幕文件生成
//    sample(client, text, true);

    // 使用ssml格式文字转语音
//    sample(client, ssml, false);
  }

  private static void sample(TtsClient client, String text, boolean genSrt) {
    TtsRequest request = TtsRequest.builder()
        .text(text)
        .speaker("xiaoyi_meet")
        .audio_type("mp3")
        .speed(1.0f)
        // 停顿调节需要对appkey授权后才可以使用，授权前传参无效。
        .symbol_sil("semi_250,exclamation_300,question_250,comma_200,stop_300,pause_150,colon_200")
        // 忽略1000字符长度限制，需要对appkey授权后才可以使用
        .ignore_limit(false)
        // 是否生成srt字幕文件，默认不开启。如果开启生成字幕，需要额外计费。生成好的srt文件地址将通过response header中的srt_address字段返回。
        .gen_srt(genSrt)
        .build();

    try {
      Response response = client.tts(request);
      String contentType = response.header("Content-Type");
      log.info("tts contentType={}", contentType);
      if ("audio/mpeg".equals(contentType)) {
        // 保存音频文件
        File audioFile = new File("tts_sample.mp3");
        FileUtils.writeByteArrayToFile(audioFile, response.body().bytes());
        log.info("tts audioFile={}", audioFile.getPath());

        // 下载srt字幕文件
        String srtAddress = response.header("srt_address");
        if (StringUtils.isNotBlank(srtAddress)) {
          log.info("tts srt_address={}", srtAddress);
          Response srtResponse = client.srt(srtAddress);
          File srtFile = new File("tts_sample.srt");
          FileUtils.writeByteArrayToFile(srtFile, srtResponse.body().bytes());
          log.info("tts srtFile={}", srtFile.getPath());
        }
      } else {
        // ContentType 为 null 或者为 "application/json"
        String result = (response.body() != null ? response.body().string() : null);
        log.error("tts failed, request={} response={}", request, result);
      }
    } catch (Exception e) {
      log.error("tts Exception: ", e);
    }
  }

}
