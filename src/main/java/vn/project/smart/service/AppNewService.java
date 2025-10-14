package vn.project.smart.service;

import java.util.Optional;
import java.util.Locale.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.App;
import vn.project.smart.domain.AppCategory;
import vn.project.smart.domain.AppNew;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.appnew.ResCreateAppNewDTO;
import vn.project.smart.domain.response.appnew.ResUpdateAppNewDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppCategoryRepository;
import vn.project.smart.repository.AppNewRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppNewService {

    private final AppNewRepository appnewRepository;
    private final AppRepository appRepository;
    private final AppCategoryRepository appCategoryRepository;

    public AppNewService(AppNewRepository appnewRepository,
            SkillRepository skillRepository,
            AppCategoryRepository appCategoryRepository,
            AppRepository appRepository) {
        this.appnewRepository = appnewRepository;
        this.appCategoryRepository = appCategoryRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppNew> fetchAppNewById(long id) {
        return this.appnewRepository.findById(id);
    }

    public ResCreateAppNewDTO create(AppNew j) {

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

        // create AppNew
        AppNew currentAppNew = this.appnewRepository.save(j);

        // convert response
        ResCreateAppNewDTO dto = new ResCreateAppNewDTO();
        dto.setId(currentAppNew.getId());
        dto.setTitle(currentAppNew.getTitle());
        dto.setDescription(currentAppNew.getDescription());
        dto.setLogo(currentAppNew.getLogo());
        dto.setUrl(currentAppNew.getUrl());
        dto.setActive(currentAppNew.isActive());
        dto.setContent(currentAppNew.getContent());
        dto.setPublic_at(currentAppNew.getPublic_at());
        dto.setCreatedAt(currentAppNew.getCreatedAt());
        dto.setCreatedBy(currentAppNew.getCreatedBy());

        return dto;
    }

    public ResUpdateAppNewDTO update(AppNew j, AppNew appnewInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                appnewInDB.setApp(cOptional.get());
            }
        }

        // check category
        if (j.getCategory() != null) {
            Optional<AppCategory> cOptional = this.appCategoryRepository.findById(j.getCategory().getId());
            if (cOptional.isPresent()) {
                j.setCategory(cOptional.get());
            }
        }

        // update correct info
        appnewInDB.setTitle(j.getTitle());
        appnewInDB.setDescription(j.getDescription());
        appnewInDB.setLogo(j.getLogo());
        appnewInDB.setUrl(j.getUrl());
        appnewInDB.setActive(j.isActive());
        appnewInDB.setContent(j.getContent());
        appnewInDB.setPublic_at(j.getPublic_at());

        // update appnew
        AppNew currentAppNew = this.appnewRepository.save(appnewInDB);

        // convert response
        ResUpdateAppNewDTO dto = new ResUpdateAppNewDTO();
        dto.setId(currentAppNew.getId());
        dto.setTitle(currentAppNew.getTitle());
        dto.setDescription(currentAppNew.getDescription());
        dto.setLogo(currentAppNew.getLogo());
        dto.setUrl(currentAppNew.getUrl());
        dto.setActive(currentAppNew.isActive());
        dto.setContent(currentAppNew.getContent());
        dto.setPublic_at(currentAppNew.getPublic_at());
        dto.setUpdatedAt(currentAppNew.getUpdatedAt());
        dto.setUpdatedBy(currentAppNew.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.appnewRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppNew> spec, Pageable pageable) {
        Page<AppNew> pageUser = this.appnewRepository.findAll(spec, pageable);

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
