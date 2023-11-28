package com.sample.totp;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.time.NtpTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * TotpClientTests
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TotpClientTests {

    @Test
    public void testGenCode() throws Exception {
        // secret
        String secret = "QYUBLST6D6QHB4GXK5W754WJEZHS5KUM";

        // valid
        int timePeriod = 30;
        //TimeProvider timeProvider = new SystemTimeProvider();
        TimeProvider timeProvider = new NtpTimeProvider("ntp6.aliyun.com", 3000);
        long counter = Math.floorDiv(timeProvider.getTime(), timePeriod);
        log.info("counter: {}", counter);
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        log.info("code: {}", codeGenerator.generate(secret, counter));
    }
}
