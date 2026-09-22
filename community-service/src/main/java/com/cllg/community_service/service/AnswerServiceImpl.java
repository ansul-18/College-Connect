package com.cllg.community_service.service;



import com.cllg.community_service.client.UserClient;
import com.cllg.community_service.dto.request.AnswerRequest;
import com.cllg.community_service.dto.response.AnswerResponse;
import com.cllg.community_service.dto.response.UserResponse;
import com.cllg.community_service.entity.Answer;
import com.cllg.community_service.exception.ResourceNotFoundException;
import com.cllg.community_service.repository.AnswerRepository;
import com.cllg.community_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl
        implements AnswerService {

    private final AnswerRepository answerRepository;

    private final PostRepository postRepository;

    private final UserClient userClient;


    @Override
    public AnswerResponse create(
            AnswerRequest request) {

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


        Answer answer =
                Answer.builder()

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
                answerRepository.save(
                        answer
                )
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<AnswerResponse>
    getByPost(
            Long postId) {

        if (!postRepository
                .existsById(postId)) {

            throw new ResourceNotFoundException(
                    "Post not found: "
                            + postId
            );
        }


        return answerRepository
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

        Answer answer =
                answerRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Answer not found: "
                                                        + id
                                        )
                        );

        answerRepository.delete(
                answer
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


    private AnswerResponse map(
            Answer answer) {

        UserResponse user =
                null;


        try {

            user =
                    userClient.getStudentById(
                            answer.getAuthorId()
                    );

        } catch (Exception ignored) {
        }


        return AnswerResponse.builder()

                .id(
                        answer.getId()
                )

                .postId(
                        answer.getPostId()
                )

                .authorId(
                        answer.getAuthorId()
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
                        answer.getContent()
                )

                .createdAt(
                        answer.getCreatedAt()
                )

                .build();
    }
}