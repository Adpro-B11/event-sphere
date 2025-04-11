// src/main/java/id/ac/ui/cs/advprog/eventsphere/event/command/UpdateStatusCommand.java
package id.ac.ui.cs.advprog.eventsphere.event.command;

import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;

public class UpdateStatusCommand implements EventCommand {
    private final EventService eventService;
    private final String eventId;
    private final String status;

    public UpdateStatusCommand(EventService eventService, String eventId, String status) {
        this.eventService = eventService;
        this.eventId = eventId;
        this.status = status;
    }

    @Override
    public void execute() {
        eventService.updateStatus(eventId, status);
    }
}