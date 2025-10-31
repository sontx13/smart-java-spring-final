package vn.project.smart.domain.response;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateSyncArticleDTO {
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

}
