package com.example.gongjibatni.notice.service;

// 파일: NoticeUtils.java
public class NoticeUtils {

    /**
     * nttid 값을 이용해 공지사항 상세 페이지 URL을 생성합니다.
     * @param nttid 외부 시스템에서 받은 공지 ID 문자열
     * @return 완성된 공지사항 URL (예: bbs/BBSMSTR_000000000050/view.do)
     */

    public static String nttidToUrl(String nttid) {
        if (nttid == null || nttid.isEmpty()) {
            return "";
        }

        // 기본 URL — 실제 사용하는 시스템 주소에 맞게 수정하세요
        String baseUrl = "https://hanbat.ac.kr/bbs/BBSMSTR_000000000050/view.do?nttId=";
        // 얘도 enum으로 뺍시다

        // bbs/BBSMSTR_000000000050/view.do -> action

        return baseUrl + nttid;
    }

}

