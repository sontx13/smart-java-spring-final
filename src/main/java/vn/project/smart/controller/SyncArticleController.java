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
import vn.project.smart.domain.SyncArticle;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.ResCreateSyncArticleDTO;
import vn.project.smart.service.SyncArticleService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SyncArticleController {

    private final SyncArticleService articleService;

    public SyncArticleController(SyncArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    @ApiMessage("Create a article")
    public ResponseEntity<ResCreateSyncArticleDTO> create(@Valid @RequestBody SyncArticle article) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.articleService.create(article));
    }

    @GetMapping("/articles/{id}")
    @ApiMessage("Get a article by id")
    public ResponseEntity<SyncArticle> getSyncArticle(@PathVariable("id") long id) throws IdInvalidException {
        Optional<SyncArticle> currentSyncArticle = this.articleService.fetchSyncArticleById(id);
        if (!currentSyncArticle.isPresent()) {
            throw new IdInvalidException("SyncArticle not found");
        }

        return ResponseEntity.ok().body(currentSyncArticle.get());
    }

    @GetMapping("/articles")
    @ApiMessage("Get article with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllSyncArticle(
            @Filter Specification<SyncArticle> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.articleService.fetchAll(spec, pageable));
    }
}
