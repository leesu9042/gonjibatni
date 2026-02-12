package com.example.gongjibatni.classifier.Tag;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 공지사항 제목에서 태그를 자동으로 추출
 */
@Component
public class NoticeTagger {

    /**
     * 공지사항 제목에서 매칭되는 태그들을 추출
     * @param title 공지사항 제목
     * @return 추출된 태그 세트
     */
    public Set<NoticeTag> extractTags(String title) {
        if (title == null || title.isBlank()) {
            return new HashSet<>();
        }

        Set<NoticeTag> extractedTags = new HashSet<>();

        // 모든 태그를 순회하며 매칭 확인
        for (NoticeTag tag : NoticeTag.values()) {
            if (tag.matches(title)) {  // 여기서 boolean 체크
                extractedTags.add(tag);  // true면 태그 추가
            }
        }

        return extractedTags;
    }
}
