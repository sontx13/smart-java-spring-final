package vn.project.smart.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.App;
import vn.project.smart.domain.AppZmau;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.domain.response.zmau.ResCreateAppZmauDTO;
import vn.project.smart.domain.response.zmau.ResUpdateAppZmauDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.AppZmauRepository;
import vn.project.smart.repository.SkillRepository;

@Service
public class AppZmauService {

    private final AppZmauRepository zmuRepository;
    private final AppRepository appRepository;

    public AppZmauService(AppZmauRepository zmuRepository,
            SkillRepository skillRepository,
            AppRepository appRepository) {
        this.zmuRepository = zmuRepository;
        this.appRepository = appRepository;
    }

    public Optional<AppZmau> fetchAppZmauById(long id) {
        return this.zmuRepository.findById(id);
    }

    public ResCreateAppZmauDTO create(AppZmau j) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                j.setApp(cOptional.get());
            }
        }

        // create AppZmau
        AppZmau currentAppZmau = this.zmuRepository.save(j);

        // convert response
        ResCreateAppZmauDTO dto = new ResCreateAppZmauDTO();
        dto.setId(currentAppZmau.getId());
        dto.setName(currentAppZmau.getName());
        dto.setAvatar(currentAppZmau.getAvatar());
        dto.setPhone_number(currentAppZmau.getPhone_number());
        dto.setZid(currentAppZmau.getZid());
        dto.setCreatedAt(currentAppZmau.getCreatedAt());
        dto.setCreatedBy(currentAppZmau.getCreatedBy());

        return dto;
    }

    public ResUpdateAppZmauDTO update(AppZmau j, AppZmau zmuInDB) {

        // check app
        if (j.getApp() != null) {
            Optional<App> cOptional = this.appRepository.findById(j.getApp().getId());
            if (cOptional.isPresent()) {
                zmuInDB.setApp(cOptional.get());
            }
        }

        // update correct info
        zmuInDB.setName(j.getName());
        zmuInDB.setName(j.getName());
        zmuInDB.setAvatar(j.getAvatar());
        zmuInDB.setPhone_number(j.getPhone_number());
        zmuInDB.setZid(j.getZid());

        // update zmu
        AppZmau currentAppZmau = this.zmuRepository.save(zmuInDB);

        // convert response
        ResUpdateAppZmauDTO dto = new ResUpdateAppZmauDTO();
        dto.setId(currentAppZmau.getId());
        dto.setName(currentAppZmau.getName());
        dto.setAvatar(currentAppZmau.getAvatar());
        dto.setPhone_number(currentAppZmau.getPhone_number());
        dto.setZid(currentAppZmau.getZid());
        dto.setUpdatedAt(currentAppZmau.getUpdatedAt());
        dto.setUpdatedBy(currentAppZmau.getUpdatedBy());
        return dto;
    }

    public void delete(long id) {
        this.zmuRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<AppZmau> spec, Pageable pageable) {
        Page<AppZmau> pageUser = this.zmuRepository.findAll(spec, pageable);

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
