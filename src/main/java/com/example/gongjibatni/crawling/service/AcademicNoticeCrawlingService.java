package com.example.gongjibatni.crawling.service;



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
    private final NoticeTagger noticeTagger;



    @Autowired
    public AcademicNoticeCrawlingService(AcademicNoticeCrawler academicNoticeCrawler,
                                         NoticeRepository noticeRepository,
                                         NoticeSyncService noticeSyncService,
                                         AcademicNoticeService academicNoticeService,
                                         NoticeTagger noticeTagger
    ) {

        this.academicNoticeCrawler = academicNoticeCrawler;
        this.noticeRepository = noticeRepository;
        this.noticeSyncService = noticeSyncService;
        this.academicNoticeService = academicNoticeService;
        this.noticeTagger = noticeTagger;

    }

    //단위테스트를위해서 데이터 불러오기 / 데이터처리로 나누기
    //데이터처리만 단위테스트
    @Transactional
    public void syncNotices(List<Notice> notices) { // 데이터처리


        //없으면 빈 list반환
        //새로운 Notice필터링
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(notices);

        if(!newNotices.isEmpty()) {
            for (Notice notice : newNotices) {
                Set<NoticeTag> noticeTags = noticeTagger.extractTags(notice.getTitle());
                notice.getTags().addAll(noticeTags);
            }
            noticeRepository.saveAll(newNotices);
            academicNoticeService.evictFirstWindowCache();// 캐시제거
        }
    }

    @Transactional
    public void crawlLatestNotices(String url) { //외부에서 데이터 가져오기
        Document doc = academicNoticeCrawler.LoadFromURL(url);
        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);





        syncNotices(notices); //외부 데이터랑 내부 데이터 (DB를) 동기화
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
//                String keyword = keywordClassifier.classifyKeyword(notice.getTitle());
//                notice.setCategoryKeyword(keyword);
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
