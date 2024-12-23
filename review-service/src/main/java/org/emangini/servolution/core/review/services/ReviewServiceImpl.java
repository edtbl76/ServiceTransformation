package org.emangini.servolution.core.review.services;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.core.review.Review;
import org.emangini.servolution.api.core.review.ReviewService;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    
    private final ServiceUtil serviceUtil;
    
    @Autowired
    public ReviewServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }
    
    @Override
    public List<Review> getReviews(int productId) {
        
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        
        if (productId == 13) {
            log.debug("No reviews found for productId: {}", productId);
            return new ArrayList<>();
        }
        
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(
                productId, 
                1, 
                "Author 1", 
                "Subject 1", 
                "Content 1", 
                serviceUtil.getServiceAddress())
        );

        reviews.add(new Review(
                productId,
                2,
                "Author 2",
                "Subject 2",
                "Content 2",
                serviceUtil.getServiceAddress())
        );


        reviews.add(new Review(
                productId,
                3,
                "Author 3",
                "Subject 3",
                "Content 3",
                serviceUtil.getServiceAddress())
        );

        log.debug("/review response size: {}", reviews.size());
        
        return reviews;
    }
    

}
