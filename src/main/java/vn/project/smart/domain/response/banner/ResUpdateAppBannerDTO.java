package vn.project.smart.domain.response.banner;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateAppBannerDTO {
    private long id;
    private String name;
    private String image;
    private String url;
    private int type;
    private boolean active;
    private int sort;
    private String description;

    private Instant updatedAt;
    private String updatedBy;
}
