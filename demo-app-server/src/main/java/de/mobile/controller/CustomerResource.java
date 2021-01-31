package de.mobile.controller;

import de.mobile.dto.CustomerDto;
import de.mobile.service.CustomerService;
import de.mobile.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("customers")
public class CustomerResource {

    private final CustomerService customerService;

    @Inject
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("{id}")
    public CustomerDto get(@PathVariable("id") Long id) {
        return customerService.get(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CustomerDto customerDto) {
        final Long newCustomerId = customerService.create(customerDto);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCustomerId).toUri();

        log.info("Customer id: {} created successfully", newCustomerId);
        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, String.format("Customer with id: %s created successfully", newCustomerId)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        log.info("Customer id: {} deleted successfully", id);
        return ResponseEntity
                .ok(new ApiResponse(true, "Customer deleted successfully"));
    }

    @GetMapping
    public List<CustomerDto> list() {
        log.info("Get all customers");
        return customerService.list();
    }
}
