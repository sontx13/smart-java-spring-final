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
import vn.project.smart.domain.AppInfor;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.infor.ResCreateAppInforDTO;
import vn.project.smart.domain.response.infor.ResUpdateAppInforDTO;
import vn.project.smart.service.AppInforService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppInforController {

    private final AppInforService inforService;

    public AppInforController(AppInforService inforService) {
        this.inforService = inforService;
    }

    @PostMapping("/infors")
    @ApiMessage("Create a infor")
    public ResponseEntity<ResCreateAppInforDTO> create(@Valid @RequestBody AppInfor infor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.inforService.create(infor));
    }

    @PutMapping("/infors")
    @ApiMessage("Update a infor")
    public ResponseEntity<ResUpdateAppInforDTO> update(@Valid @RequestBody AppInfor infor)
            throws IdInvalidException {
        Optional<AppInfor> currentAppInfor = this.inforService.fetchAppInforById(infor.getId());
        if (!currentAppInfor.isPresent()) {
            throw new IdInvalidException("AppInfor not found");
        }

        return ResponseEntity.ok()
                .body(this.inforService.update(infor, currentAppInfor.get()));
    }

    @DeleteMapping("/infors/{id}")
    @ApiMessage("Delete a infor by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppInfor> currentAppInfor = this.inforService.fetchAppInforById(id);
        if (!currentAppInfor.isPresent()) {
            throw new IdInvalidException("AppInfor not found");
        }
        this.inforService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/infors/{id}")
    @ApiMessage("Get a infor by id")
    public ResponseEntity<AppInfor> getAppInfor(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppInfor> currentAppInfor = this.inforService.fetchAppInforById(id);
        if (!currentAppInfor.isPresent()) {
            throw new IdInvalidException("AppInfor not found");
        }

        return ResponseEntity.ok().body(currentAppInfor.get());
    }

    @GetMapping("/infors")
    @ApiMessage("Get infor with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppInfor(
            @Filter Specification<AppInfor> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.inforService.fetchAll(spec, pageable));
    }
}
