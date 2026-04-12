package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ReviewDTO;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.services.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.searchReviews()
                .stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewEntity entity = reviewService.searchReview(id);
        return ResponseEntity.ok(new ReviewDTO(entity));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setComments(reviewDTO.getComments());
        reviewEntity.setRating(reviewDTO.getRating());
        reviewEntity.setReviewDate(reviewDTO.getReviewDate());

        ReviewEntity savedEntity = reviewService.createReview(
                reviewEntity, 
                reviewDTO.getAdoptionId(), 
                reviewDTO.getAdopterId()
        );
        
        return new ResponseEntity<>(new ReviewDTO(savedEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewEntity reviewDetails = new ReviewEntity();
        reviewDetails.setComments(reviewDTO.getComments());
        reviewDetails.setRating(reviewDTO.getRating());
        reviewDetails.setReviewDate(reviewDTO.getReviewDate());

        ReviewEntity updatedEntity = reviewService.updateReview(id, reviewDetails);
        return ResponseEntity.ok(new ReviewDTO(updatedEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
