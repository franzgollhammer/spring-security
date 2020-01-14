package com.franklin.springsecurity.auth;

import java.util.List;
import java.util.Optional;

import com.franklin.springsecurity.security.ApplicationUserRole;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * FakeApplicationUserDaoService
 */
@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
            .stream()
            .filter(user -> username.equals(user.getUsername()))
            .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser("annasmith", passwordEncoder.encode("password"),
                        ApplicationUserRole.STUDENT.getGrantedAuthorities(), true, true, true, true),
                new ApplicationUser("janedoe", passwordEncoder.encode("password123"),
                        ApplicationUserRole.ADMIN.getGrantedAuthorities(), true, true, true, true),
                new ApplicationUser("tomjones", passwordEncoder.encode("password123"),
                        ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(), true, true, true, true));

        return applicationUsers;
    }

}