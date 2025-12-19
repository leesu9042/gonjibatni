package com.example.gongjibatni.crawling.service;



import com.example.gongjibatni.classifier.AcademicNoticeKeywordClassifier;
import com.example.gongjibatni.crawling.parser.AcademicNoticeCrawler;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import com.example.gongjibatni.notice.service.AcademicNoticeService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicNoticeCrawlingService implements CrawlingService{

    private final AcademicNoticeCrawler academicNoticeCrawler;
    private final NoticeRepository noticeRepository;
    private final NoticeSyncService noticeSyncService;
    private final AcademicNoticeService academicNoticeService;
    private final AcademicNoticeKeywordClassifier keywordClassifier;


    @Autowired
    public AcademicNoticeCrawlingService(AcademicNoticeCrawler academicNoticeCrawler,
                                         NoticeRepository noticeRepository,
                                         NoticeSyncService noticeSyncService,
                                         AcademicNoticeService academicNoticeService,
                                         AcademicNoticeKeywordClassifier keywordClassifier
    ) {

        this.academicNoticeCrawler = academicNoticeCrawler;
        this.noticeRepository = noticeRepository;
        this.noticeSyncService = noticeSyncService;
        this.academicNoticeService = academicNoticeService;
        this.keywordClassifier = keywordClassifier;

    }



    @Transactional
    public void crawlAndSave(String url) {
        Document doc = academicNoticeCrawler.LoadFromURL(url);
        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(notices); // 최신것만 필터링

        if(!newNotices.isEmpty()) {


            noticeRepository.saveAll(newNotices);

            // 키워드 분류 추가
            newNotices.forEach(notice -> {
                String keyword = keywordClassifier.classifyKeyword(notice.getTitle());
                notice.setCategoryKeyword(keyword);
            });




            //캐시무효화
            academicNoticeService.evictFirstWindowCache();


        }







    }



    public Optional<Notice> findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId);
    }



}
