package vn.project.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.project.smart.domain.AppConfig;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long>,
                JpaSpecificationExecutor<AppConfig> {

}
