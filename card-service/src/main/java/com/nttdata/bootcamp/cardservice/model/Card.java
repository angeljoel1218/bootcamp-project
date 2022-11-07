package com.nttdata.bootcamp.cardservice.model;

import com.nttdata.bootcamp.cardservice.model.constant.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 *
 * @since 2022
 */

@Document("cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    private String id;
    private String number;
    private Date expirationDate;
    private String cvv;
    private CardType cardType;
    private String holderId;
    private List<BankAccount> accounts;
}
