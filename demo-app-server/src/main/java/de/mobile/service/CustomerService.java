package de.mobile.service;

import de.mobile.api.exception.BadRequestException;
import de.mobile.dto.CustomerDto;
import de.mobile.entity.MobileCustomer;
import de.mobile.repository.MobileCustomerRepository;
import de.mobile.api.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerService {

    private final MobileCustomerRepository mobileCustomerRepository;

    private final ModelMapper modelMapper;

    @Inject
    public CustomerService(MobileCustomerRepository mobileCustomerRepository, ModelMapper modelMapper) {
        this.mobileCustomerRepository = mobileCustomerRepository;
        this.modelMapper = modelMapper;
    }

    public Long create(CustomerDto customerDto) {
        final MobileCustomer ad = inbound(customerDto);
        return mobileCustomerRepository.create(ad);
    }

    public CustomerDto get(Long id) {
        final MobileCustomer customer = mobileCustomerRepository.get(id);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer", "id", id);
        }
        return outbound(customer);
    }

    public List<CustomerDto> list() {
        return mobileCustomerRepository.list()
                .stream()
                .map(this::outbound)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        boolean deleted = mobileCustomerRepository.delete(id);
        if (!deleted) {
            throw new BadRequestException("Could not delete Customer with id: " + id);
        }
    }

    private CustomerDto outbound(MobileCustomer mobileCustomer) {
        return modelMapper.map(mobileCustomer, CustomerDto.class);
    }

    private MobileCustomer inbound(CustomerDto customerDto) {
        return modelMapper.map(customerDto, MobileCustomer.class);
    }

}
