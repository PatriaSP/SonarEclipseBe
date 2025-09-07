package com.patria.apps.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@Entity
@Table (name="expedition_history")
@AllArgsConstructor
@NoArgsConstructor
public class ExpeditionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expedition_history_generator")
    @SequenceGenerator(name = "expedition_history_generator", sequenceName = "expedition_history_id_seq", allocationSize = 1)
    private Long id;
    
    private String detail;

    @ManyToOne
    @JoinColumn(name = "expedition_id", nullable = false)
    @JsonIgnore
    private Expedition expedition;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonIgnore
    private Transactions transactions;
    
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
