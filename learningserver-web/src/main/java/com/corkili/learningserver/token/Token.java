package com.corkili.learningserver.token;

public final class Token {

    private final String token;
    private Long userId;
    private boolean login;

    Token(String token) {
        this.token = token;
        this.userId = null;
        this.login = false;
    }

    String getToken() {
        return token;
    }

    Long getUserId() {
        return userId;
    }

    Token setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    boolean isLogin() {
        return login;
    }

    Token setLogin(boolean login) {
        this.login = login;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        return token.equals(token1.token);
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
