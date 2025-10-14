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
import vn.project.smart.domain.AppConfig;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.config.ResCreateAppConfigDTO;
import vn.project.smart.domain.response.config.ResUpdateAppConfigDTO;
import vn.project.smart.service.AppConfigService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppConfigController {

    private final AppConfigService configService;

    public AppConfigController(AppConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/configs")
    @ApiMessage("Create a config")
    public ResponseEntity<ResCreateAppConfigDTO> create(@Valid @RequestBody AppConfig config) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.configService.create(config));
    }

    @PutMapping("/configs")
    @ApiMessage("Update a config")
    public ResponseEntity<ResUpdateAppConfigDTO> update(@Valid @RequestBody AppConfig config)
            throws IdInvalidException {
        Optional<AppConfig> currentAppConfig = this.configService.fetchAppConfigById(config.getId());
        if (!currentAppConfig.isPresent()) {
            throw new IdInvalidException("AppConfig not found");
        }

        return ResponseEntity.ok()
                .body(this.configService.update(config, currentAppConfig.get()));
    }

    @DeleteMapping("/configs/{id}")
    @ApiMessage("Delete a config by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppConfig> currentAppConfig = this.configService.fetchAppConfigById(id);
        if (!currentAppConfig.isPresent()) {
            throw new IdInvalidException("AppConfig not found");
        }
        this.configService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/configs/{id}")
    @ApiMessage("Get a config by id")
    public ResponseEntity<AppConfig> getAppConfig(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppConfig> currentAppConfig = this.configService.fetchAppConfigById(id);
        if (!currentAppConfig.isPresent()) {
            throw new IdInvalidException("AppConfig not found");
        }

        return ResponseEntity.ok().body(currentAppConfig.get());
    }

    @GetMapping("/configs")
    @ApiMessage("Get config with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppConfig(
            @Filter Specification<AppConfig> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.configService.fetchAll(spec, pageable));
    }
}
