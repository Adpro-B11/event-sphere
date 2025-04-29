package id.ac.ui.cs.advprog.eventsphere.event.web;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ui/events")
public class EventWebController {

    private final EventService eventService;

    public EventWebController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String listEvents(Model model) {

    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {

    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute Event event) {

    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {

    }

    @PostMapping("/{id}/edit")
    public String submitEdit(@PathVariable String id, @ModelAttribute Event event) {

    }

    @GetMapping("/{id}/delete")
    public String deleteEvent(@PathVariable String id) {

    }
}
