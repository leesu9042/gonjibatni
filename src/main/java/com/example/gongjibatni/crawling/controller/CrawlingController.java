//package com.example.gongjibatni.crawling.controller;
//
//import com.example.gongjibatni.NoticeUrl;
//import com.example.gongjibatni.crawling.service.AcademicNoticeCrawlingService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/crawl")
//public class CrawlingController {
//
//    private final AcademicNoticeCrawlingService crawlingService;
//
//    public CrawlingController(AcademicNoticeCrawlingService crawlingService) {
//        this.crawlingService = crawlingService;
//    }
//
//    @PostMapping("/ten-pages")
//    public String crawlTenPages() {
//        try {
//            String baseUrl = NoticeUrl.ACADEMIC_NOTICE.getUrl();
//            crawlingService.crawlAndSaveTenPage(baseUrl);
//            return "10페이지 크롤링 완료!";
//        } catch (Exception e) {
//            return "크롤링 실패: " + e.getMessage();
//        }
//    }
//
//}
