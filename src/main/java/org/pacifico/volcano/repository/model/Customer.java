//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.repository.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//=================================================================================================
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
    @Column(name = "name", nullable = false, length = 1000)
    private String name;

    @Column(name = "email", nullable = false, length = 1000, unique = true)
    private String email;
}
