package com.example.demo.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * DTO는 Service에서 만든다. Entity를 Controller에 전달하지 않는다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.Max> findAll() {
        // RULE: 목록은 최신순으로 조회
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Board> boardList = boardRepository.findAll(sort);

        // Entity -> DTO 변환
        return boardList.stream()
                .map(BoardResponse.Max::new)
                .collect(Collectors.toList());
    }
}
