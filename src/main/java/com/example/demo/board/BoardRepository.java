package com.example.demo.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 수동 페이징을 위한 Native Query (공부 목적)
    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM board_tb ORDER BY id DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    java.util.List<Board> findAllManual(@org.springframework.data.repository.query.Param("limit") int limit, @org.springframework.data.repository.query.Param("offset") int offset);
}
