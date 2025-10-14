package vn.project.smart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.project.smart.domain.Answer;
import vn.project.smart.domain.Answer;
import vn.project.smart.domain.Submission;
import vn.project.smart.domain.User;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.submission.ResCreateSubmissionDTO;
import vn.project.smart.domain.response.submission.ResFetchSubmissionDTO;
import vn.project.smart.domain.response.submission.ResUpdateSubmissionDTO;
import vn.project.smart.repository.AnswerRepository;
import vn.project.smart.repository.SubmissionRepository;
import vn.project.smart.repository.UserRepository;
import vn.project.smart.util.SecurityUtil;

@Service
public class SubmissionService {
    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            UserRepository userRepository,
            AnswerRepository answerRepository) {
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public Optional<Submission> fetchById(long id) {
        return this.submissionRepository.findById(id);
    }

    public boolean checkSubmissionExistByUserAndAnswer(Submission submission) {
        // check user by id
        if (submission.getUser() == null)
            return false;
        Optional<User> userOptional = this.userRepository.findById(submission.getUser().getId());
        if (userOptional.isEmpty())
            return false;

        // check answer by id
        if (submission.getAnswer() == null)
            return false;
        Optional<Answer> answerOptional = this.answerRepository.findById(submission.getAnswer().getId());
        if (answerOptional.isEmpty())
            return false;

        return true;
    }

    public ResCreateSubmissionDTO create(Submission submission) {
        submission = this.submissionRepository.save(submission);

        ResCreateSubmissionDTO res = new ResCreateSubmissionDTO();
        res.setId(submission.getId());
        res.setCreatedBy(submission.getCreatedBy());
        res.setCreatedAt(submission.getCreatedAt());

        return res;
    }

    public ResUpdateSubmissionDTO update(Submission submission) {
        submission = this.submissionRepository.save(submission);
        ResUpdateSubmissionDTO res = new ResUpdateSubmissionDTO();
        res.setUpdatedAt(submission.getUpdatedAt());
        res.setUpdatedBy(submission.getUpdatedBy());
        return res;
    }

    public void delete(long id) {
        this.submissionRepository.deleteById(id);
    }

    public ResFetchSubmissionDTO getSubmission(Submission submission) {
        ResFetchSubmissionDTO res = new ResFetchSubmissionDTO();
        res.setId(submission.getId());
        res.setCreatedAt(submission.getCreatedAt());
        res.setCreatedBy(submission.getCreatedBy());
        res.setUpdatedAt(submission.getUpdatedAt());
        res.setUpdatedBy(submission.getUpdatedBy());

        
        res.setUser(new ResFetchSubmissionDTO.UserSubmission(submission.getUser().getId(), submission.getUser().getName(), submission.getUser().getAddress(),
                submission.getUser().getAge()));
        res.setCompany(new ResFetchSubmissionDTO.CompanySubmission(submission.getCompany().getId(),
                submission.getCompany().getName(), submission.getCompany().getDescription(),
                submission.getCompany().getLogo()));
        res.setExam(
                new ResFetchSubmissionDTO.ExamSubmission(submission.getExam().getId(), submission.getExam().getName(),
                        submission.getExam().getLogo(), submission.getExam().isActive()));
        res.setQuestion(
                new ResFetchSubmissionDTO.QuestionSubmission(submission.getQuestion().getId(), submission.getQuestion().getDescription(),
                        submission.getQuestion().getImage(), submission.getQuestion().isActive(),
                        submission.getQuestion().getType()));
        res.setAnswer(new ResFetchSubmissionDTO.AnswerSubmission(submission.getAnswer().getId(), submission.getAnswer().getDescription(),
                submission.getAnswer().isCorrect_answer()));

        return res;
    }

    public ResultPaginationDTO fetchAllSubmission(Specification<Submission> spec, Pageable pageable) {
        Page<Submission> pageUser = this.submissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchSubmissionDTO> listSubmission = pageUser.getContent()
                .stream().map(item -> this.getSubmission(item))
                .collect(Collectors.toList());

        rs.setResult(listSubmission);

        return rs;
    }

    public ResultPaginationDTO fetchSubmissionByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Submission> spec = filterSpecificationConverter.convert(node);
        Page<Submission> pageSubmission = this.submissionRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSubmission.getTotalPages());
        mt.setTotal(pageSubmission.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchSubmissionDTO> listSubmission = pageSubmission.getContent()
                .stream().map(item -> this.getSubmission(item))
                .collect(Collectors.toList());

        rs.setResult(listSubmission);

        return rs;
    }
}
