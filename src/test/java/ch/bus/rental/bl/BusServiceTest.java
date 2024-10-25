package ch.bus.rental.bl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.bus.rental.dto.BusDto;
import ch.bus.rental.entity.BusDbo;
import ch.bus.rental.repository.BusRepository;

@ExtendWith(MockitoExtension.class)
class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    private BusService busService;

    @BeforeEach
    void setUp() {
        busService = new BusService(busRepository);
    }

    @Nested
    @DisplayName("all() Tests")
    class AllTests {

        @Test
        @DisplayName("Should return empty list when no buses exist")
        void shouldReturnEmptyListWhenNoBusesExist() {
            // Arrange
            Mockito.when(busRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<BusDto> result = busService.all();

            // Assert
            Assertions.assertTrue(result.isEmpty());
            Mockito.verify(busRepository).findAll();
        }

        @Test
        @DisplayName("Should return list of BusDto when buses exist")
        void shouldReturnListOfBusDtoWhenBusesExist() {
            // Arrange
            BusDbo bus1 = new BusDbo();
            bus1.setId(1L);
            bus1.setName("Bus 1");

            BusDbo bus2 = new BusDbo();
            bus2.setId(2L);
            bus2.setName("Bus 2");

            Mockito.when(busRepository.findAll()).thenReturn(Arrays.asList(bus1, bus2));

            // Act
            List<BusDto> result = busService.all();

            // Assert
            Assertions.assertEquals(2, result.size());
            Assertions.assertEquals(1L, result.get(0).getId());
            Assertions.assertEquals("Bus 1", result.get(0).getName());
            Assertions.assertEquals(2L, result.get(1).getId());
            Assertions.assertEquals("Bus 2", result.get(1).getName());
            Mockito.verify(busRepository).findAll();
        }
    }
}