// src/test/java/id/ac/ui/cs/advprog/eventsphere/event/service/EventServiceAsyncTest.java
package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(EventServiceAsyncTest.TestConfig.class)
class EventServiceAsyncTest {

    @Configuration
    @EnableAsync
    static class TestConfig implements AsyncConfigurer {
        @Bean EventRepository repo() {
            return mock(EventRepository.class);
        }
        @Bean EventServiceImpl service(EventRepository repo) {
            return new EventServiceImpl(repo);
        }
        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
            t.setCorePoolSize(1);
            t.afterPropertiesSet();
            return t;
        }
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return (ex, method, params) -> {};
        }
    }

    @Autowired EventService svc;
    @Autowired EventRepository repo;

    private final String ID = "evt1";

    @BeforeEach
    void resetRepo() {
        reset(repo);
    }

    @Test
    void createEventAsync_success() {
        Event e = new Event();
        CompletableFuture<Event> f = svc.createEventAsync(e);
        f.join();
        verify(repo).save(e);
    }

    @Test
    void createEventAsync_null_returnsNull() {
        CompletableFuture<Event> f = svc.createEventAsync(null);
        assertThat(f.join()).isNull();
        verify(repo).save(null);
    }

    @Test
    void updateStatusAsync_success() {
        Event e = new Event();
        e.setId(ID);
        when(repo.findById(ID)).thenReturn(Optional.of(e));

        CompletableFuture<Event> f =
                svc.updateStatusAsync(ID, EventStatus.PUBLISHED.getValue());
        f.join();

        assertThat(e.getStatus())
                .isEqualTo(EventStatus.PUBLISHED.getValue());
        verify(repo).save(e);
    }

    @Test
    void updateStatusAsync_notFound_throws() {
        when(repo.findById(ID)).thenReturn(Optional.empty());

        CompletableFuture<Event> f = svc.updateStatusAsync(ID, "ANY");
        assertThatThrownBy(f::join)
                .hasCauseInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteEventAsync_success() {
        when(repo.existsById(ID)).thenReturn(true);

        CompletableFuture<Void> f = svc.deleteEventAsync(ID);
        f.join();

        verify(repo).deleteById(ID);
    }

    @Test
    void deleteEventAsync_notFound_throws() {
        when(repo.existsById(ID)).thenReturn(false);

        CompletableFuture<Void> f = svc.deleteEventAsync(ID);
        assertThatThrownBy(f::join)
                .hasCauseInstanceOf(NoSuchElementException.class);
    }
}
