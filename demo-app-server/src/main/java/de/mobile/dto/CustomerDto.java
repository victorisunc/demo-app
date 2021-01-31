package de.mobile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String lastName;

    private String companyName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    private long createTime;

    private long updateTime;

}
