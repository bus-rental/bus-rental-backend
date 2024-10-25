package ch.bus.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.bus.rental.entity.BusDbo;

public interface BusRepository extends JpaRepository<BusDbo, Long>  {

}
