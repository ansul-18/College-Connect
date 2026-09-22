package com.cllg.college_service.service;

import com.cllg.college_service.client.DepartmentClient;
import com.cllg.college_service.dto.response.DepartmentResponse;
import com.cllg.college_service.dto.request.EventRequest;
import com.cllg.college_service.dto.response.EventResponse;
import com.cllg.college_service.entity.Event;
import com.cllg.college_service.enums.EventStatus;
import com.cllg.college_service.enums.TargetType;
import com.cllg.college_service.exception.ResourceNotFoundException;
import com.cllg.college_service.repository.EventRegistrationRepository;
import com.cllg.college_service.repository.EventRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceIml implements EventService {

    private final EventRepository repository;

    private final EventRegistrationRepository registrationRepository;

    private final DepartmentClient departmentClient;

    private final CloudinaryService cloudinaryService;


    // =========================
    // CREATE EVENT
    // =========================

    @Override
    public EventResponse create(
            EventRequest request,
            MultipartFile image) {

        TargetType targetType =
                request.getTargetType();

        Long departmentId =
                request.getDepartmentId();


        if (targetType == null) {

            throw new IllegalArgumentException(
                    "Target type is required"
            );
        }


        if (targetType == TargetType.DEPARTMENT) {

            if (departmentId == null) {

                throw new IllegalArgumentException(
                        "Department is required for DEPARTMENT target"
                );
            }


            try {

                DepartmentResponse response = departmentClient.getDepartmentById(departmentId);

                if (response == null) {

                    throw new ResourceNotFoundException("Department not found with id: " + departmentId);
                }

            } catch (ResourceNotFoundException ex) {

                throw ex;

            } catch (Exception ex) {

                throw new ResourceNotFoundException("Unable to validate department: " + departmentId);
            }
        }


        if (targetType == TargetType.ALL_BRANCHES) {

            departmentId = null;
        }


        validateRegistrationLimit(request.getRegistrationRequired(), request.getRegistrationLimit());


        String imageUrl = null;


        if (image != null && !image.isEmpty()) {

            imageUrl = cloudinaryService.uploadImage(image, "college-connect/events");
        }


        Event event = Event.builder()

                        .title(request.getTitle().trim())
                        .description(request.getDescription())
                        .imageUrl(imageUrl)
                        .departmentId(departmentId)
                        .targetType(targetType)
                        .eventCategory(request.getCategory())
                        .date(request.getDate())
                        .time(request.getTime())
                        .venue(request.getVenue())
                        .registrationRequired(request.getRegistrationRequired())
                        .registrationLimit(request.getRegistrationLimit())
                        .eventStatus(request.getStatus() == null ? EventStatus.UPCOMING : request.getStatus())
                        .createdBy(request.getCreatedBy())
                        .build();


        Event saved = repository.save(event);


        return mapToResponse(saved);
    }


    // =========================
    // VALIDATE REGISTRATION LIMIT
    // =========================

    private void validateRegistrationLimit(Boolean required, Integer limit) {

        if (Boolean.TRUE.equals(required)
                && limit == null) {

            throw new IllegalArgumentException("Registration limit is required");
        }


        if (limit != null && limit <= 0) {

            throw new IllegalArgumentException("Registration limit must be greater than 0");
        }
    }


    // =========================
    // GET BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public EventResponse getById(
            Long id) {

        Event event = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));


        return mapToResponse(event);
    }


    // =========================
    // GET ALL
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getAll() {

        return repository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(event -> mapToResponse(event))
                .toList();
    }


    // =========================
    // GET BY STATUS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getByStatus(
            EventStatus status) {

        return repository
                .findByEventStatusOrderByDateAscTimeAsc(status)
                .stream()
                .map(event -> mapToResponse(event))
                .toList();
    }


    // =========================
    // UPDATE
    // =========================

    @Override
    public EventResponse update(
            Long id,
            EventRequest request,
            MultipartFile image) {

        Event event =
                repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));


        TargetType targetType = request.getTargetType();

        Long departmentId = request.getDepartmentId();


        if (targetType == null) {

            throw new IllegalArgumentException("Target type is required");
        }


        if (targetType == TargetType.DEPARTMENT) {

            if (departmentId == null) {

                throw new IllegalArgumentException("Department is required for DEPARTMENT target");
            }


            try {

                DepartmentResponse response = departmentClient.getDepartmentById(departmentId);


                if (response == null) {

                    throw new ResourceNotFoundException("Department not found with id: " + departmentId);
                }

            } catch (ResourceNotFoundException ex) {

                throw ex;

            } catch (Exception ex) {

                throw new ResourceNotFoundException("Unable to validate department: " + departmentId);
            }
        }


        if (targetType == TargetType.ALL_BRANCHES) {

            departmentId = null;
        }


        validateRegistrationLimit(request.getRegistrationRequired(), request.getRegistrationLimit());


        event.setTitle(request.getTitle().trim());

        event.setDescription(request.getDescription());

        event.setDepartmentId(departmentId);

        event.setTargetType(targetType);

        event.setEventCategory(request.getCategory());

        event.setDate(request.getDate());

        event.setTime(request.getTime());

        event.setVenue(request.getVenue());

        event.setRegistrationRequired(request.getRegistrationRequired());

        event.setRegistrationLimit(request.getRegistrationLimit());


        if (request.getStatus() != null) {

            event.setEventStatus(request.getStatus());
        }


        if (image != null && !image.isEmpty()) {

            String imageUrl = cloudinaryService.uploadImage(image, "college-connect/events");
            event.setImageUrl(imageUrl);
        }

        Event updated = repository.save(event);

        return mapToResponse(updated);
    }


    // =========================
    // DELETE
    // =========================

    @Override
    public void delete(Long id) {

        Event event = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));

        repository.delete(event);
    }


    // ENTITY TO RESPONSE
    // =========================

    private EventResponse mapToResponse(Event event) {

        long count = registrationRepository.countByEventId(event.getId());

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .departmentId(event.getDepartmentId())
                .targetType(event.getTargetType())
                .category(event.getEventCategory())
                .date(event.getDate())
                .time(event.getTime())
                .venue(event.getVenue())
                .registrationRequired(event.isRegistrationRequired())
                .registrationLimit(event.getRegistrationLimit())
                .status(event.getEventStatus())
                .createdBy(event.getCreatedBy())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .registrationCount(count)
                .build();
    }
}