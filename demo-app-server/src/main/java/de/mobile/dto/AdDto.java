package de.mobile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdDto {

    private Long id;

    private String make;

    private String model;

    private String description;

    private String category;

    private BigDecimal price;

    private CustomerDto customer;

    private long createTime;

    private long updateTime;

}
