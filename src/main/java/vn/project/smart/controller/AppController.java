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
import vn.project.smart.domain.App;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.service.AppService;
import vn.project.smart.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/apps")
    public ResponseEntity<?> createApp(@Valid @RequestBody App reqApp) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.appService.handleCreateApp(reqApp));
    }

    @GetMapping("/apps")
    @ApiMessage("Fetch apps")
    public ResponseEntity<ResultPaginationDTO> getApp(
            @Filter Specification<App> spec, Pageable pageable) {

        return ResponseEntity.ok(this.appService.handleGetApp(spec, pageable));
    }

    @PutMapping("/apps")
    public ResponseEntity<App> updateApp(@Valid @RequestBody App reqApp) {
        App updatedApp = this.appService.handleUpdateApp(reqApp);
        return ResponseEntity.ok(updatedApp);
    }

    @DeleteMapping("/apps/{id}")
    public ResponseEntity<Void> deleteApp(@PathVariable("id") long id) {
        this.appService.handleDeleteApp(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/apps/{id}")
    @ApiMessage("fetch app by id")
    public ResponseEntity<App> fetchAppById(@PathVariable("id") long id) {
        Optional<App> cOptional = this.appService.findById(id);
        return ResponseEntity.ok().body(cOptional.get());
    }
}
