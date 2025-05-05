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
@Table(name = "client_trackings")
@RequiredArgsConstructor
@Entity
@Getter
@Setter
public class ClientTracking implements Serializable {
    @Serial
    private static final long serialVersionUID = -4139484237567936581L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ref_name")
    private String refName;

    @Column(name = "ref_id")
    private int refId;

    @Column(name = "ip_address")
    private String IPAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "browser_name")
    private String browserName;

    @Column(name = "browser_version")
    private String browserVersion;

    @Column(name = "os_name")
    private String OSName;

    @Column(name = "os_version")
    private String OSVersion;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;
}
