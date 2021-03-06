package com.example.study.model.entity;

import com.example.study.model.enumclass.OrderType;
import com.example.study.model.enumclass.PaymentType;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "orderDetailList"})
@EntityListeners(AuditingEntityListener.class)
@Builder
@Accessors(chain = true)
public class OrderGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; //

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // 주문 형태 - 일괄 / 개별

    private String revAddress;
    private String revName;

    // @Enumerated(EnumType.STRING)
    private String paymentType; // 카드 / 현금

    private BigDecimal totalPrice;
    private Integer totalQuantity;
    private LocalDateTime orderAt;
    private LocalDateTime arrivalDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    // OrderGroup N : 1 User
    // User변수는 UserEntitiy의 mappedBy와 일치해야 한다.
    @ManyToOne
    private User user;

    // OrderGroup 1 : OrderDetail N
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderGroup")
    private List<OrderDetail> orderDetailList;

}