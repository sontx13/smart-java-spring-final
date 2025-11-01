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
import vn.project.smart.domain.SyncArticle;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.ResCreateSyncArticleDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppCategoryRepository;
import vn.project.smart.repository.SyncArticleRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class SyncArticleService {

    private final SyncArticleRepository articleRepository;
    private final AppRepository appRepository;
    private final AppCategoryRepository appCategoryRepository;

    public SyncArticleService(SyncArticleRepository articleRepository,
            AppCategoryRepository appCategoryRepository,
            AppRepository appRepository) {
        this.articleRepository = articleRepository;
        this.appCategoryRepository = appCategoryRepository;
        this.appRepository = appRepository;
    }

    public Optional<SyncArticle> fetchSyncArticleById(long id) {
        return this.articleRepository.findById(id);
    }

    public ResCreateSyncArticleDTO create(SyncArticle j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // check category
        if (j.getCategory() != null) {
            Optional<AppCategory> cOptional = this.appCategoryRepository.findById(j.getCategory().getId());
            if (cOptional.isPresent()) {
                j.setCategory(cOptional.get());
            }
        }

        if (j.getIdArticle() != null) {
            SyncArticle articleDB = this.articleRepository.findByIdArticle(j.getIdArticle());
            if (articleDB != null) {
                this.articleRepository.deleteById(articleDB.getId());
            }

            // create SyncArticle
            SyncArticle currentSyncArticle = this.articleRepository.save(j);

            // convert response
            ResCreateSyncArticleDTO dto = new ResCreateSyncArticleDTO();

            dto.setId(currentSyncArticle.getId());
            dto.setIdArticle(currentSyncArticle.getIdArticle());
            dto.setTitle(currentSyncArticle.getTitle());
            dto.setTitleCut(currentSyncArticle.getTitleCut());
            dto.setImageUrl(currentSyncArticle.getImageUrl());
            dto.setSummary(currentSyncArticle.getSummary());
            dto.setContent(currentSyncArticle.getContent());
            dto.setCreatedDate(currentSyncArticle.getCreatedDate());
            dto.setUrlDetail(currentSyncArticle.getUrlDetail());
            dto.setSource(currentSyncArticle.getSource());
            dto.setAuthor(currentSyncArticle.getAuthor());
            dto.setViewCount(currentSyncArticle.getViewCount());
            dto.setCateName(currentSyncArticle.getCateName());
            dto.setCateId(currentSyncArticle.getCateId());
            dto.setIsNew(currentSyncArticle.getIsNew());
            dto.setStrucId(currentSyncArticle.getStrucId());
            dto.setOtherProps(currentSyncArticle.getOtherProps());
            dto.setTimeSync(currentSyncArticle.getTimeSync());

            return dto;
        } else {
            return null;
        }
    }

    public void delete(long id) {
        this.articleRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<SyncArticle> spec, Pageable pageable) {

        Specification<SyncArticle> filterSpec = spec;

        // 🔸 Lấy param từ request (nếu có)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            String isNew = request.getParameter("isNew");
            String titleCut = request.getParameter("titleCut");
            String appId = request.getParameter("app.id");
            String cateId = request.getParameter("category.id");

            // 🔹 Nếu có is_new
            if (isNew != null) {
                // Ép về string trim để tránh khoảng trắng hoặc ký tự khác
                String isNewStr = isNew.trim().replace("'", "");
                filterSpec = filterSpec.and((root, query, cb) -> cb.equal(root.get("isNew"), isNewStr));
            }

            // 🔹 Nếu có title_cut (tìm kiếm gần đúng)
            if (titleCut != null && !titleCut.isEmpty()) {
                String titleKeyword = titleCut.trim().replace("'", "");
                filterSpec = filterSpec.and((root, query, cb) -> cb.like(cb.lower(root.get("titleCut")),
                        "%" + titleKeyword.toLowerCase() + "%"));
            }

            // 🔹 Nếu có app.id
            if (appId != null) {
                filterSpec = filterSpec
                        .and((root, query, cb) -> cb.equal(root.get("app").get("id"), Long.valueOf(appId)));
            }

            // 🔹 Nếu có category.id
            if (cateId != null) {
                filterSpec = filterSpec
                        .and((root, query, cb) -> cb.equal(root.get("category").get("id"), Long.valueOf(cateId)));
            }
        }

        // ✅ Gọi repository với filterSpec đã cộng thêm điều kiện
        Page<SyncArticle> pageUser = this.articleRepository.findAll(filterSpec, pageable);

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
