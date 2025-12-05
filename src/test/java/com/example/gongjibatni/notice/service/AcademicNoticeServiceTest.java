package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Window;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional  // 각 테스트 후 자동 롤백
@TestPropertySource(properties = {
        "spring.task.scheduling.enabled=false"
})
class AcademicNoticeServiceIntegrationTest {

    @Autowired
    private AcademicNoticeService academicNoticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        // 기존 데이터 삭제
        noticeRepository.deleteAll();

        // 테스트 데이터 30개 생성 (noticeNo: 100 ~ 71)
        for (int i = 100; i >= 71; i--) {
            Notice notice = new Notice();
            notice.setNoticeNo(i);
            notice.setTitle("학사공지 " + i);
            notice.setWriter("학사팀");
            notice.setCreatedAt(LocalDate.now().minusDays(100 - i));
            notice.setNttid("nttid_" + i);
            notice.setCategoryKeyword("학사");

            noticeRepository.save(notice);
        }

        // 변경사항을 즉시 DB에 반영
        noticeRepository.flush();
    }

    @Test
    @DisplayName("첫 번째 Window 가져오기 - 10개 조회")
    void getFirstWindow() {
        // when
        Window<Notice> window = academicNoticeService.getFirstWindow();

        // then
        assertThat(window).isNotNull();
        assertThat(window.getContent()).hasSize(10);
        assertThat(window.hasNext()).isTrue();

        List<Notice> notices = window.getContent();
        assertThat(notices.get(0).getNoticeNo()).isEqualTo(100);
        assertThat(notices.get(9).getNoticeNo()).isEqualTo(91);
    }



    @Test
    @DisplayName("다음 Window 가져오기 - 커서 기반")
    void getNextWindow() {
        // given - 첫 페이지
        Window<Notice> firstWindow = academicNoticeService.getFirstWindow();

        assertThat(firstWindow.getContent()).isNotEmpty(); // 데이터 확인

        Notice lastNotice = firstWindow.getContent()
                .get(firstWindow.getContent().size() - 1);

        // when - 다음 페이지
        Window<Notice> nextWindow = academicNoticeService.getNextWindow(
                lastNotice.getNoticeId(),
                lastNotice.getNoticeNo()
        );

        // then
        assertThat(nextWindow).isNotNull();
        assertThat(nextWindow.getContent()).hasSize(10);
        assertThat(nextWindow.hasNext()).isTrue();

        List<Notice> notices = nextWindow.getContent();
        assertThat(notices.get(0).getNoticeNo()).isEqualTo(90);
        assertThat(notices.get(9).getNoticeNo()).isEqualTo(81);
    }
}