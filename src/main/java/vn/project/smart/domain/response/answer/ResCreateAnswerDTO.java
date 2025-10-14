package vn.project.smart.domain.response.answer;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResCreateAnswerDTO {
    private long id;
    private String description;
    private boolean correct_answer;
    private int score;
    private Instant createdAt;
    private String createdBy;
}
