package com.cllg.community_service.controller;

import com.cllg.community_service.entity.Post;
import com.cllg.community_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(
                postRepository.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long id
    ) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody Post post
    ) {
        return ResponseEntity.ok(
                postRepository.save(post)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id
    ) {
        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        postRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}