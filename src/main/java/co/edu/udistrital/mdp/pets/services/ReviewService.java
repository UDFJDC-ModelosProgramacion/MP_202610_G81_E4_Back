package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.repositories.ReviewRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewService {

    private static final String REVIEW_NOT_FOUND = "Review not found with id: ";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Transactional
    public ReviewEntity createReview(ReviewEntity review, Long adoptionId, Long adopterId) {
        log.info("Creating review for adoption ID: {} and adopter ID: {}", adoptionId, adopterId);

        if (review == null) {
            throw new IllegalArgumentException("Review data cannot be null");
        }

        review.setAdoption(
            adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found with id: " + adoptionId))
        );

        review.setAdopter(
            adopterRepository.findById(adopterId)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found with id: " + adopterId))
        );

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewEntity> getReviews() {
        log.info("Fetching all reviews");
        return reviewRepository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public ReviewEntity getReview(Long id) {
        log.info("Searching review with id: {}", id);
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND + id));
    }

    @Transactional
    public ReviewEntity updateReview(Long id, ReviewEntity reviewDetails) {
        log.info("Updating review with id: {}", id);
        ReviewEntity existing = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND + id));

        existing.setComments(reviewDetails.getComments());
        existing.setRating(reviewDetails.getRating());
        existing.setReviewDate(reviewDetails.getReviewDate());

        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteReview(Long id) {
        log.info("Deleting review with id: {}", id);
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND + id));
                
        reviewRepository.delete(review);
    }
}