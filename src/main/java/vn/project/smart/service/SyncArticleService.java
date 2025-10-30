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

        if (j.getId_article() != null) {
            SyncArticle articleDB = this.articleRepository.findByIdArticle(j.getId_article());
            if (articleDB != null) {
                this.articleRepository.deleteById(articleDB.getId());
            }

            // create SyncArticle
            SyncArticle currentSyncArticle = this.articleRepository.save(j);

            // convert response
            ResCreateSyncArticleDTO dto = new ResCreateSyncArticleDTO();

            dto.setId(currentSyncArticle.getId());
            dto.setId_article(currentSyncArticle.getId_article());
            dto.setTitle(currentSyncArticle.getTitle());
            dto.setTitle_cut(currentSyncArticle.getTitle_cut());
            dto.setImage_url(currentSyncArticle.getImage_url());
            dto.setSummary(currentSyncArticle.getSummary());
            dto.setContent(currentSyncArticle.getContent());
            dto.setCreated_date(currentSyncArticle.getCreated_date());
            dto.setUrl_detail(currentSyncArticle.getUrl_detail());
            dto.setSource(currentSyncArticle.getSource());
            dto.setAuthor(currentSyncArticle.getAuthor());
            dto.setView_count(currentSyncArticle.getView_count());
            dto.setCate_name(currentSyncArticle.getCate_name());
            dto.setCate_id(currentSyncArticle.getCate_id());
            dto.setIs_new(currentSyncArticle.getIs_new());
            dto.setStruc_id(currentSyncArticle.getStruc_id());
            dto.setOther_props(currentSyncArticle.getOther_props());
            dto.setTime_sync(currentSyncArticle.getTime_sync());

            return dto;
        } else {
            return null;
        }
    }

    public ResultPaginationDTO fetchAll(Specification<SyncArticle> spec, Pageable pageable) {

        Specification<SyncArticle> filterSpec = spec;

        // 🔸 Lấy param từ request (nếu có)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            String isNew = request.getParameter("is_new");
            String titleCut = request.getParameter("title_cut");
            String appId = request.getParameter("app.id");
            String cateId = request.getParameter("category.id");

            // 🔹 Nếu có is_new
            if (isNew != null) {
                // Ép về string trim để tránh khoảng trắng hoặc ký tự khác
                String isNewStr = isNew.trim().replace("'", "");
                filterSpec = filterSpec.and((root, query, cb) -> cb.equal(root.get("is_new"), isNewStr));
            }

            // 🔹 Nếu có title_cut (tìm kiếm gần đúng)
            if (titleCut != null && !titleCut.isEmpty()) {
                String titleKeyword = titleCut.trim().replace("'", "");
                filterSpec = filterSpec.and((root, query, cb) -> cb.like(cb.lower(root.get("title_cut")),
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
