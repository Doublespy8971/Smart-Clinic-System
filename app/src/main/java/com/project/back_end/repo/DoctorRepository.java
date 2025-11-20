package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // 1. Find a doctor by email
    Doctor findByEmail(String email);

    // 2. Find doctors whose name contains the provided string (case-sensitive)
    List<Doctor> findByNameLike(String name);

    // 3. Find doctors whose name contains the string (case-insensitive) and specialty matches (case-insensitive)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    // 4. Find doctors by specialty (case-insensitive)
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
