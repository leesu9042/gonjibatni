package com.example.gongjibatni.classifier.Tag;

import java.util.List;
import java.util.Set;

/**
 * 공지사항 태그
 * 하나의 태그는 여러 키워드를 가질 수 있음
 */
public enum NoticeTag {
    // 학적/성적 관련
    LEAVE("휴학", List.of("휴학")),
    RETURN("복학", List.of("복학")),
    GRADES("성적", List.of("성적", "학점", "평점")),
    GRADUATION("졸업", List.of("졸업")),
    // 학사/수업 관련
    COURSE("수강", List.of("수강신청", "수강 신청", "수강정정", "수강 정정", "수강변경", "수강 변경", "수강철회", "폐강", "분반","계절")),

    // 교내생활 관련
    SCHOLARSHIP("장학", List.of("장학","근로","근로장학")),  // "장학"만 있어도 충분
    TUITION("등록금", List.of("등록금", "학비")),

    // 진로/경력개발 관련
    EMPLOYMENT("취업", List.of("취업", "구인","채용")),
    CONTEST("공모전", List.of("공모전", "경진", "대회","해커톤","챌린지")),
    OVERSEAS("해외", List.of("해외", "국외","교환","교환학생")),
    INTERNSHIP("인턴", List.of("코업", "co-op","뉴리더","현장실습","인턴"));


    private final String description;
    private final List<String> keywords;

    NoticeTag(String description, List<String> keywords) {
        this.description = description;
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * 제목에 이 태그의 키워드가 포함되어 있는지 확인
     */
    public boolean matches(String title) {
        String lowerTitle = title.toLowerCase();
        return keywords.stream()
                .anyMatch(keyword -> lowerTitle.contains(keyword.toLowerCase()));
    }
}

//이런 반복작업들은 AI한테 맡기고 나는 검수만함. (Claude)