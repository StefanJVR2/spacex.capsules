package com.capgemini.spacex.capsules.repositories;

import com.capgemini.spacex.capsules.repositories.models.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapsuleRepository extends JpaRepository<Capsule, String> {
}
