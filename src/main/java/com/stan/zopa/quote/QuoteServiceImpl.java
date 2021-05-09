package com.stan.zopa.quote;

import com.google.inject.Inject;
import com.stan.zopa.market.Lender;
import com.stan.zopa.market.LenderService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class QuoteServiceImpl implements QuoteService {

    private final int MIN_LOAN_AMOUNT = 1000;
    private final int MAX_LOAN_AMOUNT = 15000;
    private final int LOAN_AMOUNT_INCREMENT = 100;

    private final LenderService lenderService;

    @Inject
    public QuoteServiceImpl(LenderService lenderService){
        this.lenderService = lenderService;
    }

    public Optional<Quote> getQuote(int loanAmount, int numberOfPayments) {
        if (!isValid(loanAmount)) {
            return Optional.empty();
        }

        List<Lender> lenders = findLenders(loanAmount);
        if (lenders.isEmpty()) {
            return Optional.empty();
        }

        double totalRepayment = calculateTotalRepayment(lenders, numberOfPayments);
        double interestRate = calculateInterestRate(lenders, loanAmount);

        return Optional.of(Quote.builder()
                .loanAmount(loanAmount)
                .numberOfPayments(numberOfPayments)
                .monthlyPayment(totalRepayment / numberOfPayments)
                .interestRate(interestRate)
                .totalRepayment(totalRepayment)
                .build());
    }

    private List<Lender> findLenders(int loanAmount) {
        AtomicInteger remaining = new AtomicInteger(loanAmount);
        List<Lender> lenders = lenderService.getLendersOrderedByRate()
                .takeWhile(lender -> {
                    boolean take = remaining.get() > 0;
                    if (take) {
                        remaining.addAndGet(-lender.getAmount());
                    }
                    return take;
                })
                .collect(Collectors.toList());

        if (remaining.get() == 0) return lenders;
        if (remaining.get() > 0) return Collections.emptyList();

        Lender lender = lenders.remove(lenders.size() - 1);
        lenders.add(lender.withAmount(lender.getAmount() + remaining.get()));
        return (remaining.get() > 0)
                ? Collections.emptyList()
                : lenders;
    }

    private double calculateTotalRepayment(List<Lender> lenders, int numberOfPayments) {
        return lenders.stream()
                .map(lender -> this.calculateRepayment(numberOfPayments, lender.getAmount(), lender.getRate()))
                .reduce(0.0, Double::sum);
    }

    private double calculateRepayment(int numberOfPayments, int amount, double annualInterestRate) {
        double i = annualInterestRate / 12;
        double d = Math.pow(1 + i, numberOfPayments);
        return amount * ((i * d) / (d - 1)) * numberOfPayments;
    }

    private double calculateInterestRate(List<Lender> lenders, int amount) {
        return lenders.stream()
                .map(lender -> lender.getAmount() * lender.getRate() / amount)
                .reduce(0.0, Double::sum);
    }

    private boolean isValid(int loanAmount) {
        return loanAmount >= MIN_LOAN_AMOUNT
                && loanAmount <= MAX_LOAN_AMOUNT
                && loanAmount % LOAN_AMOUNT_INCREMENT == 0
                ;
    }
}
