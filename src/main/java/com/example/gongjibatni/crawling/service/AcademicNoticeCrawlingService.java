package com.example.gongjibatni.crawling.service;



import com.example.gongjibatni.crawling.AcademicNoticeCrawler;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicNoticeCrawlingService implements CrawlingService{

    private final AcademicNoticeCrawler academicNoticeCrawler;
    private final NoticeRepository noticeRepository;
    private final NoticeSyncService noticeSyncService;


    @Autowired
    public AcademicNoticeCrawlingService(AcademicNoticeCrawler academicNoticeCrawler,
                                         NoticeRepository noticeRepository,
                                         NoticeSyncService noticeSyncService) {

        this.academicNoticeCrawler = academicNoticeCrawler;
        this.noticeRepository = noticeRepository;
        this.noticeSyncService = noticeSyncService;

    }



    public void crawlAndSave(String url) {
        Document doc = academicNoticeCrawler.LoadFromURL(url);
        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(notices); //필터링

        if(newNotices.isEmpty()) {
            return;
        }
        else {
            noticeRepository.saveAll(newNotices);
        }


    }



    public Optional<Notice> findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId);
    }



}
