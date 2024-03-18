package dev.enricosola.porcellino.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.io.Serial;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "currencies", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code")
})
@Getter
@Setter
@RequiredArgsConstructor
public class Currency implements Serializable {
    @Serial
    private static final long serialVersionUID = 2267957486066191346L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currency")
    @JsonBackReference
    private Set<Portfolio> portfolios;
}
