package de.mobile.repository;

import de.mobile.entity.MobileCustomer;

import java.util.List;

public interface MobileCustomerRepository {

    Long create(MobileCustomer customer);

    MobileCustomer get(Long id);

    boolean delete(Long id);

    List<MobileCustomer> list();

}
