package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ReviewDTO;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.services.ReviewService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) {

        ReviewEntity entity = reviewService.createReview(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReviewDTO(entity));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAll() {

        List<ReviewDTO> list = reviewService.searchReviews()
                .stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable Long id) {

        ReviewEntity entity = reviewService.searchReview(id);

        return ResponseEntity.ok(new ReviewDTO(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(
            @PathVariable Long id,
            @RequestBody ReviewDTO dto) {

        ReviewEntity updated = reviewService.updateReview(id, dto);

        return ResponseEntity.ok(new ReviewDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long adopterId,
            @RequestParam boolean isAdmin) {

        reviewService.deleteReview(id, adopterId, isAdmin);

        return ResponseEntity.noContent().build();
    }
}
