package br.com.fiap.MottuSenseMvc.controller;

import br.com.fiap.MottuSenseMvc.model.Credentials;
import br.com.fiap.MottuSenseMvc.model.Token;
import br.com.fiap.MottuSenseMvc.model.User;
import br.com.fiap.MottuSenseMvc.service.AuthService;
import br.com.fiap.MottuSenseMvc.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(
            summary = "Realiza login do usuário e gera token JWT",
            description = "Autentica o usuário com email e senha. Retorna um token JWT se as credenciais estiverem corretas."
    )
    public Token login(@RequestBody Credentials credentials){
        User user = (User) authService.loadUserByUsername(credentials.email());
        if (!passwordEncoder.matches(credentials.password(), user.getPassword())){
            throw new BadCredentialsException("Senha incorreta");
        }
        return tokenService.createToken(user);
    }

}
