package com.example.gongjibatni.notice.domain;

import com.example.gongjibatni.classifier.Tag.NoticeTag;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(
        indexes = {
                @Index(name = "idx_notice_notice_no", columnList = "noticeNo")
        }
) //인덱스 생성
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;  // PK 공지사항 id

    @Column(unique = true)
    private Integer noticeNo;
    private String title;// 제목
    private String writer;     // 작성자
    private LocalDate createdAt;


    // nttid 로 바꿔야됨
    private String nttid;       // 상세보기 링크

    @Column(name = "category_keyword")
    private String categoryKeyword;


    // 태그들 (ElementCollection 사용)
    @ElementCollection(fetch = FetchType.LAZY) //값 타입 컬렉션을 별도 테이블에 매핑해서 저장한다
    @CollectionTable(
            name = "notice_tags",
            joinColumns = @JoinColumn(name = "notice_id")
    )
    @Enumerated(EnumType.STRING)//Enum (db에저장시 String)
    @Column(name = "tag")
    private Set<NoticeTag> tags = new HashSet<>();



    public Notice() {



    }



    public Integer getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(Integer noticeNo) {
        this.noticeNo = noticeNo;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdDate) {
        this.createdAt = createdDate;
    }

    public String getCategoryKeyword() {
        return categoryKeyword;
    }

    public void setCategoryKeyword(String categoryKeyword) {
        this.categoryKeyword = categoryKeyword;
    }



    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }


    public String getNttid() {
        return nttid;
    }

    public void setNttid(String nttid) {
        this.nttid = nttid;
    }


    public Set<NoticeTag> getTags() {
        return tags;
    }

}
