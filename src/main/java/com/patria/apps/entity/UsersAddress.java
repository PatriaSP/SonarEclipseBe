package com.patria.apps.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table (name="users_address")
@AllArgsConstructor
@NoArgsConstructor
public class UsersAddress{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_address_generator")
    @SequenceGenerator(name = "users_address_generator", sequenceName = "users_address_id_seq", allocationSize = 1)
    private Long id;
    
    private String address;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users users;
    
    @OneToMany(mappedBy = "usersAddress", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transactions> transactions;
}
