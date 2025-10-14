package vn.project.smart.domain.response.exam;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResCreateExamDTO {
    private long id;
    private String name;
    private int time_minutes;
    private int total_score;
    private int total_question;
    private String description;
    private int level;
    private String logo;

    private boolean active;

    private Instant createdAt;
    private String createdBy;
}
