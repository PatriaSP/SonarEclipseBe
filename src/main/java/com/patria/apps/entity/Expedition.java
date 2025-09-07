package com.patria.apps.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@Entity
@Table (name="expedition")
@AllArgsConstructor
@NoArgsConstructor
public class Expedition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expedition_generator")
    @SequenceGenerator(name = "expedition_generator", sequenceName = "expedition_id_seq", allocationSize = 1)
    private Long id;
    
    @Column(name="expedition_name")
    private String expeditionName;
    
    private double price;
    
    @OneToMany(mappedBy = "expedition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transactions> transactions;
    
    @OneToMany(mappedBy = "expedition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExpeditionHistory> expeditionHistory;

     /**
     * Timestampz and Blames
     */
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
