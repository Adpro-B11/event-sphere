package id.ac.ui.cs.advprog.eventsphere.event.web;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/events")
public class EventWebController {

    private final EventService eventService;

    public EventWebController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvents());
        return "events";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute Event event) {
        eventService.createEvent(event);
        return "redirect:/ui/events";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Event existing = eventService.findById(id);
        model.addAttribute("event", existing);
        return "event-form";
    }

    @PostMapping("/{id}/edit")
    public String submitEdit(@PathVariable String id, @ModelAttribute Event event) {
        event.setId(id);
        eventService.updateEventInfo(id, event);
        return "redirect:/ui/events";
    }

    @GetMapping("/{id}/delete")
    public String deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return "redirect:/ui/events";
    }
}
