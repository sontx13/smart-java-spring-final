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
import vn.project.smart.domain.AppHotline;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.hotline.ResCreateAppHotlineDTO;
import vn.project.smart.domain.response.hotline.ResUpdateAppHotlineDTO;
import vn.project.smart.service.AppHotlineService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppHotlineController {

    private final AppHotlineService hotlineService;

    public AppHotlineController(AppHotlineService hotlineService) {
        this.hotlineService = hotlineService;
    }

    @PostMapping("/hotlines")
    @ApiMessage("Create a hotline")
    public ResponseEntity<ResCreateAppHotlineDTO> create(@Valid @RequestBody AppHotline hotline) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.hotlineService.create(hotline));
    }

    @PutMapping("/hotlines")
    @ApiMessage("Update a hotline")
    public ResponseEntity<ResUpdateAppHotlineDTO> update(@Valid @RequestBody AppHotline hotline)
            throws IdInvalidException {
        Optional<AppHotline> currentAppHotline = this.hotlineService.fetchAppHotlineById(hotline.getId());
        if (!currentAppHotline.isPresent()) {
            throw new IdInvalidException("AppHotline not found");
        }

        return ResponseEntity.ok()
                .body(this.hotlineService.update(hotline, currentAppHotline.get()));
    }

    @DeleteMapping("/hotlines/{id}")
    @ApiMessage("Delete a hotline by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppHotline> currentAppHotline = this.hotlineService.fetchAppHotlineById(id);
        if (!currentAppHotline.isPresent()) {
            throw new IdInvalidException("AppHotline not found");
        }
        this.hotlineService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/hotlines/{id}")
    @ApiMessage("Get a hotline by id")
    public ResponseEntity<AppHotline> getAppHotline(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppHotline> currentAppHotline = this.hotlineService.fetchAppHotlineById(id);
        if (!currentAppHotline.isPresent()) {
            throw new IdInvalidException("AppHotline not found");
        }

        return ResponseEntity.ok().body(currentAppHotline.get());
    }

    @GetMapping("/hotlines")
    @ApiMessage("Get hotline with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppHotline(
            @Filter Specification<AppHotline> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.hotlineService.fetchAll(spec, pageable));
    }
}
