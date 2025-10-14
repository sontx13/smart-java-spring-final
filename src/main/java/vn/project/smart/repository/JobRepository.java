package vn.project.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.project.smart.domain.Job;
import vn.project.smart.domain.Skill;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>,
                JpaSpecificationExecutor<Job> {

        List<Job> findBySkillsIn(List<Skill> skills);
}
