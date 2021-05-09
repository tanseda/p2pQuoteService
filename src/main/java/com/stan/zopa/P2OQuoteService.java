package com.stan.zopa;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.stan.zopa.market.LenderService;
import com.stan.zopa.market.LenderServiceImpl;
import com.stan.zopa.quote.Quote;
import com.stan.zopa.quote.QuoteService;
import com.stan.zopa.quote.QuoteServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class P2OQuoteService {
    private final static String CURRENCY = "£";

    private final QuoteService quoteService;

    public static void main(String[] args) {
        if (args.length > 0) {
            int loanAmount;
            try {
                loanAmount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("Please enter valid amount");
                return;
            }

            Injector injector = Guice.createInjector(new BasicModule());
            QuoteServiceImpl quoteService = injector.getInstance(QuoteServiceImpl.class);

            P2OQuoteService p2OQuoteService = new P2OQuoteService(quoteService);

            p2OQuoteService. calculateQuote(loanAmount);

        } else {
            System.out.println("Please enter amount");
        }
    }

    public void calculateQuote(int loanAmount){
        quoteService.getQuote(loanAmount, 36)
                .ifPresentOrElse(this::logQuote, this::logQuoteUnavailable);
    }

    private void logQuote(Quote quote) {
        System.out.printf("Requested amount: %s%d\n", CURRENCY, quote.getLoanAmount());
        System.out.printf("Annual Interest Rate: %.1f%s\n", quote.getInterestRate() * 100, "%");
        System.out.printf("Monthly repayment: %s%.2f\n", CURRENCY, quote.getMonthlyPayment());
        System.out.printf("Total repayment: %s%.2f\n", CURRENCY, quote.getTotalRepayment());
    }

    private void logQuoteUnavailable() {
        System.out.println("It is not possible to provide a quote.");
    }

    public static class BasicModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(LenderService.class).to(LenderServiceImpl.class);
            bind(QuoteService.class).to(QuoteServiceImpl.class);
        }
    }
}
