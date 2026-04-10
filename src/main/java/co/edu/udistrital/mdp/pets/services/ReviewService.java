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

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Transactional
    public ReviewEntity createReview(ReviewEntity review, Long adoptionId, Long adopterId) {
        log.info("Creating review");

        review.setAdoption(
            adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"))
        );

        review.setAdopter(
            adopterRepository.findById(adopterId)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found"))
        );

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewEntity> searchReviews() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReviewEntity searchReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Transactional
    public ReviewEntity updateReview(Long id, ReviewEntity reviewDetails) {
        ReviewEntity existing = searchReview(id);

        existing.setComments(reviewDetails.getComments());
        existing.setRating(reviewDetails.getRating());
        existing.setReviewDate(reviewDetails.getReviewDate());

        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteReview(Long id) {
        ReviewEntity review = searchReview(id);
        reviewRepository.delete(review);
    }
}