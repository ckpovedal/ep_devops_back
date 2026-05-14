package com.example.ep2_backend_devops.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ep2_backend_devops.domain.Paciente;
import com.example.ep2_backend_devops.infrastructure.repository.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public Paciente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
    }

    public Paciente crear(Paciente paciente) {
        paciente.setId(null);
        return repository.save(paciente);
    }

    public Paciente actualizar(Long id, Paciente paciente) {
        Paciente pacienteExistente = buscarPorId(id);
        pacienteExistente.setNombre(paciente.getNombre());
        pacienteExistente.setApellido(paciente.getApellido());
        return repository.save(pacienteExistente);
    }

    public void eliminar(Long id) {
        Paciente paciente = buscarPorId(id);
        repository.delete(paciente);
    }
}
