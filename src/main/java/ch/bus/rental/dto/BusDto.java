package ch.bus.rental.dto;

import ch.bus.rental.entity.BusDbo;
import lombok.Data;

@Data
public class BusDto {
    private Long id;
    private String name;

    public BusDto mapTo(BusDbo dbo) {
        this.id = dbo.getId();
        this.name = dbo.getName();
        return this;
    }
}
