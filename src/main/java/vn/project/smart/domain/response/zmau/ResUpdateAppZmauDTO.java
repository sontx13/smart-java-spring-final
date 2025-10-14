package vn.project.smart.domain.response.zmau;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateAppZmauDTO {
    private long id;
    private String name;
    private String avatar;
    private String phone_number;
    private String zid;

    private Instant updatedAt;
    private String updatedBy;
}
