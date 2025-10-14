package vn.project.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.project.smart.domain.AppZmau;

@Repository
public interface AppZmauRepository extends JpaRepository<AppZmau, Long>,
        JpaSpecificationExecutor<AppZmau> {

}
