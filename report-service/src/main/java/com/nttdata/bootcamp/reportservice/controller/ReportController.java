package com.nttdata.bootcamp.reportservice.controller;

import com.nttdata.bootcamp.reportservice.application.ReportSevice;
import com.nttdata.bootcamp.reportservice.model.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report")
public class ReportController {

    @Autowired
    ReportSevice reportSevice;

    @GetMapping("/customer-balance/{customerId}")
    public ResponseEntity<Flux<Map<String, Object>>>  dailyBalance(@PathVariable("customerId") String customerId){
        return  ResponseEntity.ok(reportSevice.dailyBalance(customerId)) ;
    }
    @GetMapping(value = "/customer-commissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<Map<String, Object>>>  dailyBalance( @RequestParam String customerId,
                                                                    @RequestParam Date startDate,
                                                                    @RequestParam Date endDate){
        return  ResponseEntity.ok(reportSevice.productsCommissionByDates(startDate,endDate,customerId)) ;
    }
}
