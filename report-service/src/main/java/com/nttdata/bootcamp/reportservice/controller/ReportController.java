package com.nttdata.bootcamp.reportservice.controller;

import com.nttdata.bootcamp.reportservice.application.ReportService;
import com.nttdata.bootcamp.reportservice.model.ProductConsumer;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

/**
 *
 * @since 2022
 */

public class ReportController {

  @Autowired
  ReportService reportService;

  /**
   * javadoc.
   * Resume products by customer ID
   *
   * @since 2022
   */
  @GetMapping("/customer-products/{customerId}")
  public ResponseEntity<Flux<Map<String, Object>>> findProductsByCustomer(
    @PathVariable("customerId") String customerId) {
    return ResponseEntity.ok(reportService.findProductsByCustomer(customerId));
  }

  /**
   * javadoc.
   * Resume products seller by dates
   *
   * @param idProduct product-service
   * @param startDate start date param
   * @param endDate   end date param
   * @since 2022
   */

  @GetMapping(value = "/product-seller", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<ProductConsumer>> findProductConsumerByDateBetween(
    @RequestParam String idProduct,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate) {

    return ResponseEntity.ok(
      reportService.findProductConsumerByDateBetween(idProduct,
        startDate,
        endDate));
  }

  @GetMapping("/customer-balance/{customerId}")
  public ResponseEntity<Flux<Map<String, Object>>> dailyBalance(
    @PathVariable("customerId") String customerId) {

    return ResponseEntity.ok(reportService.dailyBalance(customerId));
  }

  @GetMapping(value = "/customer-commissions", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Flux<Mono<Map<String, Object>>>> productsCommissionByDates(
    @RequestParam String customerId,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate) {

    return ResponseEntity.ok(reportService.productsCommissionByDates(startDate,
      endDate,
      customerId));

  }

}