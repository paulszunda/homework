package io.fourfinanceit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.fourfinanceit.domain.LoanExtension;

public interface LoanExtensionRepository extends JpaRepository<LoanExtension, Long> {

}
