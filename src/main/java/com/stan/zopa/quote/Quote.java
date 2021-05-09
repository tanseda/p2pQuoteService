package com.stan.zopa.quote;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Quote {
    int loanAmount;
    int numberOfPayments;
    double interestRate;
    double monthlyPayment;
    double totalRepayment;
}
