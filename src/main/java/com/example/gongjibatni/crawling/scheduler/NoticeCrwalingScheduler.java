//package com.example.gongjibatni.crawling;
//
//
//
//import com.example.gongjibatni.crawling.service.CrawlingService;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import static com.example.gongjibatni.NoticeUrl.ACADEMIC;
//
//@Component
//public class NoticeCrwalingScheduler {
//
//    private final CrawlingService crawlingService;
//
//    public NoticeCrwalingScheduler(CrawlingService crawlingService) {
//        this.crawlingService = crawlingService;
//    }
//
//
//
//    @Scheduled(fixedRateString  = "${crawl.rate}") //3시간
//    public void runCrawlJob(){
//        crawlingService.crawlAndSave(ACADEMIC.getUrl());
//
//
//
//    }
//}
