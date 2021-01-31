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
public class CreateAdRequestDto {

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    private String description;

    @NotBlank
    private String category;

    @Positive
    private BigDecimal price;

    @NotNull
    private Long mobileCustomerId;

}
