package vn.project.smart.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.App;
import vn.project.smart.domain.AppConfig;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.config.ResCreateAppConfigDTO;
import vn.project.smart.domain.response.config.ResUpdateAppConfigDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppConfigRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppConfigService {

    private final AppConfigRepository configRepository;
    private final AppRepository appRepository;

    public AppConfigService(AppConfigRepository configRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.configRepository = configRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppConfig> fetchAppConfigById(long id) {
        return this.configRepository.findById(id);
    }

    public ResCreateAppConfigDTO create(AppConfig j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppConfig
        AppConfig currentAppConfig = this.configRepository.save(j);

        // convert response
        ResCreateAppConfigDTO dto = new ResCreateAppConfigDTO();
        dto.setId(currentAppConfig.getId());
        dto.setName(currentAppConfig.getName());
        dto.setDescription(currentAppConfig.getDescription());
        dto.setIcon(currentAppConfig.getIcon());
        dto.setUrl(currentAppConfig.getUrl());
        dto.setActive(currentAppConfig.isActive());
        dto.setSort(currentAppConfig.getSort());
        dto.setType(currentAppConfig.getType());
        dto.setCreatedAt(currentAppConfig.getCreatedAt());
        dto.setCreatedBy(currentAppConfig.getCreatedBy());

        return dto;
    }

    public ResUpdateAppConfigDTO update(AppConfig j, AppConfig configInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                configInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        configInDB.setName(j.getName());
        configInDB.setDescription(j.getDescription());
        configInDB.setIcon(j.getIcon());
        configInDB.setUrl(j.getUrl());
        configInDB.setActive(j.isActive());
        configInDB.setSort(j.getSort());
        configInDB.setType(j.getType());

        // update config
        AppConfig currentAppConfig = this.configRepository.save(configInDB);

        // convert response
        ResUpdateAppConfigDTO dto = new ResUpdateAppConfigDTO();
        dto.setId(currentAppConfig.getId());
        dto.setName(currentAppConfig.getName());
        dto.setDescription(currentAppConfig.getDescription());
        dto.setIcon(currentAppConfig.getIcon());
        dto.setUrl(currentAppConfig.getUrl());
        dto.setActive(currentAppConfig.isActive());
        dto.setSort(currentAppConfig.getSort());
        dto.setType(currentAppConfig.getType());
        dto.setUpdatedAt(currentAppConfig.getUpdatedAt());
        dto.setUpdatedBy(currentAppConfig.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.configRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppConfig> spec, Pageable pageable) {
        Page<AppConfig> pageUser = this.configRepository.findAll(spec, pageable);

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
