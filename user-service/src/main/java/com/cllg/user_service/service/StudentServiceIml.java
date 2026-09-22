package com.cllg.user_service.service;

import com.cllg.user_service.client.DepartmentClient;
import com.cllg.user_service.client.DepartmentResponse;
import com.cllg.user_service.dto.request.LinkAuthAccountRequest;
import com.cllg.user_service.dto.request.StudentRequest;
import com.cllg.user_service.dto.response.StudentResponse;
import com.cllg.user_service.entity.Student;
import com.cllg.user_service.enums.AccountStatus;
import com.cllg.user_service.exception.ResourceAlreadyExistsException;
import com.cllg.user_service.exception.ResourceNotFoundException;
import com.cllg.user_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceIml implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentClient departmentClient;


    // =========================================================
    // CREATE STUDENT
    // =========================================================
    @Override
    public StudentResponse createStudent(StudentRequest request) {

        validateDepartment(request.getDepartmentId());

        String email = normalizeEmail(request.getEmail());
        String rollNumber = normalizeRollNumber(request.getRollNumber());

        // Check duplicate email
        if (studentRepository.existsByEmailIgnoreCase(email)) {
            throw new ResourceAlreadyExistsException(
                    "Student already exists with email: " + email
            );
        }

        // Check duplicate roll number
        if (studentRepository.existsByRollNumberIgnoreCase(rollNumber)) {
            throw new ResourceAlreadyExistsException(
                    "Student already exists with Roll Number: " + rollNumber
            );
        }

        Student student = Student.builder()
                .name(normalize(request.getName()))
                .email(email)
                .rollNumber(rollNumber)
                .mobile(normalize(request.getMobile()))
                .bio(normalize(request.getBio()))
                .year(request.getYear())
                .departmentId(request.getDepartmentId())
                .profileImage(normalize(request.getProfileImage()))
                .linkedin(normalize(request.getLinkedin()))
                .github(normalize(request.getGithub()))
                .accountStatus(AccountStatus.NOT_REGISTER)
                .build();

        Student saved = studentRepository.save(student);

        return mapToResponse(saved);
    }


    // =========================================================
    // GET STUDENT BY ID
    // =========================================================
    @Override
    public StudentResponse getStudentById(Long id) {

        Student student = findStudentById(id);

        return mapToResponse(student);
    }


    // =========================================================
    // FIND STUDENT ENTITY BY ID
    // =========================================================
    @Override
    public Student findStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + id
                        )
                );
    }


    // =========================================================
    // GET STUDENT BY AUTH USER ID
    // =========================================================
    @Override
    public StudentResponse getStudentByAuthUserId(Long authUserId) {

        Student student = studentRepository
                .findByAuthUserId(authUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found for auth user id: "
                                        + authUserId
                        )
                );

        return mapToResponse(student);
    }


    // =========================================================
    // GET ALL STUDENTS
    // =========================================================
    @Override
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET STUDENTS BY DEPARTMENT
    // =========================================================
    @Override
    public List<StudentResponse> getStudentsByDepartment(
            Long departmentId
    ) {

        validateDepartment(departmentId);

        return studentRepository
                .findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // VALIDATE DEPARTMENT
    // =========================================================
    @Override
    public void validateDepartment(Long departmentId) {

        DepartmentResponse departmentResponse =
                departmentClient.getDepartmentById(departmentId);

        if (departmentResponse == null) {

            throw new ResourceNotFoundException(
                    "Department not found with id: " + departmentId
            );
        }
    }


    // =========================================================
    // GET STUDENTS BY YEAR
    // =========================================================
    @Override
    public List<StudentResponse> getStudentsByYear(Integer year) {

        validateYear(year);

        return studentRepository
                .findByYear(year)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET STUDENTS BY DEPARTMENT + YEAR
    // =========================================================
    @Override
    public List<StudentResponse> getStudentsByDepartmentAndYear(
            Long departmentId,
            Integer year
    ) {

        validateDepartment(departmentId);
        validateYear(year);

        return studentRepository
                .findByDepartmentIdAndYear(
                        departmentId,
                        year
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // UPDATE STUDENT
    // =========================================================
    @Override
    public StudentResponse updateStudent(
            Long id,
            StudentRequest request
    ) {

        Student existingStudent =
                studentRepository.findStudentById(id);

        validateDepartment(request.getDepartmentId());

        String email = normalizeEmail(request.getEmail());
        String rollNumber = normalizeRollNumber(request.getRollNumber());


        // -----------------------------------------------------
        // Check duplicate email
        // -----------------------------------------------------
        Student studentWithEmail =
                studentRepository
                        .findByEmailIgnoreCase(email)
                        .orElse(null);

        if (studentWithEmail != null
                && !studentWithEmail.getId().equals(id)) {

            throw new ResourceAlreadyExistsException(
                    "Email already used by another student"
            );
        }




        // -----------------------------------------------------
        // Check duplicate roll number
        // -----------------------------------------------------
        Student studentWithRollNumber =
                studentRepository
                        .findByRollNumberIgnoreCase(rollNumber)
                        .orElse(null);

        /*
         * IMPORTANT:
         * Duplicate should be thrown when another student
         * has this roll number.
         */
        if (studentWithRollNumber != null
                && !studentWithRollNumber.getId().equals(id)) {

            throw new ResourceAlreadyExistsException(
                    "Roll number already used by another student"
            );
        }


        // -----------------------------------------------------
        // Update fields
        // -----------------------------------------------------
        existingStudent.setName(
                normalize(request.getName())
        );

        existingStudent.setEmail(email);

        existingStudent.setRollNumber(rollNumber);

        existingStudent.setMobile(
                normalize(request.getMobile())
        );

        existingStudent.setDepartmentId(
                request.getDepartmentId()
        );

        existingStudent.setProfileImage(
                normalize(request.getProfileImage())
        );

        existingStudent.setYear(
                request.getYear()
        );

        existingStudent.setBio(
                normalize(request.getBio())
        );

        existingStudent.setGithub(
                normalize(request.getGithub())
        );

        existingStudent.setLinkedin(
                normalize(request.getLinkedin())
        );


        Student updated =
                studentRepository.save(existingStudent);

        return mapToResponse(updated);
    }


    // =========================================================
    // LINK AUTH ACCOUNT
    // =========================================================
    @Override
    public StudentResponse linkAuthAccount(
            Long id,
            LinkAuthAccountRequest request
    ) {

        Student student = findStudentById(id);


        // Already linked
        if (student.getAuthUserId() != null) {

            throw new ResourceAlreadyExistsException(
                    "Student is already linked to an auth account"
            );
        }


        // Auth user already linked with another student
        if (studentRepository.existsByAuthUserId(
                request.getAuthUserId()
        )) {

            throw new ResourceAlreadyExistsException(
                    "Auth user is already linked to another student"
            );
        }


        student.setAuthUserId(
                request.getAuthUserId()
        );

        student.setAccountStatus(
                AccountStatus.REGISTER
        );


        Student updated =
                studentRepository.save(student);

        return mapToResponse(updated);
    }


    // =========================================================
    // CREATE MY PROFILE
    // =========================================================
    @Override
    public StudentResponse createMyProfile(
            Long authUserId,
            StudentRequest request
    ) {

        /*
         * 1. Validate department
         */
        validateDepartment(
                request.getDepartmentId()
        );


        /*
         * 2. Check whether this logged-in user
         *    already has a student profile
         */
        if (studentRepository
                .findByAuthUserId(authUserId)
                .isPresent()) {

            throw new ResourceAlreadyExistsException(
                    "Student profile already exists"
            );
        }


        /*
         * 3. Normalize email and roll number
         */
        String email =
                normalizeEmail(
                        request.getEmail()
                );

        String rollNumber =
                normalizeRollNumber(
                        request.getRollNumber()
                );


        /*
         * 4. Check duplicate email
         */
        if (studentRepository
                .existsByEmailIgnoreCase(email)) {

            throw new ResourceAlreadyExistsException(
                    "Student already exists with email: "
                            + email
            );
        }


        /*
         * 5. Check duplicate roll number
         */
        if (studentRepository
                .existsByRollNumberIgnoreCase(
                        rollNumber
                )) {

            throw new ResourceAlreadyExistsException(
                    "Student already exists with roll number: "
                            + rollNumber
            );
        }


        /*
         * 6. Build student profile
         */
        Student student =
                Student.builder()

                        // IMPORTANT:
                        // link profile with logged-in user
                        .authUserId(authUserId)

                        .name(
                                normalize(
                                        request.getName()
                                )
                        )

                        .email(email)

                        .mobile(
                                normalize(
                                        request.getMobile()
                                )
                        )

                        .rollNumber(rollNumber)

                        .departmentId(
                                request.getDepartmentId()
                        )

                        .year(
                                request.getYear()
                        )

                        .profileImage(
                                normalize(
                                        request.getProfileImage()
                                )
                        )

                        .bio(
                                normalize(
                                        request.getBio()
                                )
                        )

                        .github(
                                normalize(
                                        request.getGithub()
                                )
                        )

                        .linkedin(
                                normalize(
                                        request.getLinkedin()
                                )
                        )

                        /*
                         * Logged-in student creating
                         * their own profile
                         */
                        .accountStatus(
                                AccountStatus.REGISTER
                        )

                        .build();


        /*
         * 7. Save
         */
        Student saved =
                studentRepository.save(student);


        /*
         * 8. Return response
         */
        return mapToResponse(saved);
    }


    // =========================================================
    // DELETE STUDENT
    // =========================================================
    @Override
    public void deleteStudent(Long id) {

        Student student = findStudentById(id);

        studentRepository.delete(student);
    }


    // =========================================================
    // NORMALIZE EMAIL
    // =========================================================
    private String normalizeEmail(String email) {

        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase();
    }


    // =========================================================
    // NORMALIZE ROLL NUMBER
    // =========================================================
    private String normalizeRollNumber(String rollNumber) {

        if (rollNumber == null) {
            return null;
        }

        return rollNumber.trim().toUpperCase();
    }


    // =========================================================
    // NORMALIZE STRING
    // =========================================================
    private String normalize(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty()
                ? null
                : trimmed;
    }


    // =========================================================
    // VALIDATE YEAR
    // =========================================================
    private void validateYear(Integer year) {

        if (year == null || year < 1 || year > 4) {

            throw new IllegalArgumentException(
                    "Year must be between 1 and 4"
            );
        }
    }


    // =========================================================
    // MAP ENTITY -> RESPONSE
    // =========================================================
    private StudentResponse mapToResponse(
            Student student
    ) {

        String departmentName = null;


        // =========================================================
        // GET DEPARTMENT NAME
        // =========================================================
        if (student.getDepartmentId() != null) {

            try {

                DepartmentResponse department =
                        departmentClient.getDepartmentById(
                                student.getDepartmentId()
                        );

                if (department != null) {

                    departmentName =
                            department.getName();
                }

            } catch (Exception exception) {

                // Keep profile usable even if
                // Department Service is unavailable.

                departmentName = null;
            }
        }


        // =========================================================
        // BUILD RESPONSE
        // =========================================================
        return StudentResponse.builder()

                .id(
                        student.getId()
                )

                .authUserId(
                        student.getAuthUserId()
                )

                .name(
                        student.getName()
                )

                .email(
                        student.getEmail()
                )

                .mobile(
                        student.getMobile()
                )

                .rollNumber(
                        student.getRollNumber()
                )

                .departmentId(
                        student.getDepartmentId()
                )

                .departmentName(
                        departmentName
                )

                .year(
                        student.getYear()
                )

                .bio(
                        student.getBio()
                )

                .github(
                        student.getGithub()
                )

                .linkedin(
                        student.getLinkedin()
                )

                .profileImage(
                        student.getProfileImage()
                )

                .accountStatus(
                        student.getAccountStatus()
                )

                .createdAt(
                        student.getCreatedAt()
                )

                .updatedAt(
                        student.getUpdatedAt()
                )

                .build();
    }
}