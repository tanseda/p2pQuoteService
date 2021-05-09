package com.stan.zopa;

import com.stan.zopa.helper.CaptureTest;
import com.stan.zopa.quote.Quote;
import com.stan.zopa.quote.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static com.stan.zopa.helper.TestHelper.captureOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class P2OQuoteServiceTest {
    P2OQuoteService p2OQuoteService;

    @Mock
    private QuoteService quoteService;

    @BeforeEach
    void setup(){
        p2OQuoteService = new P2OQuoteService(quoteService);
    }

    @Test
    void testValidQuote() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                when(quoteService.getQuote(anyInt(), anyInt())).thenReturn(
                        Optional.of(createExpectedQuote(
                                1000,
                                36, 0.07284, 31.0072,1116.2621)));

                p2OQuoteService.calculateQuote(1000);

                assertEquals( "Requested amount: £1000\n" +
                        "Annual Interest Rate: 7.3%\n" +
                        "Monthly repayment: £31.01\n" +
                        "Total repayment: £1116.26\n", outContent.toString() );
            }
        });
    }

    @Test
    void testNoArgs() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                P2OQuoteService.main(new String[0]);

                assertEquals( "Please enter amount\n", outContent.toString() );
            }
        });
    }

    @Test
    void testInvalidArguments() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                P2OQuoteService.main(new String[]{"100.123"});

                assertEquals( "Please enter valid amount\n", outContent.toString() );
            }
        });
    }

    @Test
    void testUnavailableQuote() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                when(quoteService.getQuote(anyInt(), anyInt())).thenReturn(Optional.empty());

                p2OQuoteService.calculateQuote(14900);

                assertEquals( "It is not possible to provide a quote.\n", outContent.toString() );
            }
        });
    }

    private Quote createExpectedQuote(int loanAmount, int numberOfPayments, double interestRate,
                                      double monthlyPayment, double totalRepayment) {
        return Quote.builder()
                .loanAmount(loanAmount)
                .numberOfPayments(numberOfPayments)
                .interestRate(interestRate)
                .monthlyPayment(monthlyPayment)
                .totalRepayment(totalRepayment)
                .build();
    }
}
