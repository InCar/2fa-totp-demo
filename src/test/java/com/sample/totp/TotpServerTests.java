package com.sample.totp;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * TotpServerTests
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TotpServerTests {

    @Test
    public void testGenSecret() {
        // secret
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        log.info("testGenSecret -> secret={}", secret);
    }

    @Test
    public void testGenQrImage() throws Exception {
        // secret
        String secret = "QYUBLST6D6QHB4GXK5W754WJEZHS5KUM";

        // qr code
        QrData qrData = new QrData.Builder()
                .label("aaric")
                .issuer("InCar")
                .secret(secret)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        // qr image
        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] qrBytes = qrGenerator.generate(qrData);
        String qrMimeType = qrGenerator.getImageMimeType();
        String qrImageUri = Utils.getDataUriForImage(qrBytes, qrMimeType);
        log.info("testGenQrImage -> qrImageUri={}", qrImageUri);
    }

    @Test
    public void testValidCode() {
        // secret
        String secret = "QYUBLST6D6QHB4GXK5W754WJEZHS5KUM";

        // valid
        TimeProvider timeProvider = new SystemTimeProvider();
        //CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
        CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        log.info("testValidCode -> isValidCode={}", codeVerifier.isValidCode(secret, "340200"));
    }

    @Test
    public void testGenRecoveryCodes() {
        // recovery
        RecoveryCodeGenerator recoveryCodeGenerator = new RecoveryCodeGenerator();
        String[] recoveryCodes = recoveryCodeGenerator.generateCodes(12);
        for (String code : recoveryCodes) {
            log.info("{}", code);
        }
    }
}
