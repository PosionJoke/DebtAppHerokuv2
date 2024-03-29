package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DebtorHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal debt = new BigDecimal(0);
    private long timeOfDebt;
    private String reasonForTheDebt;
    private String userName;

    @Override
    public String toString() {
        return "DebtorHistory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", debt=" + debt +
                ", timeOfDebt=" + timeOfDebt +
                ", reasonForTheDebt='" + reasonForTheDebt + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
