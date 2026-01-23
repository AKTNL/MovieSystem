package com.movie.controller;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.entity.es.MovieDoc;
import com.movie.repository.MovieEsRepository;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
public class AiController {
    @Autowired
    private ZhipuAiClient zhipuAiClient;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieEsRepository movieEsRepository;

    @GetMapping("/chat")
    public Result<String> chat(@RequestParam String message) {
        // --- 步骤 1：先用 ES 搜一下用户的话 ---
        // 假设用户输入 "科幻"，我们就去 标题、简介、类型 里找 "科幻"
        List<MovieDoc> esResult = movieEsRepository.findByTitleOrSynopsisOrGenre(message, message, message);

        String movieContext = "";

        // --- 步骤 2：判断搜没搜到 ---
        if (esResult != null && !esResult.isEmpty()) {
            // A. 搜到了！(比如用户问了具体的电影或类型)
            // 取前 5 条最相关的给 AI (避免 Token 太多)
            movieContext = esResult.stream()
                    .limit(5)
                    .map(m -> String.format("《%s》(%.1f分, %s)", m.getTitle(), m.getRating(), m.getGenre()))
                    .collect(Collectors.joining(", "));
            System.out.println("AI 采用了 ES 搜索结果: " + movieContext);
        } else {
            // B. 没搜到 (用户可能只是在打招呼，或者搜了库里没有的东西)
            // 兜底：查 MySQL 的 Top 10 热门电影，让 AI 有话可说
            QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("title", "rating", "genre");
            queryWrapper.orderByDesc("rating");
            queryWrapper.last("LIMIT 10");
            List<Movie> hotMovies = movieService.list(queryWrapper);

            movieContext = hotMovies.stream()
                    .map(m -> String.format("《%s》(%.1f分)", m.getTitle(), m.getRating()))
                    .collect(Collectors.joining(", "));
            System.out.println("AI 采用了热门榜单兜底");
        }

        // --- 步骤 3：构建 Prompt ---
        List<ChatMessage> messages = new ArrayList<>();

        String systemPrompt = """
            你是一个电影导购助手'小影'。
            
            【你的知识库】：
            %s
            
            【规则】：
            1. 优先根据【你的知识库】里的电影回答用户。
            2. 如果知识库里有用户想要的电影，请重点推荐。
            3. 如果知识库里**没有**用户问的电影，请礼貌告知“暂无资源”，并从知识库里推荐一部其他的。
            4. 语气要活泼可爱。
            """.formatted(movieContext);

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), message));

        // 3. 添加用户的问题
        ChatMessage userMsg = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(userMsg);

        // 4. 构建请求参数 (使用 glm-4.7-flash 模型，速度快)
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("glm-4.7-flash")
                .messages(messages)
                .temperature(0.7f) // 0.7 比较平衡，既有创意又不会太乱
                .build();

        int maxRetries = 3; // 最多重试3次
        for (int i = 0; i < maxRetries; i++) {
            try {
                ChatCompletionResponse response = zhipuAiClient.chat().createChatCompletion(request);
                if (response.isSuccess()) {
                    return Result.success(response.getData().getChoices().get(0).getMessage().getContent().toString());
                }
                // 如果不是成功，跳出循环报错
                return Result.error("AI 响应异常: " + response.getMsg());

            } catch (Exception e) {
                // 如果是最后一次尝试，或者不是 429 错误，直接抛出
                // 注意：这里简单判断异常信息包含 429
                if (i == maxRetries - 1 || !e.getMessage().contains("429")) {
                    e.printStackTrace();
                    return Result.error("AI 太忙了，请稍后再试");
                }

                // 如果是 429，等待 1 秒后重试
                try {
                    System.out.println("触发限流，正在重试第 " + (i + 1) + " 次...");
                    Thread.sleep(1000); // 睡 1 秒
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return Result.error("请求超时");
    }
}
