package vn.project.smart.domain.response;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateSyncArticleDTO {
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

}
