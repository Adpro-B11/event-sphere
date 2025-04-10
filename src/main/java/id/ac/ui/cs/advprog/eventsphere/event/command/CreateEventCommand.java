// src/main/java/id/ac/ui/cs/advprog/eventsphere/event/command/CreateEventCommand.java
package id.ac.ui.cs.advprog.eventsphere.event.command;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;

public class CreateEventCommand implements EventCommand {
    private final EventService eventService;
    private final Event event;

    public CreateEventCommand(EventService eventService, Event event) {
        this.eventService = eventService;
        this.event = event;
    }

    @Override
    public void execute() {
        eventService.createEvent(event);
    }
}