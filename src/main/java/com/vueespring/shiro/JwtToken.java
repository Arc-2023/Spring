package com.vueespring.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
@Slf4j
public class JwtToken extends UsernamePasswordToken {
    public String token;
    public JwtToken(String token){
        this.token=token;
    }
    /**
     * @return
     */
    @Override
    public Object getPrincipal() {
        return this.token;
    }
    /**
     * @return
     */
    @Override
    public Object getCredentials() {
        return this.token;
    }
}
