package com.nttdata.bootcamp.customerservice.aplication.mappers;

import com.nttdata.bootcamp.customerservice.model.Customer;
import com.nttdata.bootcamp.customerservice.model.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.TypeProfile;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.modelmapper.ModelMapper;

import java.util.Objects;

@Component
public class MapperCustomer {
    public CustomerDto toDto(Customer customer) {

        ModelMapper modelMapper = new ModelMapper();
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        customerDto.setItsVip(Objects.equals(TypeProfile.VIP,customerDto.getTypeCustomer()));
        customerDto.setItsPyme(Objects.equals(TypeProfile.PYME,customerDto.getTypeProfile()));
        customerDto.setItsCompany(Objects.equals(TypeCustomer.COMPANY,customerDto.getTypeCustomer()));
        customerDto.setItsPersonal(Objects.equals(TypeCustomer.PERSONAL,customerDto.getTypeCustomer()));

        return customerDto;
    }

    public Customer toCustomer(CustomerDto  customerDto) {
        ModelMapper modelMapper = new ModelMapper();
        Customer customer = modelMapper.map(customerDto, Customer.class);
        return customer;
    }
}
