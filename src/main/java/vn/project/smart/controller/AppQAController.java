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
import vn.project.smart.domain.AppQA;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.qa.ResCreateAppQADTO;
import vn.project.smart.domain.response.qa.ResUpdateAppQADTO;
import vn.project.smart.service.AppQAService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppQAController {

    private final AppQAService qaService;

    public AppQAController(AppQAService qaService) {
        this.qaService = qaService;
    }

    @PostMapping("/qas")
    @ApiMessage("Create a qa")
    public ResponseEntity<ResCreateAppQADTO> create(@Valid @RequestBody AppQA qa) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.qaService.create(qa));
    }

    @PutMapping("/qas")
    @ApiMessage("Update a qa")
    public ResponseEntity<ResUpdateAppQADTO> update(@Valid @RequestBody AppQA qa)
            throws IdInvalidException {
        Optional<AppQA> currentAppQA = this.qaService.fetchAppQAById(qa.getId());
        if (!currentAppQA.isPresent()) {
            throw new IdInvalidException("AppQA not found");
        }

        return ResponseEntity.ok()
                .body(this.qaService.update(qa, currentAppQA.get()));
    }

    @DeleteMapping("/qas/{id}")
    @ApiMessage("Delete a qa by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppQA> currentAppQA = this.qaService.fetchAppQAById(id);
        if (!currentAppQA.isPresent()) {
            throw new IdInvalidException("AppQA not found");
        }
        this.qaService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/qas/{id}")
    @ApiMessage("Get a qa by id")
    public ResponseEntity<AppQA> getAppQA(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppQA> currentAppQA = this.qaService.fetchAppQAById(id);
        if (!currentAppQA.isPresent()) {
            throw new IdInvalidException("AppQA not found");
        }

        return ResponseEntity.ok().body(currentAppQA.get());
    }

    @GetMapping("/qas")
    @ApiMessage("Get qa with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppQA(
            @Filter Specification<AppQA> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.qaService.fetchAll(spec, pageable));
    }
}
