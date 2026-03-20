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
    private final BoardRepository boardRepository; // 전체 개수(count) 조회를 위해 주입

    // RULE: 루트 경로는 게시글 목록(홈)으로 연결
    @GetMapping({"/", "/board"})
    public String home(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        // [Step 2] SQL 기초 페이징 (LIMIT/OFFSET)
        List<BoardResponse.Max> models = boardService.findAllManual(page);
        
        // [Step 4] 전체 페이지 계산 및 번호 UI를 위한 로직
        long totalCount = boardRepository.count(); // T-4.1: 전체 게시글 수 조회
        int limit = 3; // 페이지당 게시글 수
        int totalPages = (int) Math.ceil((double) totalCount / limit); // T-4.2: 총 페이지 수 계산
        boolean last = (page >= totalPages - 1); // 마지막 페이지 여부 판단
        
        BoardResponse.Paging paging = new BoardResponse.Paging(page, last, totalPages);
        
        model.addAttribute("models", models);
        model.addAttribute("paging", paging); 
        return "board/list";
    }
}