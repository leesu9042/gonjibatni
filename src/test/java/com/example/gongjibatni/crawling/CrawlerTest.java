package com.example.gongjibatni.crawling;

import com.example.gongjibatni.notice.domain.Notice;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CrawlerTest {

    @Autowired
    private AcademicNoticeCrawler academicNoticeCrawler;

    @Test
    public void LoadUrl(){
        // 실제 크롤링
        String url = "https://hanbat.ac.kr/bbs/BBSMSTR_000000000050/list.do?mno=sub07_01"; // 실제 URL로 바꿔주세요
        Document doc = academicNoticeCrawler.LoadFromURL(url);

        List<Notice> notices = academicNoticeCrawler.parsingElementsToNotices(doc);

        // 단순 출력
        for (Notice n : notices) {
            System.out.println(n.getNoticeNo() + " - " + n.getTitle());
        }

    }
}