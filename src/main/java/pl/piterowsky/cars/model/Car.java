package pl.piterowsky.cars.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Car {

    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Date transactionDate;

    @NotNull
    private String color;

}
