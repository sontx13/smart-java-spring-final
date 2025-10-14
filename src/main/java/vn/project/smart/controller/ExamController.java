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
import vn.project.smart.domain.Exam;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.exam.ResCreateExamDTO;
import vn.project.smart.domain.response.exam.ResUpdateExamDTO;
import vn.project.smart.service.ExamService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/exams")
    @ApiMessage("Create a exam")
    public ResponseEntity<ResCreateExamDTO> create(@Valid @RequestBody Exam exam) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.examService.create(exam));
    }

    @PutMapping("/exams")
    @ApiMessage("Update a exam")
    public ResponseEntity<ResUpdateExamDTO> update(@Valid @RequestBody Exam exam) throws IdInvalidException {
        Optional<Exam> currentExam = this.examService.fetchExamById(exam.getId());
        if (!currentExam.isPresent()) {
            throw new IdInvalidException("Exam not found");
        }

        return ResponseEntity.ok()
                .body(this.examService.update(exam, currentExam.get()));
    }

    @DeleteMapping("/exams/{id}")
    @ApiMessage("Delete a exam by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Exam> currentExam = this.examService.fetchExamById(id);
        if (!currentExam.isPresent()) {
            throw new IdInvalidException("Exam not found");
        }
        this.examService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/exams/{id}")
    @ApiMessage("Get a exam by id")
    public ResponseEntity<Exam> getExam(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Exam> currentExam = this.examService.fetchExamById(id);
        if (!currentExam.isPresent()) {
            throw new IdInvalidException("Exam not found");
        }

        return ResponseEntity.ok().body(currentExam.get());
    }

    @GetMapping("/exams")
    @ApiMessage("Get exam with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllExam(
            @Filter Specification<Exam> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.examService.fetchAll(spec, pageable));
    }
}
