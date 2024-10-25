package ch.bus.rental.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.bus.rental.bl.BusService;
import ch.bus.rental.dto.BusDto;


@RestController
@RequestMapping("/bus")
public class BusHandler {
    private final BusService busService;

    @Autowired
    public BusHandler(BusService busService) {
        this.busService = busService;
    }

    @GetMapping()
    public ResponseEntity<List<BusDto>> all() {
        return ResponseEntity.ok(busService.all());
    }
    

}
