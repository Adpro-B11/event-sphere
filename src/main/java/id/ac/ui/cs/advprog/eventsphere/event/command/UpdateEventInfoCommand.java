package id.ac.ui.cs.advprog.eventsphere.event.command;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;

public class UpdateEventInfoCommand implements EventCommand {
    private final EventService eventService;
    private final String eventId;
    private final Event updatedEvent;

    public UpdateEventInfoCommand(EventService eventService, String eventId, Event updatedEvent) {

    }

    @Override
    public void execute() {

    }

}
