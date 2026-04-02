package com.qf.springaialibaba.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;


/**
 * 测试ai交互
 *
 * @author ly
 */
@Component
public class TestController implements CommandLineRunner {

    @Resource
    private ChatModel chatModel;

    @Override
    public void run(String... args) throws GraphRunnerException {


        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("Get weather for a given city")
                .inputType(WeatherRequest.class)
                .build();

        // 创建 agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .tools(weatherTool)
                .systemPrompt("You are a helpful assistant")
                .saver(new MemorySaver())
                .build();

        // 运行 agent
        AssistantMessage response = agent.call("what is the weather in San Francisco");
        System.out.println(response.getText());
    }


    /**
     * 天气查询请求参数
     */
    public static class WeatherRequest {
        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    /**
     * 定义天气查询工具
     */
    public static class WeatherTool implements BiFunction<WeatherRequest, ToolContext, String> {
        @Override
        public String apply(WeatherRequest request, ToolContext toolContext) {
            return "It's always sunny in " + request.getCity() + "!";
        }
    }

}