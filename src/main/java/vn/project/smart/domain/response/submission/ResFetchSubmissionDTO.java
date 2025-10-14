package vn.project.smart.domain.response.submission;

import java.time.Instant;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.project.smart.util.constant.ResumeStateEnum;

@Getter
@Setter
public class ResFetchSubmissionDTO {
    private long id;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    private UserSubmission user;
    private CompanySubmission company;
    private ExamSubmission exam;
    private QuestionSubmission question;
    private AnswerSubmission answer;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserSubmission {
        private long id;
        private String name;
        private String address;
        private int age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CompanySubmission {
        private long id;
        private String name;
        private String description;
        private String logo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ExamSubmission {
        private long id;
        private String name;
        private String logo;
        private boolean active;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class QuestionSubmission {
        private long id;
        private String description;
        private String image;
        private boolean active;
        private int type;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AnswerSubmission {
        private long id;
        private String description;
        private boolean correct_answer;
    }
}
