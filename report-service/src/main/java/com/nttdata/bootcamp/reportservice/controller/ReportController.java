package com.nttdata.bootcamp.reportservice.controller;

import com.nttdata.bootcamp.reportservice.application.ReportSevice;
import com.nttdata.bootcamp.reportservice.model.ProductConsumer;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDto;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("report")
public class ReportController {

    @Autowired
    ReportSevice reportSevice;

    @GetMapping("/customer-products/{customerId}")
    public ResponseEntity<Flux<Map<String, Object>>>  findProductsByCustomer(@PathVariable("customerId") String customerId){
        return  ResponseEntity.ok(reportSevice.findProductsByCustomer(customerId)) ;
    }

    @GetMapping("/customer-balance/{customerId}")
    public ResponseEntity<Flux<Map<String, Object>>>  dailyBalance(@PathVariable("customerId") String customerId){
        return  ResponseEntity.ok(reportSevice.dailyBalance(customerId)) ;
    }

    @GetMapping(value = "/customer-commissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<Mono<Map<String, Object>>>>  dailyBalance( @RequestParam  String customerId,
                                                                    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
                                                                    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate){
        return  ResponseEntity.ok(reportSevice.productsCommissionByDates(startDate,endDate,customerId)) ;
    }

    @GetMapping(value = "/customer-fin/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<CustomerDto>>  findCustomerById( @PathVariable("id") String id){
        return  ResponseEntity.ok(reportSevice.findCustomerById(id)) ;
    }

    @GetMapping(value = "/product-seller", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<ProductConsumer>>  findProductConsumerByDateBetween(@RequestParam  String idProduct,
                                                                                    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
                                                                                    @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate){
        return  ResponseEntity.ok(reportSevice.findProductConsumerByDateBetween(idProduct,startDate,endDate)) ;
    }


    @GetMapping(value = "/product-find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<CreditDto>>  find(@RequestParam  String idProduct,
                                                                             @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
                                                                             @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate){
        return  ResponseEntity.ok(reportSevice.find(idProduct,startDate,endDate)) ;
    }



}
