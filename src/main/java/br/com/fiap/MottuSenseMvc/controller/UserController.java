package br.com.fiap.MottuSenseMvc.controller;


import br.com.fiap.MottuSenseMvc.dto.UserCreateDTO;
import br.com.fiap.MottuSenseMvc.dto.UserDTO;
import br.com.fiap.MottuSenseMvc.model.User;
import br.com.fiap.MottuSenseMvc.repository.UserRepository;
import br.com.fiap.MottuSenseMvc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;







    @PostMapping
    @Operation(summary = "Cria um novo usuário com a senha criptografada", description = "Cria um usuário com nome, email, senha e role(ADMIN ou USER).")
    public ResponseEntity<User> create(@RequestBody @Valid UserCreateDTO dto) {
        User user = User.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
        User savedUser = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    //metodos de crud para usuario
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> usuarios = userService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO usuario = userService.findById(id);
        return ResponseEntity.ok(usuario);
    }



}