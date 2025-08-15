package dev.enricosola.porcellino.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import dev.enricosola.porcellino.enums.TransactionType;
import org.hibernate.annotations.Generated;
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
@EntityListeners(AuditingEntityListener.class)
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
    @Generated
    private Date date;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;
}
