package com.nttdata.bootcamp.walletservice.application.mappers;


import com.nttdata.bootcamp.walletservice.model.Wallet;
import com.nttdata.bootcamp.walletservice.model.dto.WalletDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 * Some javadoc.
 *
 * @since 2022
 */

@Component
public class MapperWallet {

  public WalletDto toDto(Wallet wallet) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(wallet, WalletDto.class);
  }


  public Wallet toWallet(WalletDto  walletDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(walletDto, Wallet.class);
  }
}
