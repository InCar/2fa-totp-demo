# 2fa-totp-demo

[![Java](https://img.shields.io/badge/Java-17-brightgreen.svg?style=flat&logo=java)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![JUnit](https://img.shields.io/badge/JUnit-5.9.2-brightgreen.svg?style=flat&logo=junit5)](https://junit.org/junit5/docs/current/user-guide)
[![Gradle](https://img.shields.io/badge/Gradle-8.4-brightgreen.svg?style=flat&logo=gradle)](https://docs.gradle.org/8.4/userguide/installation.html)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.2-brightgreen.svg?style=flat&logo=springboot)](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2022.0.0-brightgreen.svg?style=flat&logo=spring)](https://docs.spring.io/spring-cloud/docs/2020.0.0/reference/htmlsingle/)
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring_Cloud_Alibaba-2022.0.0.0-brightgreen.svg?style=flat&logo=alibabacloud)](https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/zh-cn/index.html)
[![Release](https://img.shields.io/badge/Release-0.1.0-blue.svg)](https://github.com/InCar/2fa-totp-demo/releases)

> 2FA TOTP Demo.

## TOTP Server

### Generate secret

```java
public class TotpServerTests {
    @Test
    public void testGenSecret() {
        // secret
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        log.info("testGenSecret -> secret={}", secret);
    }
}
```

### Generate qr image

```java
public class TotpServerTests {
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
}
```

### Valid code

```java
public class TotpServerTests {
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
}
```

### Generate recovery codes

```java
public class TotpServerTests {
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
```

## TOTP Client

### Generate code

```java
public class TotpServerTests {
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
```

## Web Browser

&emsp;&emsp;*[http://localhost:8080/home/index](http://localhost:8080/home/index)*
