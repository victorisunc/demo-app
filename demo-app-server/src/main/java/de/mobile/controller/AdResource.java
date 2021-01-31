package de.mobile.controller;

import de.mobile.dto.AdDto;
import de.mobile.dto.CreateAdRequestDto;
import de.mobile.service.AdService;
import de.mobile.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("ads") // plural, as in resources, is more restful
public class AdResource {

    private final AdService adService;

    @Inject
    public AdResource(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("{id}")
    public AdDto get(@PathVariable("id") Long adId) {
        log.info("Getting ad by id: {}", adId);
        return adService.get(adId);
    }

    @GetMapping
    public List<AdDto> list() {
        log.info("Get all ads");
        return adService.list();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateAdRequestDto ad) {
        final Long newAdId = adService.create(ad);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newAdId).toUri();

        log.info("Ad id: {} created successfully", newAdId);
        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, String.format("Ad with id: %s created successfully", newAdId)));
    }

}
