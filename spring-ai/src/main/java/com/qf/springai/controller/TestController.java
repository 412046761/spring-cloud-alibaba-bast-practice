package com.qf.springai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.springai.config.OpenAiConfig;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestController implements CommandLineRunner {

    @Resource
    private OpenAiConfig aiConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) {
        try {
            String apiKey = aiConfig.getApiKey();
            String baseUrl = aiConfig.getBaseUrl();
            String model = aiConfig.getChat().getOptions().getModel();

            System.out.println("apiKey：" + apiKey);
            System.out.println("配置读取成功");
            System.out.println("地址：" + baseUrl);
            System.out.println("模型：" + model);

            // 构造请求体
            Map<String, Object> body = new HashMap<>();
            body.put("model", model);

            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", "请介绍一下Java");

            body.put("messages", List.of(message));

            String jsonBody = objectMapper.writeValueAsString(body);

            // 原生请求（绕过 Spring AI，解决 HMAC 鉴权）
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("返回状态：" + response.statusCode());
            System.out.println("返回结果：");
            System.out.println(response.body());

        } catch (Exception e) {
            System.err.println("调用失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}