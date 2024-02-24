package com.projeto.appspringthymeleaf.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projeto.appspringthymeleaf.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    // Rotas que devem ser ignoradas
    private final List<String> rotasExcecoes = Arrays.asList("/js", "/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Verifique se a rota atual está na lista de exceções
        if (rotasExcecoes.stream().anyMatch(request.getServletPath()::startsWith)) {
            // Se estiver na lista de exceções, não processe o token JWT
            filterChain.doFilter(request, response);
            return;
        }

        // Obtenha o token JWT do cabeçalho Authorization
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Remova o prefixo "Bearer "

            try {
                String usuarioEmail = jwtService.extrairUsuarioEmail(token);
                userDetailsService.loadUserByUsername(usuarioEmail);

                // UsuarioModel usuario = usuarioRepository.getReferenceById(usuarioId);
                // Authentication authentication = new
                // UsernamePasswordAuthenticationToken(usuario, usuario.getSenha(),
                // usuario.getAuthorities());
                // SecurityContextHolder.getContext().setAuthentication(authentication);

                // // Configure o contexto de segurança com base nas informações do token
                // JwtAuthenticationToken authentication = new
                // JwtAuthenticationToken(claims.getSubject(), null, null);
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Em caso de erro ao processar o token, não faça nada
            }
        }

        // Continue a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
