package com.evm.oauth2.infrastructure.dto.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TableDev", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class TableDev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", length = 50)
    private String surname;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
