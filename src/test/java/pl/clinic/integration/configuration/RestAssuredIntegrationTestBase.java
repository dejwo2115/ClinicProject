package pl.clinic.integration.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import pl.clinic.integration.support.AuthenticationTestSupport;
import pl.clinic.integration.support.ControllerTestSupport;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class RestAssuredIntegrationTestBase
    extends AbstractIT
    implements ControllerTestSupport, AuthenticationTestSupport {

    protected static WireMockServer wireMockServer;

    private String jSessionIdValue;

    @Autowired

    private ObjectMapper objectMapper;

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Test
    void contextLoaded() {
        assertThat(true).isTrue();
    }

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(
            wireMockConfig()
                .port(9999)
        );
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    void beforeEach() {
        jSessionIdValue = login("test_user", "test")
            .and()
            .cookie("JSESSIONID")
            .header(HttpHeaders.LOCATION, "http://localhost:%s%s/".formatted(port, basePath))
            .extract()
            .cookie("JSESSIONID");
    }

    @AfterEach
    void afterEach() {
        logout()
            .and()
            .cookie("JSESSIONID", "");
        jSessionIdValue = null;
        wireMockServer.resetAll();
    }

    public RequestSpecification requestSpecification() {
        return restAssuredBase()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie("JSESSIONID", jSessionIdValue);
    }

    public RequestSpecification requestSpecificationNoAuthentication() {
        return restAssuredBase();
    }

    private RequestSpecification restAssuredBase() {
        return RestAssured
            .given()
            .config(getConfig())
            .basePath(basePath)
            .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    private RestAssuredConfig getConfig() {
        return RestAssuredConfig.config()
            .objectMapperConfig(new ObjectMapperConfig()
                .jackson2ObjectMapperFactory((type, s) -> objectMapper));
    }
}
