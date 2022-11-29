package com.ecommerce.gomart.GomartUser.Admin;

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
                name = "adminPerms",
                column = @Column(name = "admin_perms")
        )
)
public class Admin{
    @Column(
            name = "admin_perms",
            nullable = false,
            columnDefinition = "boolean default false"

    )
    boolean adminPerms;
}

