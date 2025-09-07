package com.patria.apps.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patria.apps.vo.StatusTransactionEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_generator")
    @SequenceGenerator(name = "transactions_generator", sequenceName = "transactions_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "invoice_num")
    private String invoiceNum;

    private int qty;

    private Long total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusTransactionEnum status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users users;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    @JsonIgnore
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    @JsonIgnore
    private UsersAddress usersAddress;

    @ManyToOne
    @JoinColumn(name = "expedition_id", nullable = false)
    @JsonIgnore
    private Expedition expedition;

    @OneToMany(mappedBy = "transactions", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExpeditionHistory> expeditionHistory;

    @OneToOne(mappedBy = "transactions", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProductReview productReview;

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
