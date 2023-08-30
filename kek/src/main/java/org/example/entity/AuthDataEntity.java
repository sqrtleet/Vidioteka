package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.functions.EmployeeFunc;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auth_data", schema = "app2")
public class AuthDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "auth_login")
    private String login;
    @Column(name = "auth_password")
    private String password;
    @OneToMany(mappedBy = "authData")
    private List<EmployeeEntity> employees;
}
