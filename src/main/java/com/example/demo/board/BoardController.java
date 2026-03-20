package com.example.demo.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    // RULE: 루트 경로는 게시글 목록(홈)으로 연결
    @GetMapping({"/", "/board"})
    public String home(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        // [Step 2] SQL 기초 페이징 (LIMIT/OFFSET)
        // page 파라미터를 받아 Service에서 OFFSET을 계산함
        List<BoardResponse.Max> models = boardService.findAllManual(page);
        
        model.addAttribute("models", models);
        // Step 2에서는 아직 페이징 버튼(이전/다음)이 화면에 없으므로 DTO를 넘기지 않음
        return "board/list";
    }
}