package com.cairone.core.service;

import com.cairone.core.converter.CustomerConverter;
import com.cairone.core.resource.CustomerResource;
import com.cairone.data.collection.CustomerDoc;
import com.cairone.data.collection.customer.AddressEmbedded;
import com.cairone.data.collection.CityDoc;
import com.cairone.data.collection.repository.CityRepository;
import com.cairone.data.collection.repository.CustomerRepository;
import com.cairone.error.AppClientException;
import com.cairone.rest.request.CustomerRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CityRepository cityRepository;
    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;

    public CustomerResource save(CustomerRequest request, UUID createdBy) {

        AddressEmbedded address = AddressEmbedded.builder()
                .withStreet(request.getMainLocation().getStreet())
                .withNumber(request.getMainLocation().getNumber())
                .withZipCode(request.getMainLocation().getZipCode())
                .build();

        CityDoc cityDoc = cityRepository.findById(new ObjectId(request.getCityId()))
                .orElseThrow(() -> new AppClientException("City not found"));

        CustomerDoc customerDoc = CustomerDoc.builder()
                .withName(request.getName())
                .withDescription(request.getDescription())
                .withMainLocation(address)
                .withCity(cityDoc)
                .withPhone(request.getPhone())
                .withCreatedBy(createdBy)
                .build();

        CustomerDoc saved = customerRepository.save(customerDoc);

        return customerConverter.convert(saved);
    }
}
