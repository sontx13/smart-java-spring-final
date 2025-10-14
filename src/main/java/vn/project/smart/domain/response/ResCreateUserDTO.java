package vn.project.smart.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.project.smart.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private boolean is_admin;
    private String phone;
    private Instant createdAt;
    private CompanyUser company;
    private AppUser app;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
        private String logo;
    }

    @Getter
    @Setter
    public static class AppUser {
        private long id;
        private String name;
        private String logo;
    }
}
