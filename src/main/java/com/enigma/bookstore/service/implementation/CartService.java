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
import com.enigma.bookstore.util.ITokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private ITokenGenerator jwtToken;

    @Override
    public String createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
        return "Cart Created Successfully";
    }

    @Override
    public String addToCart(CartDTO cartDTO, String token) {
        Cart cart = checkUserAndCartIsExists(token);
        Book book = bookRepository.findById(cartDTO.id)
                .orElseThrow(() -> new BookException("Book Not Found"));
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<CartItems> bookAlreadyExist = cartItemsRepository.findCarts(pageRequest,book.getId(), cart.getCardId());
        if (bookAlreadyExist.isEmpty()) {
            CartItems cartBook = new CartItems(cartDTO, book, cart);
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
            throw new CartItemsException("There Are No Items In A Cart");
        return cartItemsList;
    }

    @Override
    public String deleteBookFromCart(Integer cartItemId, String token) {
        checkUserAndCartIsExists(token);
        CartItems cartItems = getCartItems(cartItemId);
        cartItemsRepository.delete(cartItems);
        return "Book Removed From Cart";
    }

    @Override
    public String updateCartItemQuantity(Integer cartItemId, Integer quantity, String token) {
        checkUserAndCartIsExists(token);
        CartItems cartItems = getCartItems(cartItemId);
        if (cartItems.getBook().getNoOfCopies() < quantity || 0 > quantity)
            throw new BookException("Insufficient Or Invalid Book Quantity");
        cartItems.setQuantity(quantity);
        cartItemsRepository.save(cartItems);
        return "Cart Updated Successfully";
    }

    private CartItems getCartItems(Integer cartItemId) {
        return cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemsException("There Is No Such Item In Cart"));
    }

    private Cart checkUserAndCartIsExists(String token) {
        int userId = jwtToken.verifyToken(token);
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User Not Exist"));
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartException("Cart Not Found"));
    }
}


