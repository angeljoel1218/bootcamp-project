package com.nttdata.bootcamp.reportservice.controller;

import com.nttdata.bootcamp.reportservice.application.ReportSevice;
import com.nttdata.bootcamp.reportservice.model.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportController {

    @Autowired
    ReportSevice reportSevice;

    @GetMapping("report/customer/{customerId}")
    public Flux<Map<String,Object>>  dailyBalance(@PathVariable("customerId") String customerId){
        return  reportSevice.dailyBalance(customerId).map(t->{
                Map<String,Object> map= new HashMap<>();

                map.put("Name",t.getNumber());
                map.put("Dias",reportSevice.dailyBalance2(t.getId()));


                return map  ;
        });
    }


}
