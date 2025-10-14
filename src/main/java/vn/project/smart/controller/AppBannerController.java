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
import vn.project.smart.domain.AppBanner;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.banner.ResCreateAppBannerDTO;
import vn.project.smart.domain.response.banner.ResUpdateAppBannerDTO;
import vn.project.smart.service.AppBannerService;
import vn.project.smart.util.annotation.ApiMessage;
import vn.project.smart.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AppBannerController {

    private final AppBannerService bannerService;

    public AppBannerController(AppBannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PostMapping("/banners")
    @ApiMessage("Create a banner")
    public ResponseEntity<ResCreateAppBannerDTO> create(@Valid @RequestBody AppBanner banner) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.bannerService.create(banner));
    }

    @PutMapping("/banners")
    @ApiMessage("Update a banner")
    public ResponseEntity<ResUpdateAppBannerDTO> update(@Valid @RequestBody AppBanner banner)
            throws IdInvalidException {
        Optional<AppBanner> currentAppBanner = this.bannerService.fetchAppBannerById(banner.getId());
        if (!currentAppBanner.isPresent()) {
            throw new IdInvalidException("AppBanner not found");
        }

        return ResponseEntity.ok()
                .body(this.bannerService.update(banner, currentAppBanner.get()));
    }

    @DeleteMapping("/banners/{id}")
    @ApiMessage("Delete a banner by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppBanner> currentAppBanner = this.bannerService.fetchAppBannerById(id);
        if (!currentAppBanner.isPresent()) {
            throw new IdInvalidException("AppBanner not found");
        }
        this.bannerService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/banners/{id}")
    @ApiMessage("Get a banner by id")
    public ResponseEntity<AppBanner> getAppBanner(@PathVariable("id") long id) throws IdInvalidException {
        Optional<AppBanner> currentAppBanner = this.bannerService.fetchAppBannerById(id);
        if (!currentAppBanner.isPresent()) {
            throw new IdInvalidException("AppBanner not found");
        }

        return ResponseEntity.ok().body(currentAppBanner.get());
    }

    @GetMapping("/banners")
    @ApiMessage("Get banner with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllAppBanner(
            @Filter Specification<AppBanner> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.bannerService.fetchAll(spec, pageable));
    }
}
