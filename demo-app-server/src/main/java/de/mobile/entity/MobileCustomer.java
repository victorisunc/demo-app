package de.mobile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MobileCustomer {

    private Long id;

    private String firstName;

    private String lastName;

    private String companyName;

    private String email;

    private long createTime;

    private long updateTime;

}
