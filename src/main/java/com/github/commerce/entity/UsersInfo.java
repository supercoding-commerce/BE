package com.github.commerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_infos")
public class UsersInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @Size(max = 10)
    @Column(name = "grade")
    private String grade;

    @Size(max = 10)
    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 4)
    @NotNull
    @Column(name = "age", nullable = false)
    private String age;

    @Size(max = 255)
    @Column(name = "nickname")
    private String nickname;

}