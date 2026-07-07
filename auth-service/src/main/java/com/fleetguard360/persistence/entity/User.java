package com.fleetguard360.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "app_user")
public class User {
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    private String lastname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(name = "disable", nullable = false)
    private boolean disabled;
    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired;
    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked;
    @Column(name = "credential_expired", nullable = false)
    private boolean credentialExpired;

    @OneToOne
    @JoinColumn(name = "type_of_id", referencedColumnName = "id")
    private UserIdType idType;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
