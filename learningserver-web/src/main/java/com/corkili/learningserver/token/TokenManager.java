package com.corkili.learningserver.token;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.common.ServiceUtils;

@Component
@Slf4j
public class TokenManager {

    private Map<String, Token> tokenMap;

    private TokenManager() {
        tokenMap = new ConcurrentHashMap<>();
    }

    public String getOrNewToken(String token) {
        if (StringUtils.isBlank(token)) {
            return createToken();
        }
        return token;
    }

    private String createToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (tokenMap.containsKey(token));
        tokenMap.put(token, new Token(token));
        return token;
    }

    public void removeToken(String token) {
        tokenMap.remove(token);
    }

    public boolean isTokenAssociatedWithUser(String token) {
        if (!tokenMap.containsKey(token)) {
            return false;
        }
        return tokenMap.get(token).getUserId() != null;
    }

    public Long getUserIdAssociatedWithToken(String token) {
        if (isTokenAssociatedWithUser(token)) {
            return tokenMap.get(token).getUserId();
        }
        log.error("token [{}] doesn't associated with user", token);
        throw new IllegalStateException(ServiceUtils.format("token [{}] doesn't associated with user", token));
    }

    public boolean isLogin(String token) {
        if (!tokenMap.containsKey(token)) {
            return false;
        }
        Token testToken = tokenMap.get(token);
        return testToken.getUserId() != null && testToken.isLogin();
    }

    public TokenManager setTokenAssociatedUser(String token, Long userId) {
        if (!tokenMap.containsKey(token)) {
            log.error("token [{}] not exists", token);
            throw new IllegalStateException(ServiceUtils.format("token [{}] not exists", token));
        }
        tokenMap.get(token).setUserId(userId);
        return this;
    }

    public TokenManager tokenLogin(String token) {
        if (!tokenMap.containsKey(token)) {
            log.error("token [{}] not exists", token);
            throw new IllegalStateException(ServiceUtils.format("token [{}] not exists", token));
        }
        Token testToken = tokenMap.get(token);
        if (testToken.getUserId() == null) {
            log.error("token [{}] doesn't associated with user", token);
            throw new IllegalStateException(ServiceUtils.format("token [{}] doesn't associated with user", token));
        }
        testToken.setLogin(true);
        return this;
    }

    public TokenManager setTokenAssociatedUserAndLogin(String token, Long userId) {
        return setTokenAssociatedUser(token, userId).tokenLogin(token);
    }

}
