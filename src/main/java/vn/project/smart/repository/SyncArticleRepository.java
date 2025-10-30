package vn.project.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.project.smart.domain.SyncArticle;

@Repository
public interface SyncArticleRepository extends JpaRepository<SyncArticle, Long>,
        JpaSpecificationExecutor<SyncArticle> {

    @Query("SELECT s FROM SyncArticle s WHERE s.id_article = :id_article")
    SyncArticle findByIdArticle(@Param("id_article") String id_article);

}
