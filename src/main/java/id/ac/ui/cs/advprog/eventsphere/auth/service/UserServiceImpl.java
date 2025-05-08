package id.ac.ui.cs.advprog.eventsphere.auth.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getUsernameById(String id) {
        return userRepository.findById(id)
                .map(User::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
}
