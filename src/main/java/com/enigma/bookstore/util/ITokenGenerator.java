package com.enigma.bookstore.util;

import java.util.Date;

public interface ITokenGenerator {
    String generateToken(Integer id, Date expirationTime);

     int verifyToken(String token);
}
