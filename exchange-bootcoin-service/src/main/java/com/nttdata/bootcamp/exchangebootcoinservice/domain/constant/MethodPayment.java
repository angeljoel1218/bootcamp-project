package com.nttdata.bootcamp.exchangebootcoinservice.domain.constant;

public enum MethodPayment {
    YANKI("YANKI-PAYMENT-ID"),
    TRANSFER("TRANSFER-PAYMENT-ID");
    private String action;

    public String getAction()
    {
        return this.action;
    }

    private MethodPayment(String action)
    {
        this.action = action;
    }
}
