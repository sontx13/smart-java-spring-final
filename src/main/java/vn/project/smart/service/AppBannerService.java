package vn.project.smart.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import vn.project.smart.domain.App;
import vn.project.smart.domain.AppBanner;
import vn.project.smart.domain.AppCategory;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.banner.ResCreateAppBannerDTO;
import vn.project.smart.domain.response.banner.ResUpdateAppBannerDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppBannerRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppBannerService {

    private final AppBannerRepository bannerRepository;
    private final AppRepository appRepository;

    public AppBannerService(AppBannerRepository bannerRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.bannerRepository = bannerRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppBanner> fetchAppBannerById(long id) {
        return this.bannerRepository.findById(id);
    }

    public ResCreateAppBannerDTO create(AppBanner j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppBanner
        AppBanner currentAppBanner = this.bannerRepository.save(j);

        // convert response
        ResCreateAppBannerDTO dto = new ResCreateAppBannerDTO();
        dto.setId(currentAppBanner.getId());
        dto.setName(currentAppBanner.getName());
        dto.setImage(currentAppBanner.getImage());
        dto.setUrl(currentAppBanner.getUrl());
        dto.setActive(currentAppBanner.isActive());
        dto.setSort(currentAppBanner.getSort());
        dto.setType(currentAppBanner.getType());
        dto.setCreatedAt(currentAppBanner.getCreatedAt());
        dto.setCreatedBy(currentAppBanner.getCreatedBy());

        return dto;
    }

    public ResUpdateAppBannerDTO update(AppBanner j, AppBanner bannerInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                bannerInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        bannerInDB.setName(j.getName());
        bannerInDB.setImage(j.getImage());
        bannerInDB.setUrl(j.getUrl());
        bannerInDB.setActive(j.isActive());
        bannerInDB.setSort(j.getSort());
        bannerInDB.setType(j.getType());

        // update banner
        AppBanner currentAppBanner = this.bannerRepository.save(bannerInDB);

        // convert response
        ResUpdateAppBannerDTO dto = new ResUpdateAppBannerDTO();
        dto.setId(currentAppBanner.getId());
        dto.setName(currentAppBanner.getName());
        dto.setImage(currentAppBanner.getImage());
        dto.setUrl(currentAppBanner.getUrl());
        dto.setActive(currentAppBanner.isActive());
        dto.setSort(currentAppBanner.getSort());
        dto.setType(currentAppBanner.getType());
        dto.setUpdatedAt(currentAppBanner.getUpdatedAt());
        dto.setUpdatedBy(currentAppBanner.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.bannerRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppBanner> spec, Pageable pageable) {

        Specification<AppBanner> filterSpec = spec;

        // 🔸 Lấy param từ request (nếu có)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            String activeParam = request.getParameter("active");
            String appId = request.getParameter("app.id");

            // 🔹 Nếu có active (boolean)
            if (activeParam != null) {
                boolean activeValue = Boolean.parseBoolean(activeParam.trim());
                filterSpec = filterSpec.and((root, query, cb) -> cb.equal(root.get("active"), activeValue));
            }

            // 🔹 Nếu có app.id
            if (appId != null) {
                filterSpec = filterSpec
                        .and((root, query, cb) -> cb.equal(root.get("app").get("id"), Long.valueOf(appId)));
            }
        }

        // ✅ Truy vấn repository với filterSpec
        Page<AppBanner> pageUser = this.bannerRepository.findAll(filterSpec, pageable);

        // 🔹 Chuẩn bị dữ liệu trả về
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
    }
}
