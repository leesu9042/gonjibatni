package com.example.gongjibatni.classifier.Tag;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.gongjibatni.classifier.Tag.NoticeTag;
import com.example.gongjibatni.classifier.Tag.NoticeTagger;
import org.junit.jupiter.api.Test;

class NoticeTaggerTest {

    private final NoticeTagger tagger = new NoticeTagger();

    @Test
    void 제목이_null이면_빈세트() {
        assertThat(tagger.extractTags(null)).isEmpty();
    }

    @Test
    void 제목이_blank이면_빈세트() {
        assertThat(tagger.extractTags("   ")).isEmpty();
    }

    @Test
    void 키워드가_포함되면_해당태그를_추출한다() {
        assertThat(tagger.extractTags("2026 장학금 신청 안내"))
                .contains(NoticeTag.SCHOLARSHIP);

        assertThat(tagger.extractTags("수강신청 일정 공지"))
                .contains(NoticeTag.COURSE);

        assertThat(tagger.extractTags("해커톤 참가자 모집"))
                .contains(NoticeTag.CONTEST);

        assertThat(tagger.extractTags("교환학생 선발 안내"))
                .contains(NoticeTag.OVERSEAS);
    }

    @Test
    void 여러태그가_동시에_매칭되면_모두_추출한다() {
        assertThat(tagger.extractTags("수강신청 및 장학금 신청 안내"))
                .contains(NoticeTag.COURSE, NoticeTag.SCHOLARSHIP);
    }

    @Test
    void 같은태그는_중복없이_한번만_들어간다() {
        // COURSE 관련 키워드가 여러 번 포함돼도 결과는 Set이라 1개
        assertThat(tagger.extractTags("수강신청 수강정정 수강변경 안내"))
                .containsOnly(NoticeTag.COURSE);
    }

    @Test
    void 대소문자_무시한다() {
        // INTERNSHIP keywords: "co-op" 포함
        assertThat(tagger.extractTags("CO-OP 참여자 모집"))
                .contains(NoticeTag.INTERNSHIP);
    }

    @Test
    void 어떤태그키워드도_없으면_빈세트() {
        assertThat(tagger.extractTags("도서관 이용시간 안내")).isEmpty();
    }
}