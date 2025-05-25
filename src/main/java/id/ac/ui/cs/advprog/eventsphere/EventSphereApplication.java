package id.ac.ui.cs.advprog.eventsphere;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventSphereApplication {

    public static void main(String[] args) {

        try {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
            System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
            System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
            System.setProperty("jwt.secret-key", dotenv.get("JWT_SECRET_KEY"));
            System.setProperty("jwt.expiration", dotenv.get("JWT_EXPIRATION"));
            System.out.println("SUCCESS: .env file loaded successfully");
        } catch (Exception e) {
            System.out.println("ERROR: .env file not found");
        }

        SpringApplication.run(EventSphereApplication.class, args);
    }

}
