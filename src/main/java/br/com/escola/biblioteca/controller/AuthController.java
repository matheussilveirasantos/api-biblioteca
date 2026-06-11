package br.com.escola.biblioteca.controller;

import br.com.escola.biblioteca.dto.LoginRequestDTO;
import br.com.escola.biblioteca.dto.LoginResponseDTO;
import br.com.escola.biblioteca.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
        summary = "Login",
        description = "Autentica o usuário e retorna um token JWT. " +
                      "Use: admin / admin123  ou  biblioteca / biblio@2025"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
            );
            String token = jwtUtil.gerarToken(auth.getName());
            return ResponseEntity.ok(LoginResponseDTO.of(token, auth.getName()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                    Map.of("erro", "Credenciais inválidas", "mensagem", "Usuário ou senha incorretos")
            );
        }
    }
}
