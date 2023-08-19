package org.temkarus0070.efmsocialmedia.security.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsManager implements org.springframework.security.provisioning.UserDetailsManager {

    private UserAccountRepository userAccountRepository;

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userAccountRepository.findById(username)
                .isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> userOptional = userAccountRepository.findById(username);
        if (userOptional.isPresent()) {
            UserAccount userAccount = userOptional.get();
            return new org.springframework.security.core.userdetails.User(userAccount.getUsername(),
                    userAccount.getPassword(),
                    userAccount.isEnabled(),
                    true,
                    true,
                    userAccount.isEnabled(),
                    new ArrayList<>());
        }
        throw new UsernameNotFoundException(username);
    }
}
