package com.example.demo.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    // RULE: 루트 경로는 게시글 목록(홈)으로 연결
    @GetMapping({"/", "/board"})
    public String home(Model model) {
        List<BoardResponse.Max> models = boardService.findAll();
        model.addAttribute("models", models);
        return "home";
    }
}
