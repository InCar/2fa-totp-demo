package com.sample.totp.api.page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;

/**
 * 主页模块Page接口
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@Tag(name = "主页模块Page")
public interface HomePage {

    @Operation(summary = "主页")
    String index(Model model);

    @Operation(summary = "登录后扫码绑定")
    String loginStep01(Model model, @Parameter(description = "用户名") String username) throws Exception;

    @Operation(summary = "登录后下载恢复密码")
    String loginStep02(Model model);

    @Operation(summary = "登录后绑定成功")
    String loginStep03(Model model) throws Exception;

    @Operation(summary = "登录后App验证")
    String loginStep04(Model model, @Parameter(description = "用户名") String username) throws Exception;

    @Operation(summary = "登录后App验证成功")
    String loginStep05(Model model);
}
