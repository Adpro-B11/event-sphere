package id.ac.ui.cs.advprog.eventsphere.event.command;

import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;

public class DeleteEventCommand implements EventCommand {
    private final EventService eventService;
    private final String eventId;

    public DeleteEventCommand(EventService eventService, String eventId) {
    }

    @Override
    public void execute() {
    }
}
