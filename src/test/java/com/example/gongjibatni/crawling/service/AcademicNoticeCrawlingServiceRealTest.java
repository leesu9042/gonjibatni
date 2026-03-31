//package com.example.gongjibatni.crawling.service;
//
//import com.example.gongjibatni.NoticeUrl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.example.gongjibatni.notice.domain.Notice;
//import com.example.gongjibatni.notice.repository.NoticeRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.springframework.test.context.ActiveProfiles;
//
//
//import java.util.List;
//
//
//
//@SpringBootTest
//@ActiveProfiles("test")
//class AcademicNoticeCrawlingSinglePageTest {
//
//    @Autowired
//    private AcademicNoticeCrawlingService crawlingService;
//
//    @Autowired
//    private NoticeRepository noticeRepository;
//
//    @Test
//    @DisplayName("단일 페이지 크롤링 테스트")
//    void crawlAndSave_테스트() {
//        // Given
//        String baseUrl = NoticeUrl.ACADEMIC_NOTICE.getUrl();
//
//        long beforeCount = noticeRepository.count();
//        System.out.println("=== 10페이지 크롤링 시작 ===");
//        System.out.println("크롤링 전 DB 공지 개수: " + beforeCount);
//
//        // When
//        long startTime = System.currentTimeMillis();
//
//        for (int page = 1; page <= 10; page++) {
//            String url = baseUrl + "?pageIndex=" + page;
//            System.out.println("\n페이지 " + page + " 크롤링 중: " + url);
//
//            try {
//                crawlingService.crawlLatestNotices(url);
//                System.out.println("페이지 " + page + " 완료");
//
//                // 각 페이지 사이에 1초 대기 (중요!)
//                if (page < 10) {  // 마지막 페이지 후엔 대기 안 함
//                    Thread.sleep(1000);  // 1000ms = 1초
//                }
//
//            } catch (Exception e) {
//                System.err.println("페이지 " + page + " 크롤링 실패: " + e.getMessage());
//            }
//        }
//
//
//    }
//
//
//
//
//
//    @Test
//    @DisplayName("10페이지 크롤링 - 시각적 확인용")
//    void crawlAndSaveTenPage_시각적확인() {
//        // Given
//        String baseUrl = NoticeUrl.ACADEMIC_NOTICE.getUrl();
//
//        System.out.println("\n");
//        System.out.println("═══════════════════════════════════════════════");
//        System.out.println("        10페이지 크롤링 테스트 시작");
//        System.out.println("═══════════════════════════════════════════════");
//
//        long beforeCount = noticeRepository.count();
//        System.out.println("📊 크롤링 전 DB 공지 개수: " + beforeCount);
//        System.out.println();
//
//        // When
//        long startTime = System.currentTimeMillis();
//
//        crawlingService.crawlAndSaveTenPage(baseUrl);
//
//        long endTime = System.currentTimeMillis();
//        long duration = (endTime - startTime) / 1000; // 초 단위
//
//        // Then - 결과 확인
//        long afterCount = noticeRepository.count();
//        long addedCount = afterCount - beforeCount;
//
//        System.out.println("\n");
//        System.out.println("═══════════════════════════════════════════════");
//        System.out.println("              크롤링 결과 요약");
//        System.out.println("═══════════════════════════════════════════════");
//        System.out.println("⏱️  소요 시간: " + duration + "초");
//        System.out.println("📊 크롤링 전: " + beforeCount + "개");
//        System.out.println("📊 크롤링 후: " + afterCount + "개");
//        System.out.println("✨ 새로 추가된 공지: " + addedCount + "개");
//        System.out.println("═══════════════════════════════════════════════");
//
//        // 최근 추가된 공지 10개 출력
//        if (addedCount > 0) {
//            System.out.println("\n📋 최근 추가된 공지 미리보기:");
//            System.out.println("───────────────────────────────────────────────");
//
//            List<Notice> recentNotices = noticeRepository.findAll();
//            recentNotices.stream()
//                    .limit(10)
//                    .forEach(notice -> {
//                        System.out.println("• [" + notice.getCategoryKeyword() + "] " +
//                                notice.getTitle() +
//                                " (번호: " + notice.getNoticeNo() + ")");
//                    });
//            System.out.println("───────────────────────────────────────────────");
//        }
//
//        System.out.println("\n✅ 테스트 완료!\n");
//    }
//
//
//
//    @Test
//    @DisplayName("10페이지 크롤링 테스트")
//    void crawlAndSaveTenPage_테스트() {
//        // given
//        String baseUrl = NoticeUrl.ACADEMIC_NOTICE.getUrl();
//        long beforeCount = noticeRepository.count();
//
//        System.out.println("=== 10페이지 크롤링 시작 ===");
//        System.out.println("크롤링 전: " + beforeCount + "개");
//
//        // when
//        crawlingService.crawlAndSaveTenPage(baseUrl);
//
//        // then
//        long afterCount = noticeRepository.count();
//        long added = afterCount - beforeCount;
//
//        System.out.println("크롤링 후: " + afterCount + "개");
//        System.out.println("추가됨: " + added + "개");
//        System.out.println("=== 완료 ===");
//    }
//
//
//
//
//}