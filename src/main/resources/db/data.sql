-- 유저 더미 데이터
INSERT INTO user_tb (username, password, created_at) VALUES ('ssar', '$2a$10$cQwLuTjz5UIEnuY2rmU/AO5uKYYNzBUmc8bCNHtjSIUvZWi8H8sxe', NOW());
INSERT INTO user_tb (username, password, created_at) VALUES ('cos', '$2a$10$cQwLuTjz5UIEnuY2rmU/AO5uKYYNzBUmc8bCNHtjSIUvZWi8H8sxe', NOW());

-- 게시글 더미 데이터
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('첫 번째 게시글', '안녕하세요. ssar의 첫 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('두 번째 게시글', '안녕하세요. ssar의 두 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('세 번째 게시글', '안녕하세요. cos의 첫 번째 글입니다.', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('네 번째 게시글', '네 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('다섯 번째 게시글', '다섯 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('여섯 번째 게시글', '여섯 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('일곱 번째 게시글', '일곱 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('여덟 번째 게시글', '여덟 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('아홉 번째 게시글', '아홉 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열 번째 게시글', '열 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열한 번째 게시글', '열한 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열두 번째 게시글', '열두 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열세 번째 게시글', '열세 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열네 번째 게시글', '열네 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열다섯 번째 게시글', '열다섯 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열여섯 번째 게시글', '열여섯 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열일곱 번째 게시글', '열일곱 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열여덟 번째 게시글', '열여덟 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열아홉 번째 게시글', '열아홉 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('스무 번째 게시글', '스무 번째 글입니다.', 1, NOW());

-- 댓글 더미 데이터
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('첫 번째 게시글에 ssar이 작성한 댓글입니다.', 1, 1, NOW());
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('첫 번째 게시글에 cos가 작성한 댓글입니다.', 2, 1, NOW());
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('두 번째 게시글에 cos가 작성한 댓글입니다.', 2, 2, NOW());
