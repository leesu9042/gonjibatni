package com.example.gongjibatni.crawling.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeSyncServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeSyncService noticeSyncService;


    // 테스트용 Notice 객체를 간결하게 생성하는 헬퍼 메서드
    private Notice createNotice(int no) {
        // 1. Notice 인스턴스 생성
        Notice notice = new Notice();
        // 2. 테스트의 핵심 값 설정: noticeNo
        notice.setNoticeNo(no);
        // 3. 나머지 필드에는 테스트에 영향을 주지 않는 일관된 기본값 설정
        // 제목: 테스트 목적으로 번호를 포함하여 명확하게 설정
        notice.setTitle("Test Title " + no);
        // 작성자: 기본값
        notice.setWriter("테스트 작성자");
        // 생성일: 필터링 로직에 영향이 없도록 고정된 날짜 사용 (예: 2024-01-01)
        notice.setCreatedAt(LocalDate.of(2024, 1, 1));
        // Nttid: 테스트 목적으로 ID를 문자열로 설정 (실제 로직에 맞게)
        notice.setNttid(String.valueOf(no));


        return notice;
    }

    @Test
    @DisplayName("DB보다 최신인 항목만 필터링되어 반환된다 (일부 통과)")
    void test_PartialFiltering() {
        // Given (준비): DB의 최신 항목은 No. 10이라고 가정
        when(noticeRepository.findTopByOrderByNoticeNoDesc())
                .thenReturn(Optional.of(createNotice(10)));

        // 입력: 8제외, 10 (), 12, 15 (통과)
        List<Notice> incomingNotices = Arrays.asList(
                createNotice(8), createNotice(10), createNotice(12), createNotice(15)
        );

        // When (실행)
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(incomingNotices);

        // Then (검증)
        // 결과는 12, 15 두 개만 포함해야 함
        assertThat(newNotices).hasSize(2);
        assertThat(newNotices)
                .extracting(Notice::getNoticeNo)
                .contains(12, 15);

        verify(noticeRepository, times(1)).findTopByOrderByNoticeNoDesc();
    }



    @Test
    @DisplayName("DB가 비어있을 경우, 입력 리스트 전체가 반환된다 (전체 통과)")
    void test_FullPass_DbEmpty() {
        // Given (준비): DB에 아무것도 없어 null 반환 (latestNo = -1이 됨)
        when(noticeRepository.findTopByOrderByNoticeNoDesc())
                .thenReturn(Optional.empty());

        // 입력: 1, 2, 3
        List<Notice> incomingNotices = Arrays.asList(
                createNotice(1), createNotice(2), createNotice(3)
        );

        // When (실행)
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(incomingNotices);

        // Then (검증)
        // 입력 리스트 전체가 반환되어야 함
        assertThat(newNotices).hasSize(3);
        assertThat(newNotices)
                .extracting(Notice::getNoticeNo)
                .contains(1, 2, 3);
    }

    @Test
    @DisplayName("입력 리스트가 모두 DB보다 오래된 경우 빈 리스트를 반환한다")
    void test_FullFiltered_ReturnEmptyList() {
        // Given (준비): DB 최신 공지 번호는 20번이라고 가정
        when(noticeRepository.findTopByOrderByNoticeNoDesc())
                .thenReturn(Optional.of(createNotice(20)));

        // 입력: 15, 18 (둘 다 20보다 작음)
        List<Notice> incomingNotices = Arrays.asList(
                createNotice(15), createNotice(18)
        );

        // When (실행)
        List<Notice> newNotices = noticeSyncService.filterNewAcademicNotices(incomingNotices);

        // Then (검증)
        // 결과는 비어있어야 함
        assertThat(newNotices).isEmpty();

        verify(noticeRepository, times(1)).findTopByOrderByNoticeNoDesc();
    }
}

