package dev.enricosola.porcellino.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recovery_codes")
@Getter
@Setter
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecoveryCode implements Serializable {
    @Serial
    private static final long serialVersionUID = -4987033241126735379L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "code")
    private String code;

    @Transient
    private String plainTextCode;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "invalidated_at")
    private Date invalidatedAt;
}
