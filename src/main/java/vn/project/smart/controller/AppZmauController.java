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
import vn.project.smart.domain.AppZmau;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.zmau.ResCreateAppZmauDTO;
import vn.project.smart.domain.response.zmau.ResUpdateAppZmauDTO;
import vn.project.smart.service.AppZmauService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppZmauController {

    private final AppZmauService zmauService;

    public AppZmauController(AppZmauService zmauService) {
        this.zmauService = zmauService;
    }

    @PostMapping("/zmaus")
    @ApiMessage("Create a zmau")
    public ResponseEntity<ResCreateAppZmauDTO> create(@Valid @RequestBody AppZmau zmau) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.zmauService.create(zmau));
    }

    @PutMapping("/zmaus")
    @ApiMessage("Update a zmau")
    public ResponseEntity<ResUpdateAppZmauDTO> update(@Valid @RequestBody AppZmau zmau)
            throws IdInvalidException {
        Optional<AppZmau> currentAppZmau = this.zmauService.fetchAppZmauById(zmau.getId());
        if (!currentAppZmau.isPresent()) {
            throw new IdInvalidException("AppZmau not found");
        }

        return ResponseEntity.ok()
                .body(this.zmauService.update(zmau, currentAppZmau.get()));
    }

    @DeleteMapping("/zmaus/{id}")
    @ApiMessage("Delete a zmau by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppZmau> currentAppZmau = this.zmauService.fetchAppZmauById(id);
        if (!currentAppZmau.isPresent()) {
            throw new IdInvalidException("AppZmau not found");
        }
        this.zmauService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/zmaus/{id}")
    @ApiMessage("Get a zmau by id")
    public ResponseEntity<AppZmau> getAppZmau(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppZmau> currentAppZmau = this.zmauService.fetchAppZmauById(id);
        if (!currentAppZmau.isPresent()) {
            throw new IdInvalidException("AppZmau not found");
        }

        return ResponseEntity.ok().body(currentAppZmau.get());
    }

    @GetMapping("/zmaus")
    @ApiMessage("Get zmau with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppZmau(
            @Filter Specification<AppZmau> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.zmauService.fetchAll(spec, pageable));
    }
}
