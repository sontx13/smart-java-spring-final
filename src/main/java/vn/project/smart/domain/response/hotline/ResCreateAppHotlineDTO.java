package vn.project.smart.domain.response.hotline;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateAppHotlineDTO {
    private long id;
    private String name;
    private String icon;
    private String phone_number;
    private int type;
    private boolean active;
    private int sort;
    private String description;

    private Instant createdAt;
    private String createdBy;
}
