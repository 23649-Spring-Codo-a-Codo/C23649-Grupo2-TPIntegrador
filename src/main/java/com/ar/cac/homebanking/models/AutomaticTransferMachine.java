package com.ar.cac.homebanking.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AutomaticTransferMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "automaticTransferMachine_id")
    private Long id;

    private Long originMachine;

    private Long target;

    private Date date;

    private BigDecimal amount;
}
