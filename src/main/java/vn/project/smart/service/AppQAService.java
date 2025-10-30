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
import vn.project.smart.domain.AppQA;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.qa.ResCreateAppQADTO;
import vn.project.smart.domain.response.qa.ResUpdateAppQADTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppQARepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppQAService {

    private final AppQARepository qaRepository;
    private final AppRepository appRepository;

    public AppQAService(AppQARepository qaRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.qaRepository = qaRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppQA> fetchAppQAById(long id) {
        return this.qaRepository.findById(id);
    }

    public ResCreateAppQADTO create(AppQA j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppQA
        AppQA currentAppQA = this.qaRepository.save(j);

        // convert response
        ResCreateAppQADTO dto = new ResCreateAppQADTO();
        dto.setId(currentAppQA.getId());
        dto.setName_q(currentAppQA.getName_q());
        dto.setName_a(currentAppQA.getName_a());
        dto.setTime_q(currentAppQA.getTime_q());
        dto.setTime_a(currentAppQA.getTime_a());
        dto.setEmail_q(currentAppQA.getEmail_q());
        dto.setPhone_q(currentAppQA.getPhone_q());
        dto.setContent_a(currentAppQA.getContent_a());
        dto.setContent_q(currentAppQA.getContent_q());
        dto.setActive(currentAppQA.isActive());
        dto.setCreatedAt(currentAppQA.getCreatedAt());
        dto.setCreatedBy(currentAppQA.getCreatedBy());

        return dto;
    }

    public ResUpdateAppQADTO update(AppQA j, AppQA qaInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                qaInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        qaInDB.setName_q(j.getName_q());
        qaInDB.setName_a(j.getName_a());
        qaInDB.setTime_q(j.getTime_q());
        qaInDB.setTime_a(j.getTime_a());
        qaInDB.setEmail_q(j.getEmail_q());
        qaInDB.setPhone_q(j.getPhone_q());
        qaInDB.setContent_a(j.getContent_a());
        qaInDB.setContent_q(j.getContent_q());

        // update qa
        AppQA currentAppQA = this.qaRepository.save(qaInDB);

        // convert response
        ResUpdateAppQADTO dto = new ResUpdateAppQADTO();
        dto.setId(currentAppQA.getId());
        dto.setName_q(currentAppQA.getName_q());
        dto.setName_a(currentAppQA.getName_a());
        dto.setTime_q(currentAppQA.getTime_q());
        dto.setTime_a(currentAppQA.getTime_a());
        dto.setEmail_q(currentAppQA.getEmail_q());
        dto.setPhone_q(currentAppQA.getPhone_q());
        dto.setActive(currentAppQA.isActive());
        dto.setContent_a(currentAppQA.getContent_a());
        dto.setContent_q(currentAppQA.getContent_q());
        dto.setUpdatedAt(currentAppQA.getUpdatedAt());
        dto.setUpdatedBy(currentAppQA.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.qaRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppQA> spec, Pageable pageable) {

        Specification<AppQA> filterSpec = spec;

        // 🔸 Lấy param từ request (nếu có)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            String activeParam = request.getParameter("active");
            String contentQ = request.getParameter("content_q");
            String appId = request.getParameter("app.id");

            // 🔹 Nếu có active (boolean)
            if (activeParam != null) {
                boolean activeValue = Boolean.parseBoolean(activeParam.trim());
                filterSpec = filterSpec.and((root, query, cb) -> cb.equal(root.get("active"), activeValue));
            }

            // 🔹 Nếu có content_q (tìm kiếm gần đúng)
            if (contentQ != null && !contentQ.trim().isEmpty()) {
                String keyword = "%" + contentQ.trim() + "%";
                filterSpec = filterSpec
                        .and((root, query, cb) -> cb.like(cb.lower(root.get("content_q")), keyword.toLowerCase()));
            }

            // 🔹 Nếu có app.id
            if (appId != null) {
                filterSpec = filterSpec
                        .and((root, query, cb) -> cb.equal(root.get("app").get("id"), Long.valueOf(appId)));
            }
        }

        // ✅ Truy vấn repository với filterSpec
        Page<AppQA> pageUser = this.qaRepository.findAll(filterSpec, pageable);

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
