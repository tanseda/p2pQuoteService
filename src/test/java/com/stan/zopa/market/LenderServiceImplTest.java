package com.stan.zopa.market;

import com.stan.zopa.helper.CaptureTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stan.zopa.helper.TestHelper.captureOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LenderServiceImplTest {

    @Test
    void testInvalidFile() throws Exception {
        captureOutput( new CaptureTest() {
            @Override
            public void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent) {
                LenderService lenderService = new LenderServiceImpl("market-invalid.csv");

                assertEquals( "Could not parse line: Fred;0.071;520sda\n", outContent.toString() );
            }
        });
    }

    @Test
    void testEmptyFile()  {
        LenderService lenderService = new LenderServiceImpl("empty.csv");
        assertTrue(lenderService.getLendersOrderedByRate().findFirst().isEmpty());
    }

    @Test
    void testValidFile() {
        LenderService lenderService = new LenderServiceImpl("market.csv");
        Set<Lender> ret = lenderService.getLendersOrderedByRate().collect(Collectors.toSet());

        Set<Lender> expected = Set.of(
                createLender("Bob" , 0.075, 640),
                createLender("Jane" , 0.069, 480),
                createLender("Fred" , 0.071, 520),
                createLender("Mary" , 0.104, 170),
                createLender("John" , 0.081, 320),
                createLender("Dave" , 0.074, 140),
                createLender("Angela" , 0.071, 60)
        );

        assertEquals(expected, ret);
    }

    private Lender createLender(String name, Double rate, Integer amount) {
        return Lender.builder()
                .rate(rate)
                .amount(amount)
                .name(name)
                .build();

    }
}
