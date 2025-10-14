package vn.project.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.project.smart.domain.AppQA;

@Repository
public interface AppQARepository extends JpaRepository<AppQA, Long>,
        JpaSpecificationExecutor<AppQA> {

}
