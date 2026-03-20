package com.example.demo.board;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

public class BoardResponse {

    // RULE: Max DTO는 테이블 전체 컬럼 (상세·목록 겸용)
    @Data
    public static class Max {
        private Integer id;
        private String title;
        private String content;

        public Max(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    // RULE: Detail DTO는 조인 포함 확장 정보
    @Data
    public static class Detail {
        private Integer id;
        private String title;
        private String content;
        private String username;

        public Detail(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.username = board.getUser().getUsername();
        }
    }

    // 페이징 메타데이터를 담는 DTO (학습 목적)
    @Data
    public static class Paging {
        private boolean first;
        private boolean last;
        private int prevPage;
        private int nextPage;

        public Paging(int page, boolean last) {
            this.first = (page == 0);
            this.last = last;
            this.prevPage = page - 1;
            this.nextPage = page + 1;
        }
    }
}
