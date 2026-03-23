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

    // 목록 화면 전체를 위한 DTO
    @Data
    public static class ListDTO {
        private java.util.List<Max> boards;
        private Paging paging;

        public ListDTO(java.util.List<Max> boards, int page, boolean last, int totalPages) {
            this.boards = boards;
            this.paging = new Paging(page, last, totalPages);
        }
    }

    // 페이징 메타데이터를 담는 DTO
    @Data
    public static class Paging {
        private boolean first;
        private boolean last;
        private int prevPage;
        private int nextPage;
        private int current;
        private java.util.List<PageNumber> pageNumbers;

        @Data
        public static class PageNumber {
            private int number;
            private boolean current;

            public PageNumber(int number, int current) {
                this.number = number;
                this.current = (number == current);
            }
        }

        public Paging(int page, boolean last, int totalPages) {
            this.first = (page == 1);
            this.last = last;
            this.prevPage = page - 1;
            this.nextPage = page + 1;
            this.current = page;
            
            // 페이지 번호 리스트 생성 (PageNumber 객체 리스트)
            this.pageNumbers = new java.util.ArrayList<>();
            for (int i = 1; i <= totalPages; i++) {
                this.pageNumbers.add(new PageNumber(i, page));
            }
        }
    }
}
