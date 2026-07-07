package com.fleetguard360.persistence.entity;

import com.fleetguard360.persistence.entity.enums.UserIdTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "type_of_id")
public class UserIdType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id_type")
    private UserIdTypeEnum userIdType;

    @OneToOne(mappedBy = "idType")
    private User user;
}
