package id.ac.ui.cs.advprog.eventsphere.config;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.RatingObserver;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.RatingSubject;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObserverRegistrationConfig implements CommandLineRunner {

    private final RatingSubject ratingSubject;
    private final RatingObserver ratingObserver;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Registering RatingObserver with RatingSubject...");
        ratingSubject.addObserver(ratingObserver);
        System.out.println("RatingObserver registered successfully!");
    }
}