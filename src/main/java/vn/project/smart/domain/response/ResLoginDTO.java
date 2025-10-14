package vn.project.smart.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.project.smart.domain.App;
import vn.project.smart.domain.Company;
import vn.project.smart.domain.Role;
import vn.project.smart.util.constant.GenderEnum;

@Getter
@Setter
public class ResLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;

    private UserLogin user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
        private String phone;
        private String address;
        private int age;
        private GenderEnum gender;
        private Role role;
        private Company company;
        private App app;
        private boolean is_admin;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        private UserLogin user;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String email;
        private String name;
        private String phone;
        private String address;
        private int age;
        private GenderEnum gender;
        private RoleUser role;
        private CompanyUser company;
        private AppUser app;
        private boolean is_admin;

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

        @Getter
        @Setter
        public static class RoleUser {
            private long id;
            private String name;
        }
    }

}
