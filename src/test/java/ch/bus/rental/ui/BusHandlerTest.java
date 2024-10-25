package ch.bus.rental.ui;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.bus.rental.bl.BusService;
import ch.bus.rental.config.GlobalExceptionHandler;
import ch.bus.rental.dto.BusDto;

@ExtendWith(MockitoExtension.class)
class BusHandlerTest {

    @Mock
    private BusService busService;

    private MockMvc mockMvc;
    private BusHandler busHandler;

    @BeforeEach
    void setUp() {
        busHandler = new BusHandler(busService);
        mockMvc = MockMvcBuilders.standaloneSetup(busHandler)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("GET /bus Tests")
    class GetAllTests {

        @Test
        @DisplayName("Should return empty list when no buses exist")
        void shouldReturnEmptyListWhenNoBusesExist() throws Exception {
            // Arrange
            when(busService.all()).thenReturn(Collections.emptyList());

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/bus")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("[]"));

            Mockito.verify(busService).all();
        }

        @Test
        @DisplayName("Should return list of buses when buses exist")
        void shouldReturnListOfBusesWhenBusesExist() throws Exception {
            // Arrange
            BusDto bus1 = new BusDto();
            bus1.setId(1L);
            bus1.setName("Bus 1");

            BusDto bus2 = new BusDto();
            bus2.setId(2L);
            bus2.setName("Bus 2");

            when(busService.all()).thenReturn(Arrays.asList(bus1, bus2));

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/bus")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(
                            "[{\"id\":1,\"name\":\"Bus 1\"},{\"id\":2,\"name\":\"Bus 2\"}]"
                    ));

            Mockito.verify(busService).all();
        }

        @Test
        @DisplayName("Should handle service exception gracefully")
        void shouldHandleServiceExceptionGracefully() throws Exception {
            // Arrange
            when(busService.all()).thenThrow(new RuntimeException("Service error"));

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/bus")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError());

            Mockito.verify(busService).all();
        }
    }
}
