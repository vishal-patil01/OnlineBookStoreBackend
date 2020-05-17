package com.enigma.bookstore.util;

import java.util.Date;

public interface IJWTToken {
    public String generateToken(Integer id, Date expirationTime);

    public int verifyToken(String token);
}
