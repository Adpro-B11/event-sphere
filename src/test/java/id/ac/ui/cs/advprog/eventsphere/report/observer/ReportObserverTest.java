//package id.ac.ui.cs.advprog.eventsphere.report.observer;
//
//import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
//import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
//import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//
//class ReportObserverTest {
//
//    private Report report;
//    private ReportObserver observer;
//
//    @BeforeEach
//    void setUp() {
//        report = new Report(
//                UUID.randomUUID().toString(),
//                UUID.randomUUID().toString(),
//                UUID.randomUUID().toString(),
//                UUID.randomUUID().toString(),
//                "Test description",
//                ReportCategory.TICKET.getValue(),
//                ReportStatus.ON_PROGRESS.getValue(),
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                Arrays.asList("url1", "url2"),
//                "Initial response"
//        );
//
//        // Create a mock observer
//        observer = Mockito.mock(ReportObserver.class);
//        report.addObserver(observer);
//    }
//
//    @Test
//    void shouldNotifyObserversWhenStatusChanges() {
//        report.setStatus(ReportStatus.RESOLVED.getValue());
//
//        verify(observer, times(1)).update(report);
//    }
//
//    @Test
//    void shouldNotNotifyObserversWhenStatusDoesNotChange() {
//        report.setStatus(ReportStatus.ON_PROGRESS.getValue());
//
//        verify(observer, never()).update(report);
//    }
//
//    @Test
//    void shouldNotifyObserversWhenResponseMessageChanges() {
//        report.setResponseMessage("New response message");
//
//        verify(observer, times(1)).update(report);
//    }
//
//    @Test
//    void shouldNotNotifyObserversWhenResponseMessageDoesNotChange() {
//        report.setResponseMessage("Initial response"); // Same message
//
//        verify(observer, never()).update(report);
//    }
//
//    @Test
//    void shouldNotifyObserversWhenResolvingReport() {
//        report.resolveReport("Resolution message");
//
//        verify(observer, times(1)).update(report);
//    }
//
//    @Test
//    void shouldRemoveObserver() {
//        report.removeObserver(observer);
//        report.setStatus(ReportStatus.RESOLVED.getValue());
//
//        verify(observer, never()).update(report);
//    }
//}