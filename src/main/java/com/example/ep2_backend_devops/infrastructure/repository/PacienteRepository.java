package com.example.ep2_backend_devops.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ep2_backend_devops.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
