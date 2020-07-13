package com.enigma.bookstore.service;

import com.enigma.bookstore.dto.BookDTO;
import com.enigma.bookstore.dto.UserLoginDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IAdminService {
    String addBook(BookDTO bookDTO, String token);

    String uploadImage(MultipartFile file, String token);

    String updateBook(BookDTO bookDTO, Integer bookId, String token);

    String adminLogin(UserLoginDTO userLoginDTO);
}
