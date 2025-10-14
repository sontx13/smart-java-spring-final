package vn.project.smart.domain.response.hotline;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateAppHotlineDTO {
    private long id;
    private String name;
    private String icon;
    private String phone_number;
    private int type;
    private boolean active;
    private int sort;
    private String description;

    private Instant updatedAt;
    private String updatedBy;
}
