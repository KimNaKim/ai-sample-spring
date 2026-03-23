package com.example.demo.board;

import java.io.IOException;
import java.util.List;

import com.example.demo._core.utils.Resp;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @GetMapping({"/", "/board"})
    public String home(@RequestParam(name = "page", defaultValue = "1") int page, Model model, HttpServletResponse response) throws IOException {
        // 1. 페이지 최소값 검증
        if (page < 1) {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println(Resp.script("1페이지가 시작입니다."));
            return null;
        }

        // 2. 전체 페이지 수 계산
        long totalCount = boardRepository.count();
        int limit = 3;
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        // 3. 페이지 최대값 검증 (데이터가 있을 때만)
        if (totalCount > 0 && page > totalPages) {
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println(Resp.script("마지막 페이지입니다."));
            return null;
        }

        // 4. 페이징 데이터 조회 (1-based -> 0-based 변환)
        List<BoardResponse.Max> models = boardService.findAllManual(page - 1);
        
        // 5. 페이징 메타데이터 생성
        boolean last = (page >= totalPages);
        BoardResponse.Paging paging = new BoardResponse.Paging(page, last, totalPages);
        
        model.addAttribute("models", models);
        model.addAttribute("paging", paging); 
        
        return "board/list";
    }
}
