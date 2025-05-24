package id.ac.ui.cs.advprog.eventsphere.ticket.client;

import id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase.PaymentRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceClient {

    @Value("${payment.service.url}") // Example default URL, adjust as needed
    private String paymentServiceUrl;

    private final RestTemplate restTemplate;


    public PaymentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Add any other necessary headers, e.g., Authorization if required by payment service
            // headers.setBearerAuth("your-payment-service-token");

            HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);

            String url = paymentServiceUrl + "/api/transactions/purchase";

            ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    PaymentResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
            }
        } catch (Exception e) {
        }
        return null;
    }
}
