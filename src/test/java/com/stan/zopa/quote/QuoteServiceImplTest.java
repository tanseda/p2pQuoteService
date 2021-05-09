package com.stan.zopa.quote;

import com.stan.zopa.market.Lender;
import com.stan.zopa.market.LenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteServiceImplTest {

    private QuoteService quoteService;

    @Mock
    private LenderService lenderService;

    private Stream<Lender> lenders;

    @BeforeEach
    void setup(){
        quoteService = new QuoteServiceImpl(lenderService);

        lenders = List.of(
                createLender("Bob" , 0.075, 640),
                createLender("Jane" , 0.069, 480),
                createLender("Fred" , 0.071, 520),
                createLender("Mary" , 0.104, 170),
                createLender("John" , 0.081, 320),
                createLender("Dave" , 0.074, 140),
                createLender("Angela" , 0.071, 50)
        ).stream();
    }

    @Test
    void testValidLoanRequest() {
        when(lenderService.getLendersOrderedByRate()).thenReturn(lenders);

        Optional<Quote> result = quoteService.getQuote(1000, 36);
        Quote expected = Quote.builder()
                .loanAmount(1000)
                .numberOfPayments(36)
                .interestRate(0.07284)
                .monthlyPayment(31.0072)
                .totalRepayment(1116.2621)
                .build();

        Quote actual = result.orElse(Quote.builder().build());
        assertTrue(result.isPresent());
        assertEquals(expected.getInterestRate(), actual.getInterestRate(), 0.001);
        assertEquals(expected.getMonthlyPayment(), actual.getMonthlyPayment(), 0.001);
        assertEquals(expected.getTotalRepayment(), actual.getTotalRepayment(), 0.001);
        assertEquals(expected.getNumberOfPayments(), actual.getNumberOfPayments());
        assertEquals(expected.getLoanAmount(), actual.getLoanAmount());
    }

    @Test
    void testLowLoanRequest() {
        Optional<Quote> result = quoteService.getQuote(10, 36);

        assertTrue(result.isEmpty());
    }


    @Test
    void testHighLoanRequest() {
        Optional<Quote> result = quoteService.getQuote(10000, 36);

        assertTrue(result.isEmpty());
    }

    @Test
    void testInvalidRequest() {
        Optional<Quote> result = quoteService.getQuote(14999, 36);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUnavailableQuote() {
        when(lenderService.getLendersOrderedByRate()).thenReturn(lenders);

        Optional<Quote> result = quoteService.getQuote(14900, 36);

        assertTrue(result.isEmpty());
    }

    private Lender createLender(String name, Double rate, Integer amount) {
        return Lender.builder()
                .rate(rate)
                .amount(amount)
                .name(name)
                .build();

    }
}
