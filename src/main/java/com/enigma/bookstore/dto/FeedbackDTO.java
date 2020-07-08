package com.enigma.bookstore.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FeedbackDTO {

    @NotNull(message = "Rating cannot be null")
    public Integer rating;

    @NotNull(message = "Feedback cannot be null")
    @Size(min = 10, max = 200, message = "Feedback must include 10-200 characters")
    public String feedbackMessage;

    public String isbNumber;

    public String userName;

    public FeedbackDTO() {
    }

    public FeedbackDTO(Integer rating, String feedbackMessage, String isbNumber, String userName) {
        this.rating = rating;
        this.feedbackMessage = feedbackMessage;
        this.isbNumber = isbNumber;
        this.userName=userName;
    }
}