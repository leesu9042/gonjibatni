//package com.example.gongjibatni.classifier.Tag;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class NoticeTagTest {
//
//    @Test
//    void matches는_키워드포함이면_true() {
//        assertThat(NoticeTag.SCHOLARSHIP.matches("근로장학 신청 안내")).isTrue();
//        assertThat(NoticeTag.COURSE.matches("수강 신청 기간 안내")).isTrue();
//        assertThat(NoticeTag.CONTEST.matches("챌린지 참가자 모집")).isTrue();
//        assertThat(NoticeTag.INTERNSHIP.matches("[대학일자리본부]2025년 하반기 대전형 코업(co-op) 청년 뉴리더 양성사업 학생 모집 안내(~8/12(화)16시)")).isTrue();
//    }
//
//    @Test
//    void matches는_키워드없으면_false() {
//        assertThat(NoticeTag.GRADES.matches("도서관 이용시간 안내")).isFalse();
//    }
//
//    @Test
//    void matches는_대소문자를_무시한다() {
//        assertThat(NoticeTag.INTERNSHIP.matches("Co-Op 프로그램 안내")).isTrue();
//    }
//}