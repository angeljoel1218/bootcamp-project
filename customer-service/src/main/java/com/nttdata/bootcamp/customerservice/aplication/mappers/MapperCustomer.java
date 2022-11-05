package com.nttdata.bootcamp.customerservice.aplication.mappers;



import com.nttdata.bootcamp.customerservice.model.Customer;
import com.nttdata.bootcamp.customerservice.model.constants.TypeCustomer;
import com.nttdata.bootcamp.customerservice.model.constants.TypeProfile;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */

@Component
public class MapperCustomer {

  public CustomerDto toDto(Customer customer) {
    ModelMapper modelMapper = new ModelMapper();
    CustomerDto dto = modelMapper.map(customer, CustomerDto.class);
    dto.setItsVip(Objects.equals(TypeProfile.VIP, dto.getTypeProfile()));
    dto.setItsPyme(Objects.equals(TypeProfile.PYME, dto.getTypeProfile()));
    dto.setItsCompany(Objects.equals(TypeCustomer.COMPANY, dto.getTypeCustomer()));
    dto.setItsPersonal(Objects.equals(TypeCustomer.PERSONAL, dto.getTypeCustomer()));
    return dto;
  }


  public Customer toCustomer(CustomerDto  customerDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(customerDto, Customer.class);
  }
}
