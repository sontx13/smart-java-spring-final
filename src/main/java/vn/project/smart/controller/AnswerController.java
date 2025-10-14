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
import vn.project.smart.domain.Answer;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.answer.ResCreateAnswerDTO;
import vn.project.smart.domain.response.answer.ResUpdateAnswerDTO;
import vn.project.smart.service.AnswerService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/answers")
    @ApiMessage("Create a answer")
    public ResponseEntity<ResCreateAnswerDTO> create(@Valid @RequestBody Answer answer) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.answerService.create(answer));
    }

    @PutMapping("/answers")
    @ApiMessage("Update a answer")
    public ResponseEntity<ResUpdateAnswerDTO> update(@Valid @RequestBody Answer answer) throws IdInvalidException {
        Optional<Answer> currentAnswer = this.answerService.fetchAnswerById(answer.getId());
        if (!currentAnswer.isPresent()) {
            throw new IdInvalidException("Answer not found");
        }

        return ResponseEntity.ok()
                .body(this.answerService.update(answer, currentAnswer.get()));
    }

    @DeleteMapping("/answers/{id}")
    @ApiMessage("Delete a answer by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Answer> currentAnswer = this.answerService.fetchAnswerById(id);
        if (!currentAnswer.isPresent()) {
            throw new IdInvalidException("Answer not found");
        }
        this.answerService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/answers/{id}")
    @ApiMessage("Get a answer by id")
    public ResponseEntity<Answer> getAnswer(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Answer> currentAnswer = this.answerService.fetchAnswerById(id);
        if (!currentAnswer.isPresent()) {
            throw new IdInvalidException("Answer not found");
        }

        return ResponseEntity.ok().body(currentAnswer.get());
    }

    @GetMapping("/answers")
    @ApiMessage("Get answer with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAnswer(
            @Filter Specification<Answer> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.answerService.fetchAll(spec, pageable));
    }
}
