package id.ac.ui.cs.advprog.eventsphere.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor delegateExecutor = new ThreadPoolTaskExecutor();
        // ukuran pool bisa kamu sesuaikan kebutuhan
        delegateExecutor.setCorePoolSize(5);
        delegateExecutor.setMaxPoolSize(10);
        delegateExecutor.setQueueCapacity(25);
        delegateExecutor.setThreadNamePrefix("EvtAsync-");
        delegateExecutor.initialize();
        // Wrap the executor to make it security-context-aware
        return new DelegatingSecurityContextAsyncTaskExecutor(delegateExecutor);
    }
}