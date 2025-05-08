package id.ac.ui.cs.advprog.eventsphere.auth.service;

public interface UserService {
    String getUsernameById(String id);
    void addBalance(String userId, double amount);
    boolean deductBalance(String userId, double amount);
    double getBalance(String userId);
}
