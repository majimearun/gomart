package com.ecommerce.gomart.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@AttributeOverrides(
        @AttributeOverride(
                name = "managerPerms",
                column = @Column(name = "manager_perms")
        )
)
public class Manager {
    @Column(
            name = "manager_perms",
            nullable = false,
            columnDefinition = "boolean default false"

    )
    boolean managerPerms;
}
