package com.example.gongjibatni.crawling.service;



import com.example.gongjibatni.crawling.AcademicNoticeCrawler;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.repository.NoticeRepository;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicNoticeCrawlingService  {

    private final AcademicNoticeCrawler academicNoticeCrawler;
    private final NoticeRepository noticeRepository;


    @Autowired
    public AcademicNoticeCrawlingService(AcademicNoticeCrawler academicNoticeCrawler,
                                         NoticeRepository noticeRepository) {
        this.academicNoticeCrawler = academicNoticeCrawler;
        this.noticeRepository = noticeRepository;
    }



    public void crawlAndSave(String url) {
        Document doc = academicNoticeCrawler.LoadFromURL(url);
        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);

        noticeRepository.saveAll(notices);
    }



    public Optional<Notice> findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId);
    }









}
