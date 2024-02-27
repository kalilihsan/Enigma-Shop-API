package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ConstantTable.CUSTOMER)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_phone_no")
    private String mobilePhoneNo;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(name = "status")
    private Boolean status;
}
