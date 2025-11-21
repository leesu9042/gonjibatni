package com.example.gongjibatni.crawling.service;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeSyncService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeSyncService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }


    /**
     * DB보다 더 최신 Notices만 추려내는 함수
     * 없으면 빈리스트 반환`
     */
    public List<Notice> filterNewAcademicNotices(List<Notice> noticeList){

        Optional<Notice> latestNotice = noticeRepository.findTopByOrderByNoticeNoDesc();

        //DB에 없을 수 있으니까
        int latestNo = (latestNotice.isPresent()) ? latestNotice.get().getNoticeNo() : -1;

        return noticeList.stream()
                .filter(notice -> notice.getNoticeNo() > latestNo)
                .toList(); // Java 16 이상

    }

}
