package vn.project.smart.domain.response.answer;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateAnswerDTO {
    private long id;
    private String description;
    private boolean correct_answer;
    private int score;
    private Instant updatedAt;
    private String updatedBy;
}
