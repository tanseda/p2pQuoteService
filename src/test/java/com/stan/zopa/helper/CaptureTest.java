package com.stan.zopa.helper;

import java.io.ByteArrayOutputStream;

public abstract class CaptureTest {
    public abstract void test(ByteArrayOutputStream outContent, ByteArrayOutputStream errContent ) throws Exception;
}
