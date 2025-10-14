package vn.project.smart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.Question;
import vn.project.smart.domain.Answer;
import vn.project.smart.domain.Skill;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.answer.ResCreateAnswerDTO;
import vn.project.smart.domain.response.answer.ResUpdateAnswerDTO;
import vn.project.smart.repository.QuestionRepository;
import vn.project.smart.repository.AnswerRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository examRepository;

    public AnswerService(AnswerRepository answerRepository,
            SkillRepository skillRepository,
            QuestionRepository examRepository) {
        this.answerRepository = answerRepository;
        this.examRepository = examRepository;
    }

    public Optional<Answer> fetchAnswerById(long id) {
        return this.answerRepository.findById(id);
    }

    public ResCreateAnswerDTO create(Answer j) {

        // check exam
        if (j.getQuestion() != null) {
            Optional<Question> cOptional = this.examRepository.findById(j.getQuestion().getId());
            if (cOptional.isPresent()) {
                j.setQuestion(cOptional.get());
            }
        }

        // create answer
        Answer currentAnswer = this.answerRepository.save(j);

        // convert response
        ResCreateAnswerDTO dto = new ResCreateAnswerDTO();
        dto.setId(currentAnswer.getId());
        dto.setDescription(currentAnswer.getDescription());
        dto.setCorrect_answer(currentAnswer.isCorrect_answer());
        dto.setCreatedAt(currentAnswer.getCreatedAt());
        dto.setCreatedBy(currentAnswer.getCreatedBy());
        dto.setScore(currentAnswer.getScore());
        return dto;
    }

    public ResUpdateAnswerDTO update(Answer j, Answer answerInDB) {

        // check exam
        if (j.getQuestion() != null) {
            Optional<Question> cOptional = this.examRepository.findById(j.getQuestion().getId());
            if (cOptional.isPresent()) {
                answerInDB.setQuestion(cOptional.get());
            }
        }

        // update correct info
        answerInDB.setDescription(j.getDescription());
        answerInDB.setCorrect_answer(j.isCorrect_answer());

        // update answer
        Answer currentAnswer = this.answerRepository.save(answerInDB);

        // convert response
        ResUpdateAnswerDTO dto = new ResUpdateAnswerDTO();
        dto.setId(currentAnswer.getId());
        dto.setDescription(currentAnswer.getDescription());
        dto.setCorrect_answer(currentAnswer.isCorrect_answer());
        dto.setUpdatedAt(currentAnswer.getUpdatedAt());
        dto.setUpdatedBy(currentAnswer.getUpdatedBy());
        dto.setScore(currentAnswer.getScore());
        return dto;
    }

    public void delete(long id) {
        this.answerRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<Answer> spec, Pageable pageable) {
        Page<Answer> pageUser = this.answerRepository.findAll(spec, pageable);

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
