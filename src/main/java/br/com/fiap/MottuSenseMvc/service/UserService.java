package br.com.fiap.MottuSenseMvc.service;

import br.com.fiap.MottuSenseMvc.dto.UserCreateDTO;
import br.com.fiap.MottuSenseMvc.dto.UserDTO;
import br.com.fiap.MottuSenseMvc.exception.DuplicateResourceException;
import br.com.fiap.MottuSenseMvc.exception.ResourceNotFoundException;
import br.com.fiap.MottuSenseMvc.model.User;
import br.com.fiap.MottuSenseMvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getNome(), user.getEmail(), null))
                .collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado"));
        return new UserDTO(user.getId(), user.getNome(), user.getEmail(), null);
    }

    public UserDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("O email " + dto.getEmail() + " já está em uso");
        }
        User user = User.builder()
                .id(null)
                .nome(dto.getNome())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getNome(), user.getEmail(), null);
    }
}