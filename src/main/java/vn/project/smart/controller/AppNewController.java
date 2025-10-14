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
import vn.project.smart.domain.AppNew;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.appnew.ResCreateAppNewDTO;
import vn.project.smart.domain.response.appnew.ResUpdateAppNewDTO;
import vn.project.smart.service.AppNewService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppNewController {

    private final AppNewService appnewService;

    public AppNewController(AppNewService appnewService) {
        this.appnewService = appnewService;
    }

    @PostMapping("/news")
    @ApiMessage("Create a new")
    public ResponseEntity<ResCreateAppNewDTO> create(@Valid @RequestBody AppNew appnew) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.appnewService.create(appnew));
    }

    @PutMapping("/news")
    @ApiMessage("Update a new")
    public ResponseEntity<ResUpdateAppNewDTO> update(@Valid @RequestBody AppNew appnew)
            throws IdInvalidException {
        Optional<AppNew> currentAppNew = this.appnewService.fetchAppNewById(appnew.getId());
        if (!currentAppNew.isPresent()) {
            throw new IdInvalidException("AppNew not found");
        }

        return ResponseEntity.ok()
                .body(this.appnewService.update(appnew, currentAppNew.get()));
    }

    @DeleteMapping("/news/{id}")
    @ApiMessage("Delete a new by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppNew> currentAppNew = this.appnewService.fetchAppNewById(id);
        if (!currentAppNew.isPresent()) {
            throw new IdInvalidException("AppNew not found");
        }
        this.appnewService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/news/{id}")
    @ApiMessage("Get a new by id")
    public ResponseEntity<AppNew> getAppNew(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppNew> currentAppNew = this.appnewService.fetchAppNewById(id);
        if (!currentAppNew.isPresent()) {
            throw new IdInvalidException("AppNew not found");
        }

        return ResponseEntity.ok().body(currentAppNew.get());
    }

    @GetMapping("/news")
    @ApiMessage("Get new with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppNew(
            @Filter Specification<AppNew> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.appnewService.fetchAll(spec, pageable));
    }
}
