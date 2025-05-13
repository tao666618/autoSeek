package com.example.controller;

import com.example.domain.result;
import com.example.server.DeepSeekService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api")
public class GetResult {

    @Autowired
    private DeepSeekService deepSeekService;


    // 接受前端传来的get请求数据
    @GetMapping("getData")
    public result getData(@RequestParam String title,
//                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) String options
                          ) {


        // 1. 先检查标题不能为空
        if (title == null || title.trim().isEmpty()) {
            return new result("400", "题目标题不能为空", null);
        }


        // 2. 组装要发给 deepseek 的完整问题
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(title.trim());
//        如果options参数中不含有字符“黑体”，就将options参数中的内容添加到题目后面
        if (options != null && !options.trim().isEmpty() && !options.contains("黑体")) {
            dataBuilder.append(" 选项：").append(options.trim());
        }


//        if (type != null && !type.trim().isEmpty()) {
//            dataBuilder.append("（类型：").append(type.trim()).append("）");
//        }
        // 处理选项
//        if (options != null && !options.trim().isEmpty()) {
//            StringBuilder optionBuilder = new StringBuilder();
//            char currentLetter = 'A';
//            for (char c : options.trim().toCharArray()) {
//                if (c == ' ') {
//                    optionBuilder.append(currentLetter++);
//                    if (currentLetter > 'Z') {
//                        currentLetter = 'A'; // 循环使用字母
//                    }
//                } else {
//                    optionBuilder.append(c);
//                }
//            }
//            dataBuilder.append(" 选项：").append(optionBuilder.toString().replace("\n", " "));
//        }

        String userMessage = dataBuilder.toString();
        System.out.println("userMessage1 = " + userMessage);

        // 3. 调用 deepseek
        String apiResponse = deepSeekService.getChatCompletion(userMessage);
        System.out.println("apiResponse = " + apiResponse);

        // 4. 解析 deepseek 返回，提取 content
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(apiResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                String content = choicesNode.get(0).path("message").path("content").asText();
                return new result("200", "Success", content);
            } else {
                return new result("500", "choices数组为空", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new result("500", "解析返回数据出错", null);
        }
    }
}
