package pl.clinic.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.business.dao.SpecializationDAO;
import pl.clinic.domain.Specialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceTest {
    @InjectMocks
    private SpecializationService specializationService;
    @Mock
    private SpecializationDAO specializationDAO;
    @Test
    void testFindAll() {
        // given
        List<Specialization> expectedSpecializations = new ArrayList<>();
        expectedSpecializations.add(Specialization.builder().name("specialization1").build());
        expectedSpecializations.add(Specialization.builder().name("specialization2").build());
        when(specializationDAO.findAll()).thenReturn(expectedSpecializations);

        // when
        List<Specialization> result = specializationService.findAll();

        // then
        assertEquals(expectedSpecializations, result);
    }

    @Test
    void testIsNewSpecialization() {
        //given
        Specialization someSpecialization = Specialization.builder()
                .specializationId(1).name("someSpecialization").build();
        when(specializationDAO.findBySpecializationName(someSpecialization.getName())).thenReturn(Optional.of(someSpecialization));

        //when
        Boolean isNew = specializationService.isNewSpecialization(someSpecialization);

        //then

        assertFalse(isNew);

    }

    @Test
    void testSave() {
        // given
        String specializationName = "New Specialization";

        // when
        specializationService.save(specializationName);

        // then
        Mockito.verify(specializationDAO).save(specializationName);
    }
}