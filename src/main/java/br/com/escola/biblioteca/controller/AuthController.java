package br.com.escola.biblioteca.controller;

import br.com.escola.biblioteca.dto.CadastroRequestDTO;
import br.com.escola.biblioteca.dto.CadastroResponseDTO;
import br.com.escola.biblioteca.dto.LoginRequestDTO;
import br.com.escola.biblioteca.dto.LoginResponseDTO;
import br.com.escola.biblioteca.security.JwtUtil;
import br.com.escola.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Cadastro, login e geração de token JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Operation(
        summary = "Cadastro de usuário",
        description = "Cria um novo usuário com nome, e-mail e senha. Retorna os dados do usuário criado."
    )
    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponseDTO> cadastrar(@Valid @RequestBody CadastroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(dto));
    }

    @Operation(
        summary = "Login",
        description = "Autentica o usuário com e-mail e senha, retorna um token JWT."
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
                    Map.of("erro", "Credenciais inválidas", "mensagem", "E-mail ou senha incorretos")
            );
        }
    }
}
