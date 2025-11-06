package com.app.toeic.chatai.controller;

import com.app.toeic.external.response.ResponseVO;
import com.app.toeic.external.service.SystemConfigService;
import com.app.toeic.util.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient;
import org.springframework.ai.vertexai.palm2.VertexAiPaLm2ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Blob;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.protobuf.ByteString;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;


@Log
@RestController
@RequestMapping("vertex")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VertexAIController {
    SystemConfigService systemConfigService;
    VertexAiPaLm2ChatClient chatClient;
    VertexAiGeminiChatClient vertexAiGeminiChatClient;

    @PostMapping("ask/v1")
    public Object askVertexV1(@RequestBody PayloadVertex payloadVertex) {
        //        var jsonContent = systemConfigService.getConfigValue(Constant.VERTEX);
        //        var obj = ObjectUtils.CONST(null);
        //        if (StringUtils.isBlank(jsonContent)) {
        //            log.warning("VertexAIController >> askVertexV1 >> jsonContent is blank");
        //            return ResponseVO
        //                    .builder()
        //                    .success(false)
        //                    .message("VERTEX_CONFIG_NOT_FOUND")
        //                    .build();
        //        }
        //        try (var vertexAi = new VertexAI.Builder()
        //                .setProjectId("beaming-inn-423802-q6")
        //                .setLocation("us-central1")
        //                .setCredentials(ServiceAccountCredentials
        //                                        .fromStream(new FileInputStream("src/main/resources/beaming-inn-423802-q6-329f5697e378.json"))
        //                                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform")))
        //                .build()) {
        //            var generationConfig =
        //                    GenerationConfig.newBuilder()
        //                                    .setMaxOutputTokens(8192)
        //                                    .setTemperature(1F)
        //                                    .setTopP(0.95F)
        //                                    .build();
        //            var safetySettings = List.of(
        //                    SafetySetting.newBuilder()
        //                                 .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
        //                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
        //                                 .build(),
        //                    SafetySetting.newBuilder()
        //                                 .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
        //                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
        //                                 .build(),
        //                    SafetySetting.newBuilder()
        //                                 .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
        //                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
        //                                 .build(),
        //                    SafetySetting.newBuilder()
        //                                 .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
        //                                 .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
        //                                 .build()
        //            );
        //            var model = new GenerativeModel.Builder()
        //                    .setModelName("gemini-1.0-pro")
        //                    .setVertexAi(vertexAi)
        //                    .setGenerationConfig(generationConfig)
        //                    .build();
        //
        //
        ////            var content = ContentMaker.fromMultiModalData(payloadVertex.prompt());
        //
        //            obj = model.generateContent("Hello, how are you?");
        //        } catch (Exception e) {
        //            log.warning("Error: " + e.getMessage());
        //        }

        return ResponseVO
                .builder()
                .success(true)
                .build();
    }

    @GetMapping("/ai/generate")
    public Object generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        var listMessage = List.of(
                new UserMessage("Tôi đang tìm hiểu về TOEIC."),
                new AssistantMessage("## Tìm hiểu về TOEIC\\n\\n### TOEIC là gì?\\n\\nTOEIC (Test of English for International Communication) là bài kiểm tra tiếng Anh được sử dụng phổ biến nhất trên thế giới để đánh giá khả năng giao tiếp bằng tiếng Anh trong môi trường quốc tế. Bài thi này được thiết kế để đánh giá khả năng đọc hiểu và nghe hiểu tiếng Anh của các đối tượng sau:\\n\\n* Sinh viên, người đi làm, và các chuyên gia muốn chứng minh khả năng tiếng Anh của họ cho mục đích học tập, làm việc, hoặc di cư.\\n* Các công ty, tổ chức giáo dục, và các cơ quan chính phủ muốn đánh giá trình độ tiếng Anh của nhân viên, sinh viên, hoặc ứng viên.\\n\\n### Cấu trúc bài thi TOEIC\\n\\nBài thi TOEIC bao gồm hai phần: Nghe (Listening) và Đọc (Reading). Mỗi phần có 100 câu hỏi, tổng cộng là 200 câu hỏi. Thời gian làm bài thi là 2 tiếng.\\n\\n* **Phần nghe (Listening):** 45 phút, gồm 4 phần:\\n\\n    * 10 bức ảnh mô tả một tình huống, bạn cần chọn câu trả lời phù hợp nhất với hình ảnh.\\n    * 30 câu hỏi trắc nghiệm về các chủ đề ngắn, thông thường là các cuộc hội thoại.\\n    * 30 câu hỏi trắc nghiệm về các chủ đề dài, thông thường là các bài thuyết trình hoặc bài báo.\\n* **Phần đọc (Reading):** 75 phút, gồm 3 phần:\\n\\n    * 40 câu hỏi về việc hoàn thành câu, yêu cầu bạn điền vào chỗ trống các từ phù hợp với ngữ cảnh.\\n    * 40 câu hỏi về việc xác định lỗi, yêu cầu bạn tìm lỗi sai trong câu.\\n    * 80 câu hỏi về việc đọc hiểu, yêu cầu bạn đọc một đoạn văn ngắn và trả lời các câu hỏi liên quan đến nội dung của đoạn văn.\\n\\n### Cách học TOEIC hiệu quả\\n\\nCó nhiều cách để học TOEIC hiệu quả, tùy thuộc vào trình độ và khả năng học tập của mỗi người. Dưới đây là một số lời khuyên:\\n\\n* **Xác định mục tiêu:** Trước tiên, bạn cần xác định mục tiêu của mình là đạt được bao nhiêu điểm trong bài thi TOEIC. Điều này sẽ giúp bạn tập trung vào các kỹ năng cần thiết và chọn phương pháp học phù hợp.\\n* **Học từ vựng:** Từ vựng là yếu tố quan trọng nhất trong bài thi TOEIC. Bạn nên học từ vựng theo chủ đề, dựa vào các tài liệu học TOEIC uy tín như Longman Wordlist for TOEIC hoặc ETS TOEIC Official Vocabulary List.\\n* **Luyện nghe:** Năng lực nghe hiểu là một trong những kỹ năng khó nhất trong bài thi TOEIC. Bạn nên luyện nghe tiếng Anh thường xuyên qua các podcast, audiobook, chương trình truyền hình, hoặc các video trên YouTube.\\n* **Luyện đọc:** Năng lực đọc hiểu cũng rất quan trọng trong bài thi TOEIC. Bạn nên luyện đọc các bài báo, tạp chí, hoặc sách tiếng Anh để tăng khả năng đọc hiểu. \\n* **Làm bài thi thử:** Làm bài thi thử TOEIC là cách tốt nhất để kiểm tra trình độ của bạn và làm quen với cấu trúc bài thi. Bạn có thể tìm các bài thi thử TOEIC miễn phí trên mạng hoặc mua sách bài thi thử TOEIC.\\n* **Tham gia khóa học TOEIC:** Tham gia khóa học TOEIC là một cách hiệu quả để học TOEIC trong thời gian ngắn. Các khóa học TOEIC thường do các trung tâm tiếng Anh uy tín tổ chức, với giáo viên giàu kinh nghiệm và tài liệu học chất lượng.\\n\\n### Tài liệu học TOEIC\\n\\nCó nhiều tài liệu học TOEIC uy tín trên thị trường, chẳng hạn như:\\n\\n* **Sách:** Longman Wordlist for TOEIC, ETS TOEIC Official Vocabulary List, Grammar in Use for TOEIC, Barron's TOEIC.\\n* **Website:** www.toeic.com.vn, www.ets.org/toeic.\\n* **Ứng dụng di động:** Vocab Victor, TOEIC Grammar, TOEIC Listening Practice.\\n* **Kênh YouTube:** English with Jennifer, BBC Learning English, VOA Learning English.\\n\\n### Kết luận\\n\\n TOEIC là bài thi tiếng Anh quan trọng được sử dụng phổ biến trên thế giới. Để đạt được điểm cao trong bài thi TOEIC, bạn cần học tập chăm chỉ, sử dụng phương pháp học phù hợp, và luyện tập thường xuyên. \\n\\nChúc bạn học TOEIC hiệu quả!"),
                (Message) new UserMessage("Nó là viết tắt của từ gì"));
        var prompt = new Prompt(listMessage);
        return vertexAiGeminiChatClient.call(prompt);
    }

    public record PayloadVertex(String prompt) {
    }
}
