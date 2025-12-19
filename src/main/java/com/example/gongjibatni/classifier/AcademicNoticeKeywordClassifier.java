package com.example.gongjibatni.classifier;

import com.google.genai.Client;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// title 들어오면 그걸가지고 keyword리턴하는 함수
@Service
public class AcademicNoticeKeywordClassifier implements NoticeKeywordClassifier {
    // 키워드 사전
    private static final Map<String, List<String>> KEYWORD_PATTERNS = Map.of(
            "학사", List.of("수강", "수업", "강의", "학점", "성적", "휴학", "복학", "졸업", "전과", "부전공"),
            "장학", List.of("장학금", "장학생", "등록금", "학자금", "근로장학", "국가장학금"),
            "취업", List.of("취업", "채용", "인턴", "기업", "면접", "이력서", "자소서", "설명회", "박람회"),
            "학술", List.of("특강", "세미나", "워크샵", "포럼", "학회", "강연"),
            "대회", List.of("대회", "공모전", "경진대회", "competition", "contest"),
            "행사", List.of("행사", "이벤트", "프로그램", "축제", "공연", "콘서트", "영화", "전시"),
            "시설", List.of("도서관", "기숙사", "식당", "주차", "셔틀", "체육관", "열람실"),
            "학생지원", List.of("상담", "심리", "건강", "의료", "보건", "학생증"),
            "국제", List.of("교환학생", "어학연수", "외국인", "유학", "해외")
    );
    // ⭐ 우선순위 (높을수록 우선)
    private static final Map<String, Integer> CATEGORY_PRIORITY = Map.of(
            "학사", 9,
            "장학", 8,
            "취업", 7,
            "대회", 6,
            "학술", 5,
            "행사", 4,
            "시설", 3,
            "학생지원", 2,
            "국제", 1
    );
    @Override
    public String classifyKeyword(String title) {
        if (title == null || title.isBlank()) {
            return "일반";
        }

        String normalizedTitle = title.toLowerCase();

        //점수 매기기
        // ex) 학사 : 1 , 장학 : 1,
        Map<String, Integer> scores = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : KEYWORD_PATTERNS.entrySet()) {
            String keyword = entry.getKey();
            List<String> patterns = entry.getValue();
            int score = 0;
            for (String pattern : patterns) {
                if (normalizedTitle.contains(pattern)) {
                    score++;
                }
            }
            scores.put(keyword, score);
        }

        // 최고 점수 찾기
        int maxScore = scores.values().stream()
                .max(Integer::compareTo)
                .orElse(0);
        if (maxScore == 0) {
            return "일반";
        }

        // ⭐ 동점자들 중 우선순위 높은 것 선택
        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() == maxScore)
                .max((e1, e2) -> {
                    // 우선순위 비교
                    int priority1 = CATEGORY_PRIORITY.getOrDefault(e1.getKey(), 0);
                    int priority2 = CATEGORY_PRIORITY.getOrDefault(e2.getKey(), 0);
                    return Integer.compare(priority1, priority2);
                })
                .map(Map.Entry::getKey)
                .orElse("일반");
    }
}
