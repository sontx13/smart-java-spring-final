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
import vn.project.smart.domain.AppCategory;
import vn.project.smart.domain.AppQA;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.category.ResCreateAppCategoryDTO;
import vn.project.smart.domain.response.category.ResUpdateAppCategoryDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppCategoryRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppCategoryService {

    private final AppCategoryRepository categoryRepository;
    private final AppRepository appRepository;

    public AppCategoryService(AppCategoryRepository categoryRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.categoryRepository = categoryRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppCategory> fetchAppCategoryById(long id) {
        return this.categoryRepository.findById(id);
    }

    public ResCreateAppCategoryDTO create(AppCategory j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppCategory
        AppCategory currentAppCategory = this.categoryRepository.save(j);

        // convert response
        ResCreateAppCategoryDTO dto = new ResCreateAppCategoryDTO();
        dto.setId(currentAppCategory.getId());
        dto.setName(currentAppCategory.getName());
        dto.setIcon(currentAppCategory.getIcon());
        dto.setUrl(currentAppCategory.getUrl());
        dto.setActive(currentAppCategory.isActive());
        dto.setSort(currentAppCategory.getSort());
        dto.setType(currentAppCategory.getType());
        dto.setCreatedAt(currentAppCategory.getCreatedAt());
        dto.setCreatedBy(currentAppCategory.getCreatedBy());

        return dto;
    }

    public ResUpdateAppCategoryDTO update(AppCategory j, AppCategory categoryInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                categoryInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        categoryInDB.setName(j.getName());
        categoryInDB.setIcon(j.getIcon());
        categoryInDB.setUrl(j.getUrl());
        categoryInDB.setActive(j.isActive());
        categoryInDB.setSort(j.getSort());
        categoryInDB.setType(j.getType());

        // update category
        AppCategory currentAppCategory = this.categoryRepository.save(categoryInDB);

        // convert response
        ResUpdateAppCategoryDTO dto = new ResUpdateAppCategoryDTO();
        dto.setId(currentAppCategory.getId());
        dto.setName(currentAppCategory.getName());
        dto.setIcon(currentAppCategory.getIcon());
        dto.setUrl(currentAppCategory.getUrl());
        dto.setActive(currentAppCategory.isActive());
        dto.setSort(currentAppCategory.getSort());
        dto.setType(currentAppCategory.getType());
        dto.setUpdatedAt(currentAppCategory.getUpdatedAt());
        dto.setUpdatedBy(currentAppCategory.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.categoryRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppCategory> spec, Pageable pageable) {

        Specification<AppCategory> filterSpec = spec;

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
        Page<AppCategory> pageUser = this.categoryRepository.findAll(filterSpec, pageable);

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
