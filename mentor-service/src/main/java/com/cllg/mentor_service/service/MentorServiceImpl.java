package com.cllg.mentor_service.service;

import com.cllg.mentor_service.client.DepartmentClient;
import com.cllg.mentor_service.client.UserClient;

import com.cllg.mentor_service.dto.request.MentorRequest;
import com.cllg.mentor_service.dto.response.DepartmentResponse;
import com.cllg.mentor_service.dto.response.MentorResponse;
import com.cllg.mentor_service.dto.response.UserResponse;

import com.cllg.mentor_service.entity.MentorProfile;
import com.cllg.mentor_service.entity.MentorSkill;

import com.cllg.mentor_service.enums.MentorType;

import com.cllg.mentor_service.exception.ResourceAlreadyExistsException;
import com.cllg.mentor_service.exception.ResourceNotFoundException;

import com.cllg.mentor_service.repository.MentorProfileRepository;
import com.cllg.mentor_service.repository.MentorSkillRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MentorServiceImpl
        implements MentorService {

    private final MentorProfileRepository
            mentorProfileRepository;

    private final MentorSkillRepository
            mentorSkillRepository;

    private final DepartmentClient
            departmentClient;

    private final UserClient
            userClient;

    @Override
    @Transactional
    public MentorResponse create(MentorRequest request) {

        if (mentorProfileRepository.existsByUserId(request.getUserId())) {
            throw new ResourceAlreadyExistsException(
                    "Mentor profile already exists for userId: "
                            + request.getUserId()
            );
        }

        // Get user from Auth Service
        UserResponse user = getUser(request.getUserId());

        // User must be a mentor
        if (!"MENTOR".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException(
                    "Only users with MENTOR role can create mentor profile"
            );
        }

        // Validate department
        if (request.getDepartmentId() != null) {
            validateDepartment(request.getDepartmentId());
        }

        // Validate mentor type and price
        validateMentorTypeAndPrice(
                request.getMentorType(),
                request.getPrice()
        );

        // FREE mentor => price 0
        BigDecimal finalPrice =
                request.getMentorType() == MentorType.PAID
                        ? request.getPrice()
                        : BigDecimal.ZERO;

        MentorProfile mentor =
                MentorProfile.builder()
                        .userId(user.getId())

                        // Academic
                        .departmentId(request.getDepartmentId())
                        .year(request.getYear())

                        // Professional
                        .title(request.getTitle())
                        .experience(request.getExperience())
                        .company(request.getCompany())
                        .designation(request.getDesignation())

                        // Profile
                        .bio(request.getBio())
                        .profileImage(request.getProfileImage())

                        // Social
                        .github(request.getGithub())
                        .linkedin(request.getLinkedin())

                        // Mentorship
                        .mentorType(request.getMentorType())
                        .price(finalPrice)

                        .active(true)
                        .build();

        // Save mentor profile
        MentorProfile saved =
                mentorProfileRepository.save(mentor);

        // Save skills
        saveSkills(
                saved.getId(),
                request.getSkills()
        );

        return mapToResponse(saved, user);
    }

    @Override
    @Transactional(readOnly = true)
    public MentorResponse getById(
            Long id
    ) {

        MentorProfile mentor =
                findById(id);

        UserResponse user =
                getUser(
                        mentor.getUserId()
                );

        return mapToResponse(
                mentor,
                user
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorResponse> getAll() {

        return mentorProfileRepository
                .findByActiveTrue()
                .stream()
                .map(
                        mentor -> {

                            UserResponse user =
                                    getUser(
                                            mentor.getUserId()
                                    );

                            return mapToResponse(
                                    mentor,
                                    user
                            );
                        }
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorResponse> getByDepartment(
            Long departmentId
    ) {

        validateDepartment(
                departmentId
        );

        return mentorProfileRepository
                .findByDepartmentIdAndActiveTrue(
                        departmentId
                )
                .stream()
                .map(
                        mentor -> {

                            UserResponse user =
                                    getUser(
                                            mentor.getUserId()
                                    );

                            return mapToResponse(
                                    mentor,
                                    user
                            );
                        }
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorResponse> getByType(
            MentorType mentorType
    ) {

        return mentorProfileRepository
                .findByMentorTypeAndActiveTrue(
                        mentorType
                )
                .stream()
                .map(
                        mentor -> {

                            UserResponse user =
                                    getUser(
                                            mentor.getUserId()
                                    );

                            return mapToResponse(
                                    mentor,
                                    user
                            );
                        }
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MentorResponse> search(
            String skill
    ) {

        if (skill == null ||
                skill.isBlank()) {

            return getAll();
        }

        return mentorSkillRepository
                .findAll()
                .stream()
                .filter(
                        mentorSkill ->
                                mentorSkill
                                        .getSkillName()
                                        .equalsIgnoreCase(
                                                skill.trim()
                                        )
                )
                .map(
                        MentorSkill::getMentorId
                )
                .distinct()
                .map(
                        mentorProfileRepository::findById
                )
                .filter(
                        java.util.Optional::isPresent
                )
                .map(
                        java.util.Optional::get
                )
                .filter(
                        mentor ->
                                Boolean.TRUE.equals(
                                        mentor.getActive()
                                )
                )
                .map(
                        mentor -> {

                            UserResponse user =
                                    getUser(
                                            mentor.getUserId()
                                    );

                            return mapToResponse(
                                    mentor,
                                    user
                            );
                        }
                )
                .toList();
    }

    @Override
    public MentorResponse update(
            Long id,
            MentorRequest request
    ) {

        MentorProfile mentor =
                findById(id);

        UserResponse user =
                getUser(request.getUserId());

        // Prevent assigning another existing mentor profile
        if (!mentor.getUserId()
                .equals(request.getUserId())) {

            if (mentorProfileRepository.existsByUserId(
                    request.getUserId()
            )) {

                throw new ResourceAlreadyExistsException(
                        "Another mentor profile already exists for this user"
                );
            }
        }

        // Validate department
        if (request.getDepartmentId() != null) {
            validateDepartment(
                    request.getDepartmentId()
            );
        }

        // Validate mentor type and price
        validateMentorTypeAndPrice(
                request.getMentorType(),
                request.getPrice()
        );

        BigDecimal finalPrice =
                request.getMentorType() == MentorType.PAID
                        ? request.getPrice()
                        : BigDecimal.ZERO;

        // User
        mentor.setUserId(
                request.getUserId()
        );

        // Academic
        mentor.setDepartmentId(
                request.getDepartmentId()
        );

        mentor.setYear(
                request.getYear()
        );

        // Professional
        mentor.setTitle(
                request.getTitle()
        );

        mentor.setExperience(
                request.getExperience()
        );

        mentor.setCompany(
                request.getCompany()
        );

        mentor.setDesignation(
                request.getDesignation()
        );

        // Profile
        mentor.setBio(
                request.getBio()
        );

        mentor.setProfileImage(
                request.getProfileImage()
        );

        // Social
        mentor.setGithub(
                request.getGithub()
        );

        mentor.setLinkedin(
                request.getLinkedin()
        );

        // Mentorship
        mentor.setMentorType(
                request.getMentorType()
        );

        mentor.setPrice(
                finalPrice
        );

        MentorProfile updated =
                mentorProfileRepository.save(
                        mentor
                );

        // Update skills
        mentorSkillRepository.deleteByMentorId(id);

        saveSkills(
                id,
                request.getSkills()
        );

        return mapToResponse(
                updated,
                user
        );
    }

    @Override
    public void deactivate(
            Long id
    ) {

        MentorProfile mentor =
                findById(id);

        mentor.setActive(false);

        mentorProfileRepository.save(
                mentor
        );
    }

    @Override
    public void delete(
            Long id
    ) {

        MentorProfile mentor =
                findById(id);

        mentorSkillRepository
                .deleteByMentorId(id);

        mentorProfileRepository
                .delete(mentor);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserId(Long mentorId) {
        MentorProfile mentor =
                findById(mentorId);

        return mentor.getUserId();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMentorProfileIdByUserId(Long userId) {

        MentorProfile mentor = mentorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Mentor profile not found for userId: " + userId
                        )
                );

        return mentor.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasChatAccess(
            Long studentId,
            Long mentorId
    ) {

        MentorProfile mentor =
                mentorProfileRepository.findById(mentorId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Mentor not found with id: "
                                                + mentorId
                                )
                        );

        return Boolean.TRUE.equals(mentor.getActive());
    }

    private MentorProfile findById(
            Long id
    ) {

        return mentorProfileRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Mentor not found with id: "
                                                + id
                                )
                );
    }

    private UserResponse getUser(Long userId) {
        try {
            return userClient.getUserById(userId);
        } catch (Exception ex) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }
    }

    private void validateDepartment(
            Long departmentId
    ) {

        try {

            departmentClient
                    .getDepartmentById(
                            departmentId
                    );

        } catch (Exception ex) {

            throw new ResourceNotFoundException(
                    "Department not found with id: "
                            + departmentId
            );
        }
    }

    private void validateMentorTypeAndPrice(
            MentorType type,
            BigDecimal price
    ) {

        if (type == null) {

            throw new IllegalArgumentException(
                    "Mentor type is required"
            );
        }

        if (type == MentorType.PAID &&
                (
                        price == null ||
                        price.compareTo(
                                BigDecimal.ZERO
                        ) <= 0
                )
        ) {

            throw new IllegalArgumentException(
                    "Paid mentor must have a valid price"
            );
        }
    }

    private void saveSkills(
            Long mentorId,
            List<String> skills
    ) {

        if (skills == null ||
                skills.isEmpty()) {

            return;
        }

        skills.stream()
                .filter(
                        skill ->
                                skill != null &&
                                !skill.isBlank()
                )
                .map(
                        String::trim
                )
                .distinct()
                .forEach(
                        skill -> {

                            MentorSkill mentorSkill =
                                    MentorSkill.builder()
                                            .mentorId(
                                                    mentorId
                                            )
                                            .skillName(
                                                    skill
                                            )
                                            .build();

                            mentorSkillRepository.save(
                                    mentorSkill
                            );
                        }
                );
    }

    private MentorResponse mapToResponse(
            MentorProfile mentor,
            UserResponse user
    ) {

        List<String> skills =
                mentorSkillRepository
                        .findByMentorId(
                                mentor.getId()
                        )
                        .stream()
                        .map(
                                MentorSkill::getSkillName
                        )
                        .toList();

        String departmentName = null;

        if (mentor.getDepartmentId() != null) {

            try {

                DepartmentResponse department =
                        departmentClient
                                .getDepartmentById(
                                        mentor.getDepartmentId()
                                );

                departmentName =
                        department.getName();

            } catch (Exception ignored) {
                // Keep response usable if department
                // service is temporarily unavailable.
            }
        }

        return MentorResponse.builder()

                .id(mentor.getId())

                .userId(mentor.getUserId())
                .name(user.getName())
                .email(user.getEmail())

                .departmentId(mentor.getDepartmentId())
                .departmentName(departmentName)
                .year(mentor.getYear())

                .title(mentor.getTitle())
                .experience(mentor.getExperience())
                .company(mentor.getCompany())
                .designation(mentor.getDesignation())

                .bio(mentor.getBio())
                .profileImage(mentor.getProfileImage())

                .skills(skills)

                .github(mentor.getGithub())
                .linkedin(mentor.getLinkedin())

                .mentorType(mentor.getMentorType())
                .price(mentor.getPrice())

                .active(mentor.getActive())

                .createdAt(mentor.getCreatedAt())
                .updatedAt(mentor.getUpdatedAt())

                .build();
    }

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void debugJwtSecret() {
        System.out.println(
                "JWT SECRET LENGTH = " + secret.length()
        );
    }
}