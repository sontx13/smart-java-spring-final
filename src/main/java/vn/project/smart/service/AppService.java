package vn.project.smart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.project.smart.domain.App;
import vn.project.smart.domain.User;
import vn.project.smart.domain.response.ResultPaginationDTO;
import vn.project.smart.repository.AppRepository;
import vn.project.smart.repository.UserRepository;

@Service
public class AppService {

    private final AppRepository appRepository;
    private final UserRepository userRepository;

    public AppService(
            AppRepository appRepository,
            UserRepository userRepository) {
        this.appRepository = appRepository;
        this.userRepository = userRepository;
    }

    public App handleCreateApp(App c) {
        return this.appRepository.save(c);
    }

    public ResultPaginationDTO handleGetApp(Specification<App> spec, Pageable pageable) {
        Page<App> pApp = this.appRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pApp.getTotalPages());
        mt.setTotal(pApp.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pApp.getContent());
        return rs;
    }

    public App handleUpdateApp(App c) {
        Optional<App> appOptional = this.appRepository.findById(c.getId());
        if (appOptional.isPresent()) {
            App currentApp = appOptional.get();
            currentApp.setLogo(c.getLogo());
            currentApp.setName(c.getName());
            currentApp.setDescription(c.getDescription());
            currentApp.setType(c.getType());
            currentApp.setSort(c.getSort());
            currentApp.setActive(c.isActive());
            return this.appRepository.save(currentApp);
        }
        return null;
    }

    public void handleDeleteApp(long id) {
        this.appRepository.deleteById(id);
    }

    public Optional<App> findById(long id) {
        return this.appRepository.findById(id);
    }
}
