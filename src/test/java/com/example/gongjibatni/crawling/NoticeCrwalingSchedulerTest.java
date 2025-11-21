//package com.example.gongjibatni.crawling;
//
//import com.example.gongjibatni.NoticeUrl;
//import com.example.gongjibatni.crawling.service.CrawlingService;
//import org.awaitility.Durations;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.awaitility.Awaitility.await;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//@SpringBootTest
//@TestPropertySource(properties = "crawl.rate=1000")  // 테스트에서는 1초로 실행
//class NoticeCrwalingSchedulerTest {
//
//
//
//    @SpyBean
//    NoticeCrwalingScheduler scheduler; // 실제 스케줄러 빈
//
//    @DisplayName("실제로 크롤링이 주기마다 실행되는지 확인 , 5초에 두번 실행되는가?")
//    @Test
//    void scheduleShouldRunPeriodically() {
//        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
//                verify(scheduler, atLeast(2)).runCrawlJob()
//        );
//
//    }
//
//
//}