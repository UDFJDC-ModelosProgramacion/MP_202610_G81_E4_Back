package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import jakarta.persistence.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.ReviewRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    public ReviewEntity createReview(ReviewEntity review) {
        log.info("Creating review");
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (review.getAdoption() == null) {
            throw new IllegalArgumentException("Adoption cannot be null");
        }
        if (review.getAdopter() == null) {
            throw new IllegalArgumentException("Adopter cannot be null");
        }
        ReviewEntity savedReview = reviewRepository.save(review);
        log.info("Review created with id: {}", savedReview.getId());
        return savedReview;
    }
    public ReviewEntity searchReview(Long id) {
        log.info("Searching review with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }
    public List<ReviewEntity> searchReviews() {
        log.info("Searching all reviews");
        return reviewRepository.findAll();
    }
    public ReviewEntity updateReview(Long id, ReviewEntity review) {
        log.info("Updating review with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }
        ReviewEntity existing = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        existing.setComments(review.getComments());
        existing.setRating(review.getRating());
        existing.setReviewDate(review.getReviewDate());
        
        ReviewEntity updatedReview = reviewRepository.save(existing);
        log.info("Review updated with id: {}", updatedReview.getId());
        return updatedReview;
    }
    public void deleteReview(Long id, Long adopterId, boolean isAdmin) {
        log.info("Deleting review with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        ReviewEntity review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        if (!isAdmin && !review.getAdopter().getId().equals(adopterId)) {
            throw new IllegalStateException("Only the author or an admin can delete this review");
        }
        reviewRepository.delete(review);
    }
}