package dev.enricosola.porcellino.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.*;
import java.io.Serializable;
import java.io.Serial;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_tokens")
@RequiredArgsConstructor
@Entity
@Getter
@Setter
public class RefreshToken implements Serializable {
    @Serial
    private static final long serialVersionUID = -5512772657706728535L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "payload")
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at")
    private Date expiredAt;
}
