package com.lookmarket.chatbot.service;

import com.lookmarket.chatbot.mapper.BoardBotMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BoardAnswerService {
    private final BoardBotMapper mapper;
    public BoardAnswerService(BoardBotMapper mapper) { this.mapper = mapper; }

    public Optional<String> answerBoard(String q) {
        var rows = mapper.searchBoard(Map.of("q", q, "limit", 3));
        if (rows == null || rows.isEmpty()) return Optional.empty();

        var b = rows.get(0);
        String title = String.valueOf(b.get("b_title"));
        String content = String.valueOf(b.get("b_content"));
        if (content != null && content.length() > 400) content = content.substring(0, 400) + "...";
        return Optional.of("[" + title + "]\n" + content);
    }
}
