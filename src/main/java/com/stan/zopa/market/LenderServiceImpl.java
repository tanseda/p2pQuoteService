package com.stan.zopa.market;

import com.google.inject.Inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class LenderServiceImpl implements LenderService{

    private final List<Lender> market = new ArrayList<>();

    @Inject
    public LenderServiceImpl() {
        init("market.csv");
    }

    public LenderServiceImpl(String sourceFile) {
        init(sourceFile);
    }

    private void init(String sourceFile) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sourceFile);
        Objects.requireNonNull(inputStream);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line).ifPresent(market::add);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while parsing the lender list.");
            e.printStackTrace();
        }
    }

    private Optional<Lender> parseLine(String csvLine) {
        String[] fields = csvLine.split(";");
        if (fields.length == 3) {
            try {
                double rate = Double.parseDouble(fields[1]);
                int amount = Integer.parseInt(fields[2]);
                return Optional.of(
                        Lender.builder()
                                .name(fields[0])
                                .rate(rate)
                                .amount(amount)
                                .build()
                );
            } catch (Exception e) {
                System.out.println("Could not parse line: " + csvLine);
            }
        }
        return Optional.empty();
    }

    public Stream<Lender> getLendersOrderedByRate() {
        return market.stream()
                .sorted(Comparator.comparingDouble(Lender::getRate));
    }
}
