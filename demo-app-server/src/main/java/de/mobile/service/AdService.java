package de.mobile.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import de.mobile.dto.AdDto;
import de.mobile.dto.CreateAdRequestDto;
import de.mobile.dto.CustomerDto;
import de.mobile.entity.Category;
import de.mobile.entity.MobileAd;
import de.mobile.repository.MobileAdRepository;
import de.mobile.api.exception.BadRequestException;
import de.mobile.api.exception.ResourceNotFoundException;
import de.mobile.repository.MobileAdRepositoryDBImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AdService {

    private final MobileAdRepository mobileAdRepository;
    private final CustomerService customerService;

    private final ModelMapper modelMapper;

    @Inject
    public AdService(MobileAdRepository mobileAdRepository,
                     CustomerService customerService,
                     ModelMapper modelMapper) {
        this.mobileAdRepository = mobileAdRepository;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    public Long create(CreateAdRequestDto adRequest) {
        CustomerDto customer = customerService.get(adRequest.getMobileCustomerId());
        if (customer == null) {
            throw new BadRequestException("Ad is not assigned to a valid customer id: " + adRequest.getMobileCustomerId());
        }
        MobileAd mobileAd = inbound(adRequest);
        return mobileAdRepository.create(mobileAd);
    }

    public AdDto get(Long id) {
        final MobileAd mobileAd = mobileAdRepository.get(id);
        if (mobileAd == null) {
            throw new ResourceNotFoundException("Ad", "id", id);
        }
        return outbound(mobileAd);
    }

    public List<AdDto> list() {
        return mobileAdRepository.list()
                .stream()
                .map(this::outbound)
                .collect(Collectors.toList());
    }

    private AdDto outbound(MobileAd mobileAd) {
        AdDto adDto = modelMapper.map(mobileAd, AdDto.class);
        CustomerDto customer = customerService.get(mobileAd.getMobileCustomerId());
        adDto.setCustomer(customer);
        return adDto;
    }

    private MobileAd inbound(CreateAdRequestDto adRequest) {
        final MobileAd ad = modelMapper.map(adRequest, MobileAd.class);
        if (ad.getCategory() == null) {
            // Category is an ENUM, if it's null it means modelMapper couldn't map the String into one of the enum values
            // We can also implement a custom ConstraintValidator and use it as annotation.
            throw new BadRequestException("Category is invalid. Valid values are: " + Arrays.asList(Category.values()));
        }
        return ad;
    }

}
