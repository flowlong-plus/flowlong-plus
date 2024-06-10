package cn.yuencode.flowlongplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.yuencode.flowlongplus.config.redis.RedisService;
import cn.yuencode.flowlongplus.controller.req.LoginReq;
import cn.yuencode.flowlongplus.service.AuthService;
import cn.yuencode.flowlongplus.util.R;
import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 授权接口
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
@Api(tags = "授权接口")
public class AuthController {
    @Resource
    private AuthService authService;
    @Resource
    private RedisService redisService;

    /**
     * 登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R<?> authLogin(@RequestBody LoginReq loginReq) {
        return R.success(authService.authLogin(loginReq));
    }


    /**
     * 退出登录
     */
    @SaCheckLogin
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public R<?> logout() {
        authService.logout();
        return R.success();
    }

    /**
     * 查询当前用户信息
     */
    @SaCheckLogin
    @ApiOperation("查询当前用户信息")
    @PostMapping("/info")
    public R<?> getInfo() {
        return R.success(authService.getInfo());
    }

    /**
     * 获取路由信息
     */
    @SaCheckLogin
    @ApiOperation("获取路由信息")
    @GetMapping("/getRouters")
    public R<?> getRouters() {
        return R.success(authService.getRouters());
    }

    /**
     * 获取Base64验证码
     */
    @ApiOperation("获取Base64验证码")
    @GetMapping("/captchaBase64")
    public R<Object> captchaBase64() {
        SpecCaptcha specCaptcha = new SpecCaptcha();
        redisService.add("captcha_" + specCaptcha.text().toUpperCase(), specCaptcha.text().toUpperCase(), 300L, TimeUnit.SECONDS);
        JSONObject resultJson = new JSONObject();
        resultJson.put("verCode", specCaptcha.text());
        resultJson.put("img", specCaptcha.toBase64());
        return R.success(resultJson);
    }
}
