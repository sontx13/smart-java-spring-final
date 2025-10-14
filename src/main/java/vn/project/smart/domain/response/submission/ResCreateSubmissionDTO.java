package vn.project.smart.domain.response.submission;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateSubmissionDTO {
    private long id;
    private int score;
    private Instant createdAt;
    private String createdBy;
}
