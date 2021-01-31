package de.mobile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MobileAd {

    private Long id;

    private String make;

    private String model;

    private String description;

    private Category category;

    private BigDecimal price;

    private Long mobileCustomerId;

    private long createTime;

    private long updateTime;

}
