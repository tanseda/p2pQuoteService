package com.stan.zopa.market;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

@Value
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Lender {
    String name;
    Double rate;
    @With Integer amount;
}
