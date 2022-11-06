package com.nttdata.bootcamp.adbootcoinservice.domain.dto;

import com.nttdata.bootcamp.adbootcoinservice.domain.constant.StateAdvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */
@Data
public class AdvertDto {
    private String id;
    private String nickname;
    private BigDecimal amount;
    private BigDecimal maxAmount;
    private BigDecimal minAmount;
    private String walletId;
    private String methodPayment;
    private StateAdvert state;
    private Date createdAt;
}
