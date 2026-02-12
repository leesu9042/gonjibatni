package com.example.gongjibatni.crawling.scheduler;

import com.example.gongjibatni.crawling.service.CrawlingService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

//완
@SpringBootTest(
        classes = {
                NoticeCrwalingScheduler.class,
                NoticeCrwalingSchedulerTest.TestConfig.class
        },
        properties = {
                "crawl.rate=100",
                "spring.task.scheduling.enabled=true"
        }
)
class NoticeCrwalingSchedulerTest {

    @MockBean
    private CrawlingService crawlingService;

    @Test
    void 스케줄러가_주기적으로_크롤링서비스를_호출한다() {
        Awaitility.await()
                .atMost(Duration.ofSeconds(1))
                .untilAsserted(() ->
                        Mockito.verify(crawlingService, Mockito.atLeastOnce())
                                .crawlLatestNotices(Mockito.anyString())
                );
    }
    @EnableScheduling
    static class TestConfig { }

}