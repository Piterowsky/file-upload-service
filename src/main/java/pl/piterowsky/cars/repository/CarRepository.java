package pl.piterowsky.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.piterowsky.cars.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}
