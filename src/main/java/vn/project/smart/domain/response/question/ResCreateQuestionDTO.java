package vn.project.smart.domain.response.question;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResCreateQuestionDTO {
    private long id;
    private int type;
    private int total_answers;
    private int score;
    private String description;
    private String image;
    private boolean active;

    private Instant createdAt;
    private String createdBy;
}
