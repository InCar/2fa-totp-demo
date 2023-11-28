package com.sample.totp.api.page.impl;

import com.sample.totp.api.page.HomePage;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.recovery.RecoveryCodeGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 主页模块Page接口控制器
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/home")
public class HomeController implements HomePage {

    public static final String HOME_CONTROLLER_HASH_KEY = HomeController.class.getSimpleName() + "TotpTest";

    final StringRedisTemplate stringRedisTemplate;

    @Override
    @GetMapping(value = "/index")
    public String index(Model model) {
        // model
        model.addAttribute("title", "主页");
        return "index";
    }

    @Override
    @GetMapping(value = "/login/step01")
    public String loginStep01(Model model, @RequestParam("username") String username) throws Exception {
        // secret
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();

        // cache
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(HOME_CONTROLLER_HASH_KEY, username, secret);

        // qr code
        String issuer = "InCar";
        QrData qrData = new QrData.Builder()
                .label(username)
                .issuer(issuer)
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
        log.info("loginStep01 -> username={}, qrImageUri={}", username, qrImageUri);

        // model
        model.addAttribute("title", "登录后扫码绑定");
        model.addAttribute("label", username);
        model.addAttribute("issuer", issuer);
        model.addAttribute("secret", secret);
        model.addAttribute("qrImageUri", qrImageUri);
        return "login/step01";
    }

    @Override
    @PostMapping(value = "/login/step02")
    public String loginStep02(Model model) {
        // recovery
        RecoveryCodeGenerator recoveryCodeGenerator = new RecoveryCodeGenerator();
        String[] recoveryCodes = recoveryCodeGenerator.generateCodes(12);

        // model
        model.addAttribute("title", "登录后绑定成功");
        model.addAttribute("recoveryCodes", recoveryCodes);
        log.info("loginStep02 -> recoveryCodes=\n{}", String.join("\n", recoveryCodes));

        return "login/step02";
    }

    @Override
    @PostMapping(value = "/login/step03")
    public String loginStep03(Model model) {
        // model
        model.addAttribute("title", "登录后绑定成功");
        return "login/step03";
    }

    @Override
    @PostMapping(value = "/login/step04")
    public String loginStep04(Model model, @RequestParam("username") String username) throws Exception {
        // cache
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String secret = hashOperations.get(HOME_CONTROLLER_HASH_KEY, username);

        // valid
        if (StringUtils.isNotBlank(secret)) {
            int timePeriod = 30;
            TimeProvider timeProvider = new SystemTimeProvider();
            long counter = Math.floorDiv(timeProvider.getTime(), timePeriod);
            CodeGenerator codeGenerator = new DefaultCodeGenerator();
            String code = codeGenerator.generate(secret, counter);
            log.info("loginStep04 -> username={}, secret={}, code={}", username, secret, code);

            // model
            model.addAttribute("secret", secret);
            model.addAttribute("code", code);
        }

        // model
        model.addAttribute("title", "登录后App验证");
        model.addAttribute("username", username);
        return "login/step04";
    }

    @Override
    @PostMapping(value = "/login/step05")
    public String loginStep05(Model model) {
        // model
        model.addAttribute("title", "登录后App验证成功");
        return "login/step05";
    }
}
