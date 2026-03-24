package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewEntity> createReview(@RequestBody ReviewEntity review) {
        ReviewEntity newReview = reviewService.createReview(review);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewEntity>> getAllReviews() {
        List<ReviewEntity> reviews = reviewService.searchReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewEntity> getReviewById(@PathVariable Long id) {
        ReviewEntity review = reviewService.searchReview(id);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

   @PutMapping("/{id}")
    public ResponseEntity<ReviewEntity> updateReview(
    @PathVariable Long id, 
    @RequestBody ReviewEntity review) { 
    return new ResponseEntity<>(reviewService.updateReview(id, review), HttpStatus.OK);
}
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id, 
            @RequestParam Long adopterId, 
            @RequestParam boolean isAdmin) {
        reviewService.deleteReview(id, adopterId, isAdmin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
