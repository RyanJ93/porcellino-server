package dev.enricosola.porcellino.entity;

import dev.enricosola.porcellino.enums.TransactionType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.io.Serial;
import java.util.Date;
import lombok.*;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@RequiredArgsConstructor
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 2834974322099975884L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Column(name = "amount")
    private double amount;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "note")
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}
