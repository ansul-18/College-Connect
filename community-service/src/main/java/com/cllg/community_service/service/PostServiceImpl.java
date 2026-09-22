package com.cllg.community_service.service;



import com.cllg.community_service.client.UserClient;
import com.cllg.community_service.dto.request.PostRequest;
import com.cllg.community_service.dto.response.PostResponse;
import com.cllg.community_service.dto.response.UserResponse;
import com.cllg.community_service.entity.Post;
import com.cllg.community_service.entity.PostTag;
import com.cllg.community_service.entity.Tag;
import com.cllg.community_service.enums.PostCategory;
import com.cllg.community_service.exception.ResourceNotFoundException;
import com.cllg.community_service.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final TagRepository tagRepository;

    private final PostTagRepository postTagRepository;

    private final AnswerRepository answerRepository;

    private final CommentRepository commentRepository;

    private final UserClient userClient;


    @Override
    public PostResponse create(
            PostRequest request) {

        validateUser(
                request.getAuthorId()
        );


        Post post =
                Post.builder()

                        .authorId(
                                request.getAuthorId()
                        )

                        .title(
                                request.getTitle()
                                        .trim()
                        )

                        .content(
                                request.getContent()
                        )

                        .category(
                                request.getCategory()
                        )

                        .build();


        Post saved =
                postRepository.save(
                        post
                );


        saveTags(
                saved.getId(),
                request.getTags()
        );


        return mapToResponse(
                saved
        );
    }




    @Override
    @Transactional(readOnly = true)
    public PostResponse getById(
            Long id) {

        return mapToResponse(
                findPost(id)
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {

        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(
                        this::mapToResponse
                )
                .toList();
    }



    @Override
    @Transactional(readOnly = true)
    public List<PostResponse>
    getByCategory(
            PostCategory category) {

        return postRepository
                .findByCategoryOrderByCreatedAtDesc(
                        category
                )
                .stream()
                .map(
                        this::mapToResponse
                )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostResponse>
    search(
            String keyword) {

        if (keyword == null
                || keyword.isBlank()) {

            return getAll();
        }


        String value =
                keyword.trim();


        /*
         * V1 search:
         * title + content.
         *
         * Tag search is handled separately.
         */
        return postRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByCreatedAtDesc(
                        value,
                        value
                )
                .stream()
                .map(
                        this::mapToResponse
                )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostResponse>
    getByAuthor(
            Long authorId) {

        return postRepository
                .findByAuthorIdOrderByCreatedAtDesc(
                        authorId
                )
                .stream()
                .map(
                        this::mapToResponse
                )
                .toList();
    }


    @Override
    public void delete(
            Long id) {

        Post post =
                findPost(id);


        postTagRepository
                .deleteByPostId(id);


        /*
         * Answers/comments are deleted
         * explicitly because their data
         * belongs to the post.
         */
        answerRepository
                .findByPostIdOrderByCreatedAtAsc(id)
                .forEach(
                        answerRepository::delete
                );


        commentRepository
                .findByPostIdOrderByCreatedAtAsc(id)
                .forEach(
                        commentRepository::delete
                );


        postRepository.delete(
                post
        );
    }


    private Post findPost(
            Long id) {

        return postRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Post not found with id: "
                                                + id
                                )
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


    private void saveTags(
            Long postId,
            List<String> tags) {

        if (tags == null
                || tags.isEmpty()) {

            return;
        }


        for (String rawTag : tags) {

            if (rawTag == null
                    || rawTag.isBlank()) {

                continue;
            }


            String tagName =
                    rawTag
                            .trim()
                            .toLowerCase();


            Tag tag =
                    tagRepository
                            .findByNameIgnoreCase(
                                    tagName
                            )
                            .orElseGet(
                                    () ->
                                            tagRepository.save(
                                                    Tag.builder()
                                                            .name(tagName)
                                                            .build()
                                            )
                            );


            if (!postTagRepository
                    .existsByPostIdAndTagId(
                            postId,
                            tag.getId()
                    )) {

                postTagRepository.save(
                        PostTag.builder()

                                .postId(
                                        postId
                                )

                                .tagId(
                                        tag.getId()
                                )

                                .build()
                );
            }
        }
    }


    private PostResponse mapToResponse(
            Post post) {

        UserResponse user =
                null;


        try {

            user =
                    userClient.getStudentById(
                            post.getAuthorId()
                    );

        } catch (Exception ignored) {
            // Keep post available even if
            // user service is temporarily unavailable.
        }


        List<String> tags =
                postTagRepository
                        .findByPostId(
                                post.getId()
                        )
                        .stream()
                        .map(
                                PostTag::getTagId
                        )
                        .map(
                                tagRepository::findById
                        )
                        .filter(
                                java.util.Optional::isPresent
                        )
                        .map(
                                java.util.Optional::get
                        )
                        .map(
                                Tag::getName
                        )
                        .toList();


        int answerCount =
                (int) answerRepository
                        .countByPostId(
                                post.getId()
                        );


        int commentCount =
                (int) commentRepository
                        .countByPostId(
                                post.getId()
                        );


        return PostResponse.builder()

                .id(
                        post.getId()
                )

                .authorId(
                        post.getAuthorId()
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

                .title(
                        post.getTitle()
                )

                .content(
                        post.getContent()
                )

                .category(
                        post.getCategory()
                )

                .tags(tags)

                .answerCount(
                        answerCount
                )

                .commentCount(
                        commentCount
                )

                .createdAt(
                        post.getCreatedAt()
                )

                .updatedAt(
                        post.getUpdatedAt()
                )

                .build();
    }
}