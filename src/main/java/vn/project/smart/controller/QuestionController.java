package vn.project.smart.controller;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.project.smart.domain.Question;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.question.ResCreateQuestionDTO;
import vn.project.smart.domain.response.question.ResUpdateQuestionDTO;
import vn.project.smart.service.QuestionService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/questions")
    @ApiMessage("Create a question")
    public ResponseEntity<ResCreateQuestionDTO> create(@Valid @RequestBody Question question) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.questionService.create(question));
    }

    @PutMapping("/questions")
    @ApiMessage("Update a question")
    public ResponseEntity<ResUpdateQuestionDTO> update(@Valid @RequestBody Question question) throws IdInvalidException {
        Optional<Question> currentQuestion = this.questionService.fetchQuestionById(question.getId());
        if (!currentQuestion.isPresent()) {
            throw new IdInvalidException("Question not found");
        }

        return ResponseEntity.ok()
                .body(this.questionService.update(question, currentQuestion.get()));
    }

    @DeleteMapping("/questions/{id}")
    @ApiMessage("Delete a question by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Question> currentQuestion = this.questionService.fetchQuestionById(id);
        if (!currentQuestion.isPresent()) {
            throw new IdInvalidException("Question not found");
        }
        this.questionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/questions/{id}")
    @ApiMessage("Get a question by id")
    public ResponseEntity<Question> getQuestion(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Question> currentQuestion = this.questionService.fetchQuestionById(id);
        if (!currentQuestion.isPresent()) {
            throw new IdInvalidException("Question not found");
        }

        return ResponseEntity.ok().body(currentQuestion.get());
    }

    @GetMapping("/questions")
    @ApiMessage("Get question with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllQuestion(
            @Filter Specification<Question> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.questionService.fetchAll(spec, pageable));
    }
}
