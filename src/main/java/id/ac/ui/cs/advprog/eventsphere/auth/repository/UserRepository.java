package id.ac.ui.cs.advprog.eventsphere.auth.repository;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    void save(User user);
}
