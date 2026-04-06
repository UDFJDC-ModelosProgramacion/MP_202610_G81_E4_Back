package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.dto.ReviewDTO;
import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.repositories.*;

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
    public ReviewEntity createReview(ReviewDTO dto) {
        log.info("Creating review");

        if (dto == null) {
            throw new IllegalArgumentException("Review cannot be null");
        }

        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        ReviewEntity review = new ReviewEntity();
        review.setComments(dto.getComments());
        review.setRating(dto.getRating());
        review.setReviewDate(dto.getReviewDate());

        // 🔥 RELACIONES CORRECTAS
        review.setAdoption(
            adoptionRepository.findById(dto.getAdoptionId())
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"))
        );

        review.setAdopter(
            adopterRepository.findById(dto.getAdopterId())
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
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Transactional
    public ReviewEntity updateReview(Long id, ReviewDTO dto) {

        ReviewEntity existing = searchReview(id);

        existing.setComments(dto.getComments());
        existing.setRating(dto.getRating());
        existing.setReviewDate(dto.getReviewDate());

        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteReview(Long id, Long adopterId, boolean isAdmin) {

        ReviewEntity review = searchReview(id);

        if (!isAdmin && !review.getAdopter().getId().equals(adopterId)) {
            throw new IllegalStateException("Only the author or admin can delete");
        }

        reviewRepository.delete(review);
    }
}