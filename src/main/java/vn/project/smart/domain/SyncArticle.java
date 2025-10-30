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

    private String id_article;

    private String title;

    private String title_cut;

    private String image_url;

    private String summary;

    private String content;

    private String created_date;

    private String url_detail;

    private String source;

    private String author;

    private int view_count;

    private String cate_name;

    private long cate_id;

    private String is_new;

    private String struc_id;

    private String other_props;

    private Instant time_sync;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private AppCategory category;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @PrePersist
    public void handleBeforeCreate() {
        this.time_sync = Instant.now();
    }

}
