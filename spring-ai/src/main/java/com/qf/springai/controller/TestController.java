package com.qf.springai.controller;

import com.qf.springai.dto.ChatRequest;
import com.qf.springai.dto.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

/**
 * Spring AI 聊天控制器
 * 提供 REST API 接口进行 AI 对话
 *
 * @author ly
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
public class TestController {

    @Resource
    private ChatModel chatModel;

    private static final String DEFAULT_SYSTEM_PROMPT =
            "我想让你充当一个拥有十年开发经验的架构师，对多种编程语言和技术栈有深入的了解，" +
            "精通编程原理、算法、数据结构以及调试技巧，能够有效地沟通和解释复杂概念，" +
            "提供清晰、准确、有用的回答，帮助提问者解决问题，并提升个人在专业领域的声誉。" +
            "回答需要保持专业、尊重和客观，避免使用过于复杂或初学者难以理解的术语。";

    /**
     * 发送聊天请求 (GET 方式，用于简单测试)
     *
     * @param message 用户消息
     * @return AI 响应结果
     */
    @GetMapping("/chat")
    public ChatResponse chatGet(@RequestParam String message) {
        return doChat(message);
    }

    /**
     * 发送聊天请求 (POST 方式，标准 JSON)
     *
     * @param request 聊天请求对象
     * @return AI 响应结果
     */
    @PostMapping("/chat")
    public ChatResponse chatPost(@RequestBody ChatRequest request) {
        return doChat(request.getMessage());
    }

    /**
     * 核心聊天处理逻辑
     *
     * @param message 用户消息
     * @return AI 响应结果
     */
    private ChatResponse doChat(String message) {
        try {
            log.info("=== 开始处理聊天请求 ===");
            log.info("用户消息: {}", message);

            log.debug("准备调用 AI 模型...");
            long startTime = System.currentTimeMillis();

            var aiResponse = chatModel.call(
                    new Prompt(
                            new SystemMessage(DEFAULT_SYSTEM_PROMPT),
                            new UserMessage(message)
                    )
            );

            long endTime = System.currentTimeMillis();
            log.debug("AI 调用完成，耗时: {}ms", endTime - startTime);

            String response = aiResponse.getResult().getOutput().getText();
            log.info("AI 响应成功，内容长度: {}, 耗时: {}ms", response.length(), endTime - startTime);

            return ChatResponse.success(response);
        } catch (Exception e) {
            log.error("AI 聊天处理失败，错误信息: {}", e.getMessage(), e);
            return ChatResponse.error("处理请求时出现错误: " + e.getMessage());
        }
    }

}