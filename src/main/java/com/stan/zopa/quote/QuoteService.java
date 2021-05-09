package com.stan.zopa.quote;

import java.util.Optional;

public interface QuoteService {
    Optional<Quote> getQuote(int loanAmount, int numberOfPayments);
}
