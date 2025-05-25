package id.ac.ui.cs.advprog.eventsphere.event.repository;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testSave_AddNewEvent() {
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        eventRepository.save(event);

        assertThat(eventRepository.findById(event.getId())).isPresent();
    }

    @Test
    void testSave_UpdateExistingEvent() {
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        eventRepository.save(event);

        event.setDescription("Konser musik terbesar tahun ini");
        eventRepository.save(event);

        Event updated = eventRepository.findById(event.getId()).orElseThrow();
        assertThat(updated.getDescription())
                .isEqualTo("Konser musik terbesar tahun ini");
    }

    @Test
    void testFindById_InvalidId() {
        assertThat(eventRepository.findById("INVALID")).isNotPresent();
    }

    @Test
    void testFindAllByOrganizer() {
        Event e1 = new Event();
        e1.setId(UUID.randomUUID().toString());
        e1.setTitle("A");
        e1.setDescription("D");
        e1.setDate("2025-05-01");
        e1.setLocation("L");
        e1.setPrice(100.0);
        e1.setOrganizer("OrgX");

        Event e2 = new Event();
        e2.setId(UUID.randomUUID().toString());
        e2.setTitle("B");
        e2.setDescription("D2");
        e2.setDate("2025-06-01");
        e2.setLocation("L2");
        e2.setPrice(200.0);
        e2.setOrganizer("OrgY");
        eventRepository.save(e1);
        eventRepository.save(e2);

        List<Event> byOrgX = eventRepository.findByOrganizer("OrgX");
        assertThat(byOrgX).hasSize(1).contains(e1);

        List<Event> byNotExist = eventRepository.findByOrganizer("Nope");
        assertThat(byNotExist).isEmpty();
    }

    @Test
    void testDeleteAndFindAll() {
        Event e1 = new Event();
        e1.setId(UUID.randomUUID().toString());
        e1.setTitle("X"); e1.setDescription("D");
        e1.setDate("2025-07-01"); e1.setLocation("L");
        e1.setPrice(50.0); e1.setOrganizer("OX");

        eventRepository.save(e1);
        boolean deleted = eventRepository.existsById(e1.getId());
        eventRepository.deleteById(e1.getId());

        assertThat(deleted).isTrue();
        assertThat(eventRepository.findById(e1.getId())).isNotPresent();
        assertThat(eventRepository.findAll()).isEmpty();
    }
}
