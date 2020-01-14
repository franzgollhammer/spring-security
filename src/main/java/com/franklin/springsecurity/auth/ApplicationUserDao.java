package com.franklin.springsecurity.auth;

import java.util.Optional;

/**
 * ApplicationUserDao
 */
public interface ApplicationUserDao {
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}