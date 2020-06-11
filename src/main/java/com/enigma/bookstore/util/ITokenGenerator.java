package com.enigma.bookstore.util;

import java.util.Date;

public interface ITokenGenerator {
    public String generateToken(Integer id, Date expirationTime);

    public int verifyToken(String token);
}
