package dictation.word.config;


import dictation.word.filter.AuthenticationFilter;
import dictation.word.filter.LoginFilter;
import dictation.word.handler.CustomAccessDeniedHandler;
import dictation.word.handler.UnAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ljh
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    /**
     * 请求接口白名单
     */
    public static final String[] URL_LIST = {
            "/user/key",
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置
//        disableSecurity(http);
        enableSecurity(http);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略白名单
        web.ignoring().antMatchers(URL_LIST);
    }

    /**
     * 关闭安全控制
     */
    private void disableSecurity(HttpSecurity http) throws Exception {
        //取消 cors 和 csrf
        http.cors().and().csrf().disable();
    }

    /**
     * 启用安全控制
     */
    private void enableSecurity(HttpSecurity http) throws Exception {
        //取消 cors 和 csrf
        http.cors().and().csrf().disable()
                //启用授权请求
                .authorizeRequests()
                //其它任何请求都进行认证
                .anyRequest().authenticated()
                .and()
                //启用异常处理器
                .exceptionHandling()
                //登录无效处理
                .authenticationEntryPoint(new UnAuthEntryPoint())
                //授权拒绝处理
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                //添加登录过滤器
                .addFilter(new LoginFilter(getAuthenticationManager(), stringRedisTemplate))
                //添加身份认证过滤器
                .addFilter(new AuthenticationFilter(getAuthenticationManager(), stringRedisTemplate))
                .httpBasic();
    }

    /**
     * 返回密码编码器，用于验证密码是否正确
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取身份验证管理器
     */
    @Bean
    AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
