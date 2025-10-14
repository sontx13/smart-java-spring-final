package vn.project.smart.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.App;
import vn.project.smart.domain.AppInfor;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.infor.ResCreateAppInforDTO;
import vn.project.smart.domain.response.infor.ResUpdateAppInforDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppInforRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppInforService {

    private final AppInforRepository inforRepository;
    private final AppRepository appRepository;

    public AppInforService(AppInforRepository inforRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.inforRepository = inforRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppInfor> fetchAppInforById(long id) {
        return this.inforRepository.findById(id);
    }

    public ResCreateAppInforDTO create(AppInfor j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppInfor
        AppInfor currentAppInfor = this.inforRepository.save(j);

        // convert response
        ResCreateAppInforDTO dto = new ResCreateAppInforDTO();
        dto.setId(currentAppInfor.getId());
        dto.setName(currentAppInfor.getName());
        dto.setDescription(currentAppInfor.getDescription());
        dto.setIcon(currentAppInfor.getIcon());
        dto.setUrl(currentAppInfor.getUrl());
        dto.setActive(currentAppInfor.isActive());
        dto.setSort(currentAppInfor.getSort());
        dto.setType(currentAppInfor.getType());
        dto.setCreatedAt(currentAppInfor.getCreatedAt());
        dto.setCreatedBy(currentAppInfor.getCreatedBy());

        return dto;
    }

    public ResUpdateAppInforDTO update(AppInfor j, AppInfor inforInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                inforInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        inforInDB.setName(j.getName());
        inforInDB.setDescription(j.getDescription());
        inforInDB.setIcon(j.getIcon());
        inforInDB.setUrl(j.getUrl());
        inforInDB.setActive(j.isActive());
        inforInDB.setSort(j.getSort());
        inforInDB.setType(j.getType());

        // update infor
        AppInfor currentAppInfor = this.inforRepository.save(inforInDB);

        // convert response
        ResUpdateAppInforDTO dto = new ResUpdateAppInforDTO();
        dto.setId(currentAppInfor.getId());
        dto.setName(currentAppInfor.getName());
        dto.setDescription(currentAppInfor.getDescription());
        dto.setIcon(currentAppInfor.getIcon());
        dto.setUrl(currentAppInfor.getUrl());
        dto.setActive(currentAppInfor.isActive());
        dto.setSort(currentAppInfor.getSort());
        dto.setType(currentAppInfor.getType());
        dto.setUpdatedAt(currentAppInfor.getUpdatedAt());
        dto.setUpdatedBy(currentAppInfor.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.inforRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppInfor> spec, Pageable pageable) {
        Page<AppInfor> pageUser = this.inforRepository.findAll(spec, pageable);

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
