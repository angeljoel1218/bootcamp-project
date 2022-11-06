package com.nttdata.bootcamp.productservice.serviceimpl;


import com.nttdata.bootcamp.productservice.application.ProductService;
import com.nttdata.bootcamp.productservice.application.ProductServiceImpl;
import com.nttdata.bootcamp.productservice.application.mappers.MapperProduct;
import com.nttdata.bootcamp.productservice.infrastructure.ProductRepository;
import com.nttdata.bootcamp.productservice.model.Product;
import com.nttdata.bootcamp.productservice.model.constant.Category;
import com.nttdata.bootcamp.productservice.model.constant.ProductType;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
class ProductServiceImplTests {

  @TestConfiguration
  static class ProductServiceImplTestsConfiguration {
    @Bean
    public ProductService productService() {
      return new ProductServiceImpl();
    }

    @Bean
    public MapperProduct mapperProduct(){
      return new MapperProduct();
    }

    @MockBean
    public ProductRepository productRepository;
  }

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;


  @Test
	void create() {
    Product product = Product.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();

    when(productRepository.insert(product)).thenReturn(Mono.just(product));

    ProductDto request = ProductDto.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();

    ProductDto response = ProductDto.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();



    StepVerifier.create(productService.create(request))
      .expectNext(response)
      .expectComplete()
      .verify();

  }


  @Test
  void update() {
    var id = "01505254514";

    ProductDto request = ProductDto.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();

    when(productRepository.findById(id)).thenReturn(Mono.just(new Product()));

    StepVerifier.create(productService.update(id, request))
      .expectError(RuntimeException.class)
      .verify();
  }



  @Test
  void delete() {
    var id = "70150585sdsds";
    when(productRepository.findById(id)).thenReturn(Mono.just(new Product()));

    StepVerifier.create(productService.delete(id))
      .expectError(RuntimeException.class)
      .verify();
  }

  @Test
  void findById() {
    var id="70150585sdsds";
    Product product = Product.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();

    when(productRepository.findById("70150585sdsds")).thenReturn(Mono.just(product));

    ProductDto response = ProductDto.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();

    StepVerifier.create(productService.findById(id))
      .expectNext(response)
      .expectComplete()
      .verify();
  }


  @Test
  void findAll() {

    Product productOne = Product.builder()
      .code("12345678")
      .name("cueta corriente")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.PASSIVE)
      .productTypeId(ProductType.CURRENT_ACCOUNT)
      .build();

    Product productTwo = Product.builder()
      .code("121222")
      .name("cueta credito")
      .maintenance(2F)
      .maxMovements(1)
      .maxNumberCredits(10)
      .commissionAmount(10F)
      .openingAmount(10f)
      .minFixedAmount(10F)
      .category(Category.ACTIVE)
      .productTypeId(ProductType.BUSINESS_CREDIT)
      .build();

    when(productRepository.findAll()).thenReturn(Flux.just(productOne, productTwo));


    StepVerifier.create(productService.findAll())
      .expectSubscription()
      .expectNext(
        ProductDto.builder()
          .code("12345678")
          .name("cueta corriente")
          .maintenance(2F)
          .maxMovements(1)
          .maxNumberCredits(10)
          .commissionAmount(10F)
          .openingAmount(10f)
          .minFixedAmount(10F)
          .category(Category.PASSIVE)
          .productTypeId(ProductType.CURRENT_ACCOUNT)
          .build()
      )
      .expectNext(
        ProductDto.builder()
          .code("121222")
          .name("cueta credito")
          .maintenance(2F)
          .maxMovements(1)
          .maxNumberCredits(10)
          .commissionAmount(10F)
          .openingAmount(10f)
          .minFixedAmount(10F)
          .category(Category.ACTIVE)
          .productTypeId(ProductType.BUSINESS_CREDIT)
          .build()
      ).verifyComplete();


  }


}
