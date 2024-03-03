package dev.enricosola.porcellino.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.io.Serial;
import java.util.Date;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@RequiredArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -2090355125855192549L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonIgnore
    private Date updatedAt;
}
