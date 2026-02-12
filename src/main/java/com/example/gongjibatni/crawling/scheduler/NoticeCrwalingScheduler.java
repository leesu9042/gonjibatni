package com.example.gongjibatni.crawling.scheduler;



import com.example.gongjibatni.crawling.service.CrawlingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.example.gongjibatni.NoticeUrl.ACADEMIC;

@Component
public class NoticeCrwalingScheduler {

    private final CrawlingService crawlingService;
    private static final Log log = LogFactory.getLog(NoticeCrwalingScheduler.class);


    public NoticeCrwalingScheduler(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }



    @Scheduled(fixedRateString = "${crawl.rate:10800000}") //3시간
    public void runCrawlJob(){

        log.info("[CrawlScheduler] academic notice crawl started");
        crawlingService.crawlLatestNotices(ACADEMIC.getUrl());


    }
}