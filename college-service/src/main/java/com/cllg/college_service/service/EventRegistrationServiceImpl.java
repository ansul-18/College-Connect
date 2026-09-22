package com.cllg.college_service.service;

import com.cllg.college_service.dto.response.RegistrationResponse;
import com.cllg.college_service.entity.Event;
import com.cllg.college_service.entity.EventRegistration;
import com.cllg.college_service.enums.EventStatus;
import com.cllg.college_service.enums.RegistrationStatus;
import com.cllg.college_service.exception.ResourceAlreadyExistsException;
import com.cllg.college_service.exception.ResourceNotFoundException;
import com.cllg.college_service.repository.EventRegistrationRepository;
import com.cllg.college_service.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EventRegistrationServiceImpl implements EventRegistrationService{

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    @Override
    public RegistrationResponse register(Long eventId, Long studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        if (event.getEventStatus() == EventStatus.COMPLETED || event.getEventStatus() == EventStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Registration is closed for this event"
            );
        }

        if (!Boolean.TRUE.equals(event.isRegistrationRequired())) {

            throw new IllegalArgumentException("Registration is not required for this event");
        }


        registrationRepository.findByEventIdAndStudentId(eventId, studentId)
                .ifPresent(registration -> {

                    if (registration.getStatus() == RegistrationStatus.REGISTERED) {
                                throw new ResourceAlreadyExistsException("Student is already registered");
                            }
                        }
                );

        long currentCount = registrationRepository.countByEventIdAndStatus(eventId, RegistrationStatus.REGISTERED);

        if (event.getRegistrationLimit() != null
                && currentCount >=
                event.getRegistrationLimit()) {

            throw new IllegalArgumentException(
                    "Event registration limit reached"
            );
        }


        EventRegistration registration =
                registrationRepository
                        .findByEventIdAndStudentId(
                                eventId,
                                studentId
                        )
                        .orElseGet(
                                () ->
                                        EventRegistration
                                                .builder()
                                                .eventId(eventId)
                                                .studentId(studentId)
                                                .build()
                        );


        registration.setStatus(
                RegistrationStatus.REGISTERED
        );


        EventRegistration saved =
                registrationRepository
                        .save(registration);


        return mapToResponse(saved);
    }


    @Override
    public void cancel(Long eventId, Long studentId) {
        EventRegistration registration = registrationRepository
                        .findByEventIdAndStudentId(eventId, studentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

        registration.setStatus(RegistrationStatus.CANCELLED);

        registrationRepository.save(registration);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getByEvent(Long eventId) {

        return registrationRepository
                .findByEventIdOrderByRegisteredAtDesc(eventId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getByStudent(Long studentId) {

        return registrationRepository
                .findByStudentIdOrderByRegisteredAtDesc(
                        studentId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RegistrationResponse mapToResponse(
            EventRegistration entity) {

        return RegistrationResponse
                .builder()

                .id(entity.getId())

                .eventId(
                        entity.getEventId()
                )

                .studentId(
                        entity.getStudentId()
                )

                .registeredAt(
                        entity.getRegisteredAt()
                )

                .status(
                        entity.getStatus()
                )

                .build();
    }
}
