package vn.project.smart.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.Company;
import vn.project.smart.domain.Exam;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.exam.ResCreateExamDTO;
import vn.project.smart.domain.response.exam.ResUpdateExamDTO;
import vn.project.smart.repository.CompanyRepository;
import vn.project.smart.repository.ExamRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final CompanyRepository companyRepository;

    public ExamService(ExamRepository examRepository,
            SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.examRepository = examRepository;
        this.companyRepository = companyRepository;
    }

    public Optional<Exam> fetchExamById(long id) {
        return this.examRepository.findById(id);
    }

    public ResCreateExamDTO create(Exam j) {

        // check company
        if (j.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(j.getCompany().getId());
            if (cOptional.isPresent()) {
                j.setCompany(cOptional.get());
            }
        }

        // create Exam
        Exam currentExam = this.examRepository.save(j);

        // convert response
        ResCreateExamDTO dto = new ResCreateExamDTO();
        dto.setId(currentExam.getId());
        dto.setName(currentExam.getName());
        dto.setDescription(currentExam.getDescription());
        dto.setLogo(currentExam.getLogo());
        dto.setLevel(currentExam.getLevel());
        dto.setActive(currentExam.isActive());
        dto.setTime_minutes(currentExam.getTime_minutes());
        dto.setTotal_question(currentExam.getTotal_question());
        dto.setTotal_score(currentExam.getTotal_score());
        dto.setCreatedAt(currentExam.getCreatedAt());
        dto.setCreatedBy(currentExam.getCreatedBy());

        return dto;
    }

    public ResUpdateExamDTO update(Exam j, Exam examInDB) {

        // check company
        if (j.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(j.getCompany().getId());
            if (cOptional.isPresent()) {
                examInDB.setCompany(cOptional.get());
            }
        }

        // update correct info
        examInDB.setName(j.getName());
        examInDB.setDescription(j.getDescription());
        examInDB.setLogo(j.getLogo());
        examInDB.setLevel(j.getLevel());
        examInDB.setActive(j.isActive());
        examInDB.setTime_minutes(j.getTime_minutes());
        examInDB.setTotal_question(j.getTotal_question());
        examInDB.setTotal_score(j.getTotal_score());

        // update exam
        Exam currentExam = this.examRepository.save(examInDB);

        // convert response
        ResUpdateExamDTO dto = new ResUpdateExamDTO();
        dto.setId(currentExam.getId());
        dto.setName(currentExam.getName());
        dto.setDescription(currentExam.getDescription());
        dto.setLogo(currentExam.getLogo());
        dto.setLevel(currentExam.getLevel());
        dto.setActive(currentExam.isActive());
        dto.setUpdatedAt(currentExam.getUpdatedAt());
        dto.setUpdatedBy(currentExam.getUpdatedBy());
        dto.setTime_minutes(currentExam.getTime_minutes());
        dto.setTotal_question(currentExam.getTotal_question());
        dto.setTotal_score(currentExam.getTotal_score());
        return dto;
    }

    public void delete(long id) {
        this.examRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<Exam> spec, Pageable pageable) {
        Page<Exam> pageUser = this.examRepository.findAll(spec, pageable);

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
