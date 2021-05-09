package com.stan.zopa.market;

import java.util.stream.Stream;

public interface LenderService {
    Stream<Lender> getLendersOrderedByRate();
}
