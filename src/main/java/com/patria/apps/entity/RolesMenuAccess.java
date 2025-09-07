package com.patria.apps.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table (name="role_menu_access")
@AllArgsConstructor
@NoArgsConstructor
public class RolesMenuAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_menu_access")
    @SequenceGenerator(name = "role_menu_access_generator", sequenceName = "role_menu_access_id_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonIgnore
    private Menu menu;
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Roles roles;
}
