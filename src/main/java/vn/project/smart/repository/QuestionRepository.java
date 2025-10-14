package vn.project.smart.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.project.smart.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>,
                JpaSpecificationExecutor<Question> {

}
