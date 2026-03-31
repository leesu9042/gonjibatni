package com.example.gongjibatni.notice.controller;

import com.example.gongjibatni.notice.domain.Notice;
import com.example.gongjibatni.notice.service.AcademicNoticeService;
import org.springframework.data.domain.Window;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notices/academic")
public class AcademicNoticeController {

    private final AcademicNoticeService academicNoticeService;

    public AcademicNoticeController(AcademicNoticeService academicNoticeService) {
        this.academicNoticeService = academicNoticeService;
    }

    // 첫 페이지 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getFirstWindow() {
        Window<Notice> window = academicNoticeService.getFirstWindow();

        //이부분이 이해가 안가네
        Map<String, Object> response = new HashMap<>();
        response.put("content", window.getContent());
        response.put("hasNext", window.hasNext());


        List<Notice> notices = window.getContent();

        // 다음 페이지 요청용 cursor 정보
        if (!notices.isEmpty()) {
            Notice lastNotice = notices.get(notices.size() - 1);
            response.put("lastNoticeId", lastNotice.getNoticeId());
            response.put("lastNoticeNo", lastNotice.getNoticeNo());
        }


        return ResponseEntity.ok(response);
    }

    // 다음 페이지 조회
    @GetMapping("/next")
    public ResponseEntity<Map<String, Object>> getNextWindow(
            @RequestParam Long lastNoticeId,
            @RequestParam Integer lastNoticeNo
    ) {
        Window<Notice> window = academicNoticeService.getNextWindow(lastNoticeId, lastNoticeNo);

        Map<String, Object> response = new HashMap<>();
        response.put("content", window.getContent());
        response.put("hasNext", window.hasNext());

        if (!window.getContent().isEmpty()) {
            Notice lastNotice = window.getContent().get(window.getContent().size() - 1);
            response.put("lastNoticeId", lastNotice.getNoticeId());
            response.put("lastNoticeNo", lastNotice.getNoticeNo());
        }

        return ResponseEntity.ok(response);
    }
}
