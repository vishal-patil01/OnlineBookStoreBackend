package com.enigma.bookstore.controller;


import com.enigma.bookstore.dto.Response;
import com.enigma.bookstore.enums.FilterAttributes;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.service.IBookService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/bookstore")
public class BookController {

    @Autowired
    private IBookService iBookStoreService;

    @GetMapping("/books/")
    public ResponseEntity<Response> fetchBooks(@RequestParam(value = "searchtext", defaultValue = "") String searchText, @RequestParam(value = "pageno") int pageNo, @RequestParam(value = "filterattributes", defaultValue = "DEFAULT") FilterAttributes filterAttributes) {
        Page<Book> books = iBookStoreService.fetchBooks(searchText, pageNo, filterAttributes);
        Response response = new Response("Book fetch Successfully", books, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("image/{fileName}")
    public ResponseEntity<Resource> loadImage(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = iBookStoreService.loadImages(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(request.getServletContext().getMimeType(resource.getFile().getAbsolutePath())))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
