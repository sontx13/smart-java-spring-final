package vn.project.smart.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.Exam;
import vn.project.smart.domain.Question;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.question.ResCreateQuestionDTO;
import vn.project.smart.domain.response.question.ResUpdateQuestionDTO;
import vn.project.smart.repository.ExamRepository;
import vn.project.smart.repository.QuestionRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    public QuestionService(QuestionRepository questionRepository,
            SkillRepository skillRepository,
            ExamRepository examRepository) {
        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
    }

    public Optional<Question> fetchQuestionById(long id) {
        return this.questionRepository.findById(id);
    }

    public ResCreateQuestionDTO create(Question j) {

        // check exam
        if (j.getExam() != null) {
            Optional<Exam> cOptional = this.examRepository.findById(j.getExam().getId());
            if (cOptional.isPresent()) {
                j.setExam(cOptional.get());
            }
        }

        // create question
        Question currentQuestion = this.questionRepository.save(j);

        // convert response
        ResCreateQuestionDTO dto = new ResCreateQuestionDTO();
        dto.setId(currentQuestion.getId());
        dto.setDescription(currentQuestion.getDescription());
        dto.setImage(currentQuestion.getImage());
        dto.setType(currentQuestion.getType());
        dto.setActive(currentQuestion.isActive());
        dto.setCreatedAt(currentQuestion.getCreatedAt());
        dto.setCreatedBy(currentQuestion.getCreatedBy());
        dto.setTotal_answers(currentQuestion.getTotal_answers());
        dto.setScore(currentQuestion.getScore());
       
        return dto;
    }

    public ResUpdateQuestionDTO update(Question j, Question questionInDB) {

        // check exam
        if (j.getExam() != null) {
            Optional<Exam> cOptional = this.examRepository.findById(j.getExam().getId());
            if (cOptional.isPresent()) {
                questionInDB.setExam(cOptional.get());
            }
        }

        // update correct info
        questionInDB.setDescription(j.getDescription());
        questionInDB.setImage(j.getImage());
        questionInDB.setType(j.getType());
   
        questionInDB.setActive(j.isActive());

        // update question
        Question currentQuestion = this.questionRepository.save(questionInDB);

        // convert response
        ResUpdateQuestionDTO dto = new ResUpdateQuestionDTO();
        dto.setId(currentQuestion.getId());
        dto.setDescription(currentQuestion.getDescription());
        dto.setImage(currentQuestion.getImage());
        dto.setType(currentQuestion.getType());
        dto.setActive(currentQuestion.isActive());
        dto.setUpdatedAt(currentQuestion.getUpdatedAt());
        dto.setUpdatedBy(currentQuestion.getUpdatedBy());
        dto.setTotal_answers(currentQuestion.getTotal_answers());
        dto.setScore(currentQuestion.getScore());
        return dto;
    }

    public void delete(long id) {
        this.questionRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<Question> spec, Pageable pageable) {
        Page<Question> pageUser = this.questionRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageUser.getContent());

        return rs;
    }
}
