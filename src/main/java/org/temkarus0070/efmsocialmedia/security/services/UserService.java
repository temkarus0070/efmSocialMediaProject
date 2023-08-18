package org.temkarus0070.efmsocialmedia.security.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.User;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsManager {

    private UserRepository userRepository;

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
        return userRepository.findByUsername(username)
                             .isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                          user.getPassword(),
                                                                          user.isEnabled(),
                                                                          true,
                                                                          true,
                                                                          user.isEnabled(),
                                                                          new ArrayList<>());
        }
        throw new UsernameNotFoundException(username);
    }
}
