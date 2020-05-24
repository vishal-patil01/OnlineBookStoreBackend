package com.enigma.bookstore.service.implementation;

import com.enigma.bookstore.dto.CartDTO;
import com.enigma.bookstore.exception.BookException;
import com.enigma.bookstore.exception.CartException;
import com.enigma.bookstore.exception.CartItemsException;
import com.enigma.bookstore.exception.UserException;
import com.enigma.bookstore.model.Book;
import com.enigma.bookstore.model.Cart;
import com.enigma.bookstore.model.CartItems;
import com.enigma.bookstore.model.User;
import com.enigma.bookstore.repository.IBookRepository;
import com.enigma.bookstore.repository.ICartItemsRepository;
import com.enigma.bookstore.repository.ICartRepository;
import com.enigma.bookstore.repository.IUserRepository;
import com.enigma.bookstore.service.ICartService;
import com.enigma.bookstore.util.IJWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    IBookRepository bookRepository;
    @Autowired
    private ICartItemsRepository cartItemsRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private IJWTToken jwtToken;

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public String addToCart(CartDTO bookCartDTO, String token) {
        Cart cart = checkUserAndCartIsExists(token);
        Book book = bookRepository.findById(bookCartDTO.id)
                .orElseThrow(() -> new BookException("Book Not Found"));
        List<CartItems> bookAlreadyExist = cartItemsRepository.findByBookIdAndCart_CardId(book.getId(), cart.getCardId());
        if (bookAlreadyExist.isEmpty()) {
            CartItems cartBook = new CartItems(bookCartDTO, book, cart);
            cartItemsRepository.save(cartBook);
            return "Book Added To Cart Successfully";
        }
        throw new CartItemsException("Book Already Exists In Cart");
    }

    @Override
    public List<CartItems> fetchCart(String token) {
        Cart cart = checkUserAndCartIsExists(token);
        List<CartItems> cartItemsList = cartItemsRepository.findAllByCart_CardId(cart.getCardId());
        if (cartItemsList.isEmpty())
            throw new CartItemsException("There Are In Items In A Cart");
        return cartItemsList;
    }

    private Cart checkUserAndCartIsExists(String token) {
        int userId = jwtToken.verifyToken(token);
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Exist"));
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartException("Cart Not Found"));
    }
}


