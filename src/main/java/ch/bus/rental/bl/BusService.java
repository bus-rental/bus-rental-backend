package ch.bus.rental.bl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.bus.rental.dto.BusDto;
import ch.bus.rental.repository.BusRepository;

@Component
public class BusService {
    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<BusDto> all() {
        return busRepository.findAll().stream().map(dbo -> new BusDto().mapTo(dbo)).toList();
    }
}
