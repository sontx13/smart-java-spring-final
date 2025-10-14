package vn.project.smart.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import vn.project.smart.domain.Company;
import vn.project.smart.domain.Exam;
import vn.project.smart.domain.Answer;
import vn.project.smart.domain.Submission;
import vn.project.smart.domain.User;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.submission.ResCreateSubmissionDTO;
import vn.project.smart.domain.response.submission.ResFetchSubmissionDTO;
import vn.project.smart.domain.response.submission.ResUpdateSubmissionDTO;
import vn.project.smart.service.SubmissionService;
import vn.project.smart.service.UserService;
import vn.project.smart.util.SecurityUtil;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final UserService userService;

    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public SubmissionController(
            SubmissionService submissionService,
            UserService userService,
            FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.submissionService = submissionService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/submissions")
    @ApiMessage("Create a submission")
    public ResponseEntity<ResCreateSubmissionDTO> create(@Valid @RequestBody Submission submission) throws IdInvalidException {
        // check id exists
        boolean isIdExist = this.submissionService.checkSubmissionExistByUserAndAnswer(submission);
        if (!isIdExist) {
            throw new IdInvalidException("User id/Answer id không tồn tại");
        }

        // create new submission
        return ResponseEntity.status(HttpStatus.CREATED).body(this.submissionService.create(submission));
    }

    @PutMapping("/submissions")
    @ApiMessage("Update a submission")
    public ResponseEntity<ResUpdateSubmissionDTO> update(@RequestBody Submission submission) throws IdInvalidException {
        // check id exist
        Optional<Submission> reqSubmissionOptional = this.submissionService.fetchById(submission.getId());
        if (reqSubmissionOptional.isEmpty()) {
            throw new IdInvalidException("Submission với id = " + submission.getId() + " không tồn tại");
        }

        Submission reqSubmission = reqSubmissionOptional.get();

        return ResponseEntity.ok().body(this.submissionService.update(reqSubmission));
    }

    @DeleteMapping("/submissions/{id}")
    @ApiMessage("Delete a submission by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Submission> reqSubmissionOptional = this.submissionService.fetchById(id);
        if (reqSubmissionOptional.isEmpty()) {
            throw new IdInvalidException("Submission với id = " + id + " không tồn tại");
        }

        this.submissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/submissions/{id}")
    @ApiMessage("Fetch a submission by id")
    public ResponseEntity<ResFetchSubmissionDTO> fetchById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Submission> reqSubmissionOptional = this.submissionService.fetchById(id);
        if (reqSubmissionOptional.isEmpty()) {
            throw new IdInvalidException("Submission với id = " + id + " không tồn tại");
        }

        return ResponseEntity.ok().body(this.submissionService.getSubmission(reqSubmissionOptional.get()));
    }

    // @GetMapping("/submissions")
    // @ApiMessage("Fetch all submission with paginate")
    // public ResponseEntity<ResultPaginationDTO> fetchAll(
    //         @Filter Specification<Submission> spec,
    //         Pageable pageable) {

    //     List<Long> arrAnswerIds = null;
    //     String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
    //             ? SecurityUtil.getCurrentUserLogin().get()
    //             : "";
    //     User currentUser = this.userService.handleGetUserByUsername(email);
    //     if (currentUser != null) {
    //         Company userCompany = currentUser.getCompany();
    //         List<Exam> userCompanyExam = userCompany.getExams();

    //         if (userCompany != null) {
    //             List<Answer> companyAnswers = userCompany.getAnswers();
    //             if (companyAnswers != null && companyAnswers.size() > 0) {
    //                 arrAnswerIds = companyAnswers.stream().map(x -> x.getId())
    //                         .collect(Collectors.toList());
    //             }
    //         }
    //     }

    //     Specification<Submission> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
    //             .in(filterBuilder.input(arrAnswerIds)).get());

    //     Specification<Submission> finalSpec = jobInSpec.and(spec);

    //     return ResponseEntity.ok().body(this.submissionService.fetchAllSubmission(finalSpec, pageable));
    // }

    @PostMapping("/submissions/by-user")
    @ApiMessage("Get list submissions by user")
    public ResponseEntity<ResultPaginationDTO> fetchSubmissionByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.submissionService.fetchSubmissionByUser(pageable));
    }
}
