package com.bootlabs.digest.service;


import com.bootlabs.digest.entity.User;
import com.bootlabs.digest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    /**
     * Load user info by credential
     *
     * @param usernameValue username or email
     * @return UserDetails object
     */
    @Override
    public UserDetails loadUserByUsername(String usernameValue) {
        Optional<User> user = userRepository.findActiveByUsername(usernameValue);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        detailsChecker.check(user.get());
        return user.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
