package pl.bykowski.rectangleapp.repositories.repo_struct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
//Use annotation @Entity to mark that class as database
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class
Debtor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float debt;
    private float totalDebt;
    private LocalDate date;
    private String dateNow;

    @Override
    public String toString() {
        return "Name -------> " + name + "\n" +
                "Total Debt -> " + totalDebt;
    }
}
