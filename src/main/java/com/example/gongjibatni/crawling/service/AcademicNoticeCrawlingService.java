package com.example.gongjibatni.crawling.service;



import com.example.gongjibatni.classifier.AcademicNoticeKeywordClassifier;
import com.example.gongjibatni.classifier.Tag.NoticeTag;
import com.example.gongjibatni.classifier.Tag.NoticeTagger;
import com.example.gongjibatni.crawling.crawler.AcademicNoticeCrawler;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import com.example.gongjibatni.notice.service.AcademicNoticeService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AcademicNoticeCrawlingService implements CrawlingService{

    private final AcademicNoticeCrawler academicNoticeCrawler;
    private final NoticeRepository noticeRepository;
    private final NoticeSyncService noticeSyncService;
    private final AcademicNoticeService academicNoticeService;
    private final AcademicNoticeKeywordClassifier keywordClassifier;
    private final NoticeTagger noticeTagger;



    @Autowired
    public AcademicNoticeCrawlingService(AcademicNoticeCrawler academicNoticeCrawler,
                                         NoticeRepository noticeRepository,
                                         NoticeSyncService noticeSyncService,
                                         AcademicNoticeService academicNoticeService,
                                         AcademicNoticeKeywordClassifier keywordClassifier,
                                         NoticeTagger noticeTagger
    ) {

        this.academicNoticeCrawler = academicNoticeCrawler;
        this.noticeRepository = noticeRepository;
        this.noticeSyncService = noticeSyncService;
        this.academicNoticeService = academicNoticeService;
        this.keywordClassifier = keywordClassifier;
        this.noticeTagger = noticeTagger;

    }

    //단위테스트를위해서 데이터 불러오기 / 데이터처리로 나누기
    //데이터처리만 단위테스트
    @Transactional
    public void syncNotices(List<Notice> notices) { // 데이터처리
        //없으면 빈 list반환
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(notices);

        if(!newNotices.isEmpty()) {
            for (Notice notice : newNotices) {
                Set<NoticeTag> noticeTags = noticeTagger.extractTags(notice.getTitle());
                notice.getTags().addAll(noticeTags);
            }
            noticeRepository.saveAll(newNotices);
            academicNoticeService.evictFirstWindowCache();
        }
    }

    @Transactional
    public void crawlLatestNotices(String url) { //외부에서 데이터 가져오기
        Document doc = academicNoticeCrawler.LoadFromURL(url);
        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);
        syncNotices(notices); //외부 데이터랑 내부 데이터 (DB를) 동기화
    }


    @Transactional
    public void crawlAndSaveTenPage(String baseUrl) {

        List<Notice> allNewNotices = new ArrayList<>();
        List<Integer> failedPages = new ArrayList<>();
        Random random = new Random();


        // 1차 시도: 10페이지 모두
        for (int page = 1; page <= 10; page++) {
            String pageUrl = buildPageUrl(baseUrl, page);
            System.out.println("\n━━━ 페이지 " + page + "/10 크롤링 중 ━━━");

            Document doc = academicNoticeCrawler.LoadFromURL(pageUrl);

            if (doc == null) {
                System.err.println("❌ 페이지 " + page + " - Document null");
                failedPages.add(page);  // 실패 목록에 추가
                continue;
            }

            // 크롤링 성공 처리
            processPage(doc, page, allNewNotices);

            // 다음 페이지 전 대기
            // 🔥 랜덤 대기: 7~12초 사이
            if (page < 10) {
                int waitTime = 7000 + random.nextInt(5000); // 7~12초
                System.out.println("⏳ " + (waitTime/1000) + "초 대기...");
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // 2차 시도: 실패한 페이지만 재시도
        if (!failedPages.isEmpty()) {
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("🔄 실패한 페이지 재시도: " + failedPages);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━\n");

            List<Integer> stillFailed = new ArrayList<>();

            for (int page : failedPages) {
                String pageUrl = buildPageUrl(baseUrl, page);
                System.out.println("🔄 페이지 " + page + " 재시도 중...");

                Document doc = academicNoticeCrawler.LoadFromURL(pageUrl);

                if (doc == null) {
                    stillFailed.add(page);
                    System.err.println("❌ 페이지 " + page + " 재시도 실패");
                } else {
                    processPage(doc, page, allNewNotices);
                    System.out.println("✅ 페이지 " + page + " 재시도 성공!");
                }

                try {
                    Thread.sleep(7000); // 재시도는 더 길게 대기
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // 여전히 실패한 페이지가 있으면 경고
            if (!stillFailed.isEmpty()) {
                System.err.println("\n⚠️⚠️⚠️ 최종 실패 페이지: " + stillFailed);
                // 필요하면 예외 발생: throw new RuntimeException("크롤링 실패: " + stillFailed);
            }
        }

        // 모든 페이지 처리 후 한 번만 저장
        if (!allNewNotices.isEmpty()) {
            noticeRepository.saveAll(allNewNotices);
            academicNoticeService.evictFirstWindowCache();
            System.out.println("\n🎉 총 " + allNewNotices.size() + "개 공지 저장 완료!");
        } else {
            System.out.println("\n📭 저장할 새 공지가 없습니다");
        }
    }

    // 페이지 처리 로직 분리
    private void processPage(Document doc, int pageNum, List<Notice> allNewNotices) {
        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);

        // NoticeNo가 없는 것만 필터링
        List<Notice> filteredNotice = notices.stream()
                .filter(n -> !noticeRepository.existsByNoticeNo(n.getNoticeNo()))
                .toList();

        if (!filteredNotice.isEmpty()) {
            // 키워드 분류
            filteredNotice.forEach(notice -> {
                String keyword = keywordClassifier.classifyKeyword(notice.getTitle());
                notice.setCategoryKeyword(keyword);
            });

            allNewNotices.addAll(filteredNotice);
            System.out.println("✅ 페이지 " + pageNum + " - 새 공지 " + filteredNotice.size() + "개 발견");
        } else {
            System.out.println("ℹ️ 페이지 " + pageNum + " - 새 공지 없음");
        }
    }

    private String buildPageUrl(String baseUrl, int page) {
        return baseUrl + "?pageIndex=" + page;
    }



    public Optional<Notice> findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId);
    }


}
