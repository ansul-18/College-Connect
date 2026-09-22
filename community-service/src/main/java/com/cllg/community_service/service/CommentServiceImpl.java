package com.cllg.community_service.service;



import com.cllg.community_service.client.UserClient;
import com.cllg.community_service.dto.request.CommentRequest;
import com.cllg.community_service.dto.response.CommentResponse;
import com.cllg.community_service.dto.response.UserResponse;
import com.cllg.community_service.entity.Comment;
import com.cllg.community_service.exception.ResourceNotFoundException;
import com.cllg.community_service.repository.CommentRepository;
import com.cllg.community_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl
        implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserClient userClient;


    @Override
    public CommentResponse create(
            CommentRequest request) {

        if (!postRepository
                .existsById(
                        request.getPostId()
                )) {

            throw new ResourceNotFoundException(
                    "Post not found: "
                            + request.getPostId()
            );
        }


        validateUser(
                request.getAuthorId()
        );


        Comment comment =
                Comment.builder()

                        .postId(
                                request.getPostId()
                        )

                        .authorId(
                                request.getAuthorId()
                        )

                        .content(
                                request.getContent()
                        )

                        .build();


        return map(
                commentRepository.save(
                        comment
                )
        );
    }



    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse>
    getByPost(
            Long postId) {

        if (!postRepository
                .existsById(postId)) {

            throw new ResourceNotFoundException(
                    "Post not found: "
                            + postId
            );
        }


        return commentRepository
                .findByPostIdOrderByCreatedAtAsc(
                        postId
                )
                .stream()
                .map(this::map)
                .toList();
    }


    @Override
    public void delete(
            Long id) {

        Comment comment =
                commentRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Comment not found: "
                                                        + id
                                        )
                        );

        commentRepository.delete(
                comment
        );
    }


    private void validateUser(
            Long userId) {

        try {

            UserResponse user =
                    userClient.getStudentById(
                            userId
                    );

            if (user == null) {

                throw new ResourceNotFoundException(
                        "User not found: "
                                + userId
                );
            }

        } catch (ResourceNotFoundException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ResourceNotFoundException(
                    "Unable to validate user: "
                            + userId
            );
        }
    }


    private CommentResponse map(
            Comment comment) {

        UserResponse user =
                null;


        try {

            user =
                    userClient.getStudentById(
                            comment.getAuthorId()
                    );

        } catch (Exception ignored) {
        }


        return CommentResponse.builder()

                .id(
                        comment.getId()
                )

                .postId(
                        comment.getPostId()
                )

                .authorId(
                        comment.getAuthorId()
                )

                .authorName(
                        user != null
                                ? user.getName()
                                : "Unknown User"
                )

                .authorProfileImage(
                        user != null
                                ? user.getProfileImage()
                                : null
                )

                .content(
                        comment.getContent()
                )

                .createdAt(
                        comment.getCreatedAt()
                )

                .build();
    }
}