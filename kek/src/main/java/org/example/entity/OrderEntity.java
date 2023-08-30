package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.CollateralType;
import org.example.entity.enums.OrderStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_order", schema = "app2")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "return_date")
    private LocalDate returnDate;
    @Column(name = "collateral_data")
    @Enumerated(EnumType.STRING)
    private CollateralType collateralData;
    @Column(name = "collateral_data_value")
    private String collateralDataValue;
    @Column(name = "sum")
    private int sum;
    @ManyToOne
    @JoinColumn(name = "client_id_fk")
    private ClientEntity client;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @OneToMany(mappedBy = "order")
    @Fetch(FetchMode.JOIN)
    List<CdEntity> cdList;
}
