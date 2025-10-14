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
import vn.project.smart.domain.AppCategory;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.category.ResCreateAppCategoryDTO;
import vn.project.smart.domain.response.category.ResUpdateAppCategoryDTO;
import vn.project.smart.service.AppCategoryService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppCategoryController {

    private final AppCategoryService categoryService;

    public AppCategoryController(AppCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage("Create a category")
    public ResponseEntity<ResCreateAppCategoryDTO> create(@Valid @RequestBody AppCategory category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.categoryService.create(category));
    }

    @PutMapping("/categories")
    @ApiMessage("Update a category")
    public ResponseEntity<ResUpdateAppCategoryDTO> update(@Valid @RequestBody AppCategory category)
            throws IdInvalidException {
        Optional<AppCategory> currentAppCategory = this.categoryService.fetchAppCategoryById(category.getId());
        if (!currentAppCategory.isPresent()) {
            throw new IdInvalidException("AppCategory not found");
        }

        return ResponseEntity.ok()
                .body(this.categoryService.update(category, currentAppCategory.get()));
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Delete a category by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppCategory> currentAppCategory = this.categoryService.fetchAppCategoryById(id);
        if (!currentAppCategory.isPresent()) {
            throw new IdInvalidException("AppCategory not found");
        }
        this.categoryService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Get a category by id")
    public ResponseEntity<AppCategory> getAppCategory(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppCategory> currentAppCategory = this.categoryService.fetchAppCategoryById(id);
        if (!currentAppCategory.isPresent()) {
            throw new IdInvalidException("AppCategory not found");
        }

        return ResponseEntity.ok().body(currentAppCategory.get());
    }

    @GetMapping("/categories")
    @ApiMessage("Get category with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppCategory(
            @Filter Specification<AppCategory> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.categoryService.fetchAll(spec, pageable));
    }
}
