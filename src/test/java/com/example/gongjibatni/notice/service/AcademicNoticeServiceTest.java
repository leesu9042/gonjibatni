package com.example.gongjibatni.notice.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Window;
import org.springframework.data.support.WindowIterator;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Spring 환경 로드 및 H2 DB 사용
@SpringBootTest
@Transactional // 테스트 후 자동 롤백
@TestPropertySource(properties = {
        "spring.task.scheduling.enabled=false" // 스케줄링 Bean 로드 자체를 건너뜁니다.
})
class AcademicNoticeServiceIntegrationTest {


    @BeforeEach
    void cleanUp() {
        noticeRepository.deleteAll();
    }
    @Autowired
    private AcademicNoticeService academicNoticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    // --- 헬퍼 함수 ---
    private Notice createAndSaveNotice(int no) {
        Notice notice = new Notice();
        notice.setNoticeNo(no);
        notice.setTitle("Test Title " + no);
        notice.setWriter("테스트");
        notice.setCreatedAt(LocalDate.now());
        notice.setNttid(String.valueOf(no));
        return noticeRepository.save(notice);
    }
    // ------------------


    @Test
    @DisplayName("DB에 데이터가 있을 경우, 첫 번째 Window에 데이터가 존재해야 한다")
    void getNoticeFirstWindow_ShouldReturnContentIfDataExists() {
        // 1. Given (준비: 테스트용 데이터 3개 저장)

        createAndSaveNotice(103);
        createAndSaveNotice(102);
        createAndSaveNotice(101); // 3개의 공지  완료

        // 2. When (실행)
        WindowIterator<Notice> iterator = academicNoticeService.getNoticeFirstwindow();

        // 3. Then (검증)

        // 1) Iterator가 다음 Window를 가지고 있어야 함 (데이터가 존재하므로)
        assertThat(iterator.hasNext()).isTrue();


        // 3) 가져온 Window의 내용(List)이 비어있지 않은지 확인

        List<Notice> notices = academicNoticeService.iteratorToList(iterator);


        // 4) 최소한 저장한 개수만큼(3개) 포함하고 있는지 확인 (findFirst10이므로 3개를 모두 가져옴)
        assertThat(notices).hasSize(3);
    }
}