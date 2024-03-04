package pl.clinic.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {
    @InjectMocks
    private HomeController homeController;

    @Test
    void homePage() {
        //given, when
        String result = homeController.homePage();
        //then
        assertEquals("home", result);
    }

    @Test
    void getSuccessMessage() {
        //given, when
        String result = homeController.getSuccessMessage();

        assertEquals("successMessage", result);

    }
}