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
import vn.project.smart.domain.AppHotline;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.hotline.ResCreateAppHotlineDTO;
import vn.project.smart.domain.response.hotline.ResUpdateAppHotlineDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppHotlineRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppHotlineService {

    private final AppHotlineRepository hotlineRepository;
    private final AppRepository appRepository;

    public AppHotlineService(AppHotlineRepository hotlineRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.hotlineRepository = hotlineRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppHotline> fetchAppHotlineById(long id) {
        return this.hotlineRepository.findById(id);
    }

    public ResCreateAppHotlineDTO create(AppHotline j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppHotline
        AppHotline currentAppHotline = this.hotlineRepository.save(j);

        // convert response
        ResCreateAppHotlineDTO dto = new ResCreateAppHotlineDTO();
        dto.setId(currentAppHotline.getId());
        dto.setName(currentAppHotline.getName());
        dto.setDescription(currentAppHotline.getDescription());
        dto.setIcon(currentAppHotline.getIcon());
        dto.setPhone_number(currentAppHotline.getPhone_number());
        dto.setActive(currentAppHotline.isActive());
        dto.setSort(currentAppHotline.getSort());
        dto.setType(currentAppHotline.getType());
        dto.setCreatedAt(currentAppHotline.getCreatedAt());
        dto.setCreatedBy(currentAppHotline.getCreatedBy());

        return dto;
    }

    public ResUpdateAppHotlineDTO update(AppHotline j, AppHotline hotlineInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                hotlineInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        hotlineInDB.setName(j.getName());
        hotlineInDB.setDescription(j.getDescription());
        hotlineInDB.setIcon(j.getIcon());
        hotlineInDB.setPhone_number(j.getPhone_number());
        hotlineInDB.setActive(j.isActive());
        hotlineInDB.setSort(j.getSort());
        hotlineInDB.setType(j.getType());

        // update hotline
        AppHotline currentAppHotline = this.hotlineRepository.save(hotlineInDB);

        // convert response
        ResUpdateAppHotlineDTO dto = new ResUpdateAppHotlineDTO();
        dto.setId(currentAppHotline.getId());
        dto.setName(currentAppHotline.getName());
        dto.setDescription(currentAppHotline.getDescription());
        dto.setIcon(currentAppHotline.getIcon());
        dto.setPhone_number(currentAppHotline.getPhone_number());
        dto.setActive(currentAppHotline.isActive());
        dto.setSort(currentAppHotline.getSort());
        dto.setType(currentAppHotline.getType());
        dto.setUpdatedAt(currentAppHotline.getUpdatedAt());
        dto.setUpdatedBy(currentAppHotline.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.hotlineRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppHotline> spec, Pageable pageable) {

        Specification<AppHotline> filterSpec = spec;

        // 🔸 Lấy param từ request (nếu có)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            String appId = request.getParameter("app.id");
            String activeParam = request.getParameter("active");

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

        // ✅ Gọi repository với filterSpec
        Page<AppHotline> pageUser = this.hotlineRepository.findAll(filterSpec, pageable);

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
