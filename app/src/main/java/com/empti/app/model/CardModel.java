package com.empti.app.model;

import com.stripe.android.Stripe;

public class CardModel {

    String cardNumber;
    String cvc;
    String expMonth;
    String expYear;

    public CardModel(String cardNumber, String cvc, String expMonth, String expYear) {
        this.cardNumber = cardNumber;
        this.cardNumber = cvc;
        this.expMonth = expMonth;
        this.expYear = expYear;


    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }


}
