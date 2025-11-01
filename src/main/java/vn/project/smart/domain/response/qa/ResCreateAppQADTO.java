package vn.project.smart.domain.response.qa;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateAppQADTO {
    private long id;
    private String nameQ;
    private String emailQ;
    private String phoneQ;
    private String contentQ;
    private Instant timeQ;
    private String nameA;
    private Instant timeA;
    private String contentA;
    private boolean active;

    private Instant createdAt;
    private String createdBy;
}
