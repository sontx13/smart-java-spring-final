package vn.project.smart.domain.response.submission;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateSubmissionDTO {
    private Instant updatedAt;
    private String updatedBy;
}
