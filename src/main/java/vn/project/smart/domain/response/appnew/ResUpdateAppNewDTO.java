package vn.project.smart.domain.response.appnew;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateAppNewDTO {
    private long id;
    private String title;
    private String logo;
    private String description;
    private String content;
    private int type;
    private boolean active;
    private String url;
    private Instant public_at;

    private Instant updatedAt;
    private String updatedBy;
}
