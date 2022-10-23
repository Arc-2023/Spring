package com.vueespring.shiro;

import com.vueespring.entity.WebEntity.UserVoeEntity;
import com.vueespring.service.IUserVoeTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
@Slf4j

public class AccountRealm extends SimpleAccountRealm {
    @Resource
    JwtUtils jwtUtils;
    @Autowired
    IUserVoeTableService userVOEService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
      return null;

    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String)token.getPrincipal();
        String id = jwtUtils.getClaimByToken(jwt).getSubject();
        UserVoeEntity userVoe = userVOEService.getById(id);
        if(userVoe == null){
            throw new UnknownAccountException("账户不存在！");
        }else {
            return new SimpleAuthenticationInfo(token,token,this.getName());
        }
    }
}
