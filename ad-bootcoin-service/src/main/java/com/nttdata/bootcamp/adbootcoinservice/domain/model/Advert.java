package com.nttdata.bootcamp.adbootcoinservice.domain.model;

import com.nttdata.bootcamp.adbootcoinservice.domain.constant.StateAdvert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @since 2022
 */
@Document(collection = "advertisements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advert {
    @Id
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
