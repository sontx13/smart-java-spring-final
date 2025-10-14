package vn.project.smart.domain.response.category;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateAppCategoryDTO {
    private long id;
    private String name;
    private String icon;
    private String url;
    private int type;
    private boolean active;
    private int sort;
    private String description;

    private Instant createdAt;
    private String createdBy;
}
