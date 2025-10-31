package vn.project.smart.domain;

import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "sync_articles")
@Entity
@Getter
@Setter
public class SyncArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String idArticle;

    private String title;

    private String titleCut;

    private String imageUrl;

    private String summary;

    private String content;

    private String createdDate;

    private String urlDetail;

    private String source;

    private String author;

    private int viewCount;

    private String cateName;

    private long cateId;

    private String isNew;

    private String strucId;

    private String otherProps;

    private Instant timeSync;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private AppCategory category;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @PrePersist
    public void handleBeforeCreate() {
        this.timeSync = Instant.now();
    }

}
