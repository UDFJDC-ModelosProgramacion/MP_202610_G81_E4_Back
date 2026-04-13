package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.ReviewDTO;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.services.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private static final String ERR_MSG = "message";

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ReviewDTO dto) {
        try {
            // Se pasan los 3 parámetros que requiere el service
            ReviewEntity saved = service.createReview(
                toEntity(dto), 
                dto.getAdoptionId(), 
                dto.getAdopterId()
            );
            return new ResponseEntity<>(new ReviewDTO(saved), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAll() {
        return ResponseEntity.ok(service.getReviews().stream()
                .map(ReviewDTO::new)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ReviewDTO(service.getReview(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            service.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    private ReviewEntity toEntity(ReviewDTO dto) {
        if (dto == null) return null;
        ReviewEntity e = new ReviewEntity();
        e.setId(dto.getId());
        e.setRating(dto.getRating());
        e.setComments(dto.getComments()); // Corregido de 'comment' a 'comments'
        e.setReviewDate(dto.getReviewDate());
        return e;
    }
}
