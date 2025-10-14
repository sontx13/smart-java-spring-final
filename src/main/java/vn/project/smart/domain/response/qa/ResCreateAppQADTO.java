package vn.project.smart.domain.response.qa;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateAppQADTO {
    private long id;
    private String name_q;
    private String email_q;
    private String phone_q;
    private String content_q;
    private Instant time_q;
    private String name_a;
    private Instant time_a;
    private String content_a;
    private boolean active;

    private Instant createdAt;
    private String createdBy;
}
