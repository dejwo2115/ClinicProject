package pl.clinic.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.clinic.integration.configuration.AbstractIT;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeControllerIT extends AbstractIT {

    private final TestRestTemplate testRestTemplate;

    @Test
    void thatHomePageRequiredSigningIn() {
        String url = String.format("http://localhost:%s%s", port, basePath);

        String page = this.testRestTemplate.getForObject(url, String.class);
        Assertions.assertThat(page).contains("Please sign in");
    }

    @Test
    void thatDoctorPageRequiredSigningIn() {
        String url = String.format("http://localhost:%s%s/doctor", port, basePath);

        String page = this.testRestTemplate.getForObject(url, String.class);
        Assertions.assertThat(page).contains("Please sign in");
    }
    @Test
    void thatPatientPageRequiredSigningIn() {
        String url = String.format("http://localhost:%s%s/patient", port, basePath);

        String page = this.testRestTemplate.getForObject(url, String.class);
        Assertions.assertThat(page).contains("Please sign in");
    }
}
