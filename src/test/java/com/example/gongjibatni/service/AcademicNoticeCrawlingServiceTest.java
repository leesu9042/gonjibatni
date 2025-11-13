package com.example.gongjibatni.service;

import com.example.gongjibatni.crawling.service.AcademicNoticeCrawlingService;
import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
//@Transactional  // 테스트 끝나면 자동 롤백

class AcademicNoticeCrawlingServiceTest {

    @Autowired
    private AcademicNoticeCrawlingService crawlingService;

    @Autowired
    private NoticeRepository noticeRepository;

    @DisplayName("크롤러 Service의 크롤링결과 저장기능 확인 ")
    @Test
    void crawlAndSaveTest() {
        // 테스트할 URL 지정 (학교 공지 페이지 등)
        String url = "https://hanbat.ac.kr/bbs/BBSMSTR_000000000050/list.do?mno=sub07_01";

        crawlingService.crawlAndSave(url);

        // DB에 실제로 저장되었는지 확인
        List<Notice> notices = noticeRepository.findAll();
        System.out.println("저장된 공지 개수: " + notices.size());

        for (Notice n : notices) {
            System.out.println(n.getTitle() + " | " + n.getWriter());
        }
    }
}
