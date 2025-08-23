package br.com.fiap.MottuSenseMvc.controller;

import br.com.fiap.MottuSenseMvc.dto.UserCreateDTO;
import br.com.fiap.MottuSenseMvc.dto.LoginDTO;
import br.com.fiap.MottuSenseMvc.model.User;
import br.com.fiap.MottuSenseMvc.model.UserRole;
import br.com.fiap.MottuSenseMvc.repository.UserRepository;
import br.com.fiap.MottuSenseMvc.service.TokenService;
import br.com.fiap.MottuSenseMvc.model.Token;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setNome(userCreateDTO.getNome());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        repository.save(user);

        Token token = tokenService.createToken(user);
        return ResponseEntity.ok(token.token());
    }



    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOpt = repository.findByEmail(loginDTO.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }
        if (user.getRole() == null) {
            user.setRole(UserRole.USER); // or another default role
        }
        Token token = tokenService.createToken(user);
        return ResponseEntity.ok(token);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> payload, HttpSession session) {
        String token = payload.get("token");
        System.out.println("Received token: [" + token + "]");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required.");
        }
        try {
            User user = tokenService.getUserFromToken(token);
            if (user == null) {
                System.out.println("Token validation failed.");
                return ResponseEntity.status(401).body("Invalid token.");
            }
            session.setAttribute("user", user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid token.");
        }
    }
}