package com.stan.zopa.integration;

import com.stan.zopa.P2OQuoteService;
import com.stan.zopa.helper.CaptureTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static com.stan.zopa.helper.TestHelper.captureOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class P2OQuoteServiceIT {

    @Test
    void testValidQuote() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                P2OQuoteService.main(new String[]{"1000"});

                assertEquals( "Requested amount: £1000\n" +
                        "Annual Interest Rate: 7.0%\n" +
                        "Monthly repayment: £30.88\n" +
                        "Total repayment: £1111.64\n", outContent.toString() );
            }
        });
    }

    @Test
    void testUnAvailableQuote() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                P2OQuoteService.main(new String[]{"14900"});

                assertEquals( "It is not possible to provide a quote.\n", outContent.toString() );
            }
        });
    }

}
