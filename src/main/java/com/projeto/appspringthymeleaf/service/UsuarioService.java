package com.projeto.appspringthymeleaf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.PerfilRepository;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;
import com.projeto.appspringthymeleaf.util.PasswordGeneratorService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// @Autowired
	// private JwtService jwtService;

	@Autowired
	private PasswordGeneratorService passwordGeneratorService;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private HttpServletRequest request;

	public void save(UsuarioModel model) {
		usuarioRepository.save(model);
	}

	public void delete(UsuarioModel model) {
		usuarioRepository.delete(model);
	}

	public void update(Long id, UsuarioModel model) {
		UsuarioModel user = usuarioRepository.getReferenceById(id);
		user.setNome(model.getNome());
		user.setEmail(model.getEmail());
		usuarioRepository.save(user);
	}

	public void deleteById(Long id) {
		getById(id);
		usuarioRepository.deleteById(id);
	}

	public UsuarioModel getById(Long id) {
		usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
		return usuarioRepository.findById(id).get();
	}

	public List<UsuarioModel> getAll() {
		return usuarioRepository.findAll();
	}

	public Long[] getSelectedPerfilIds(Long usuarioId) {
		UsuarioModel usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
		List<PerfilModel> perfis = usuario.getPerfis();
		Long[] ids = new Long[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			ids[i] = perfis.get(i).getId();
		}
		return ids;
	}

	public void salvarUsuarioPerfis(Long usuarioId, Long[] selectedPerfilIds) {
		UsuarioModel usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

		usuario.getPerfis().clear();

		if (selectedPerfilIds != null) {
			for (Long perfilId : selectedPerfilIds) {
				PerfilModel perfil = perfilRepository.findById(perfilId)
						.orElseThrow(() -> new RuntimeException("Perfil não encontrado!"));
				usuario.getPerfis().add(perfil);
			}
		}

		usuarioRepository.save(usuario);
	}

	public String getBaseUrl() {
		String baseUrl = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		return baseUrl.substring(0, baseUrl.indexOf(contextPath) + contextPath.length());
	}

	public String getUsuarioAutenticado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName(); // Retorna o nome de usuário do usuário autenticado
		} else {
			return null; // Retorna null se o usuário não estiver autenticado
		}
	}

	public void senhaRecuperar(String email) throws MessagingException {
		UsuarioModel usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
		String senhaNova = passwordGeneratorService.generateRandomPassword();
		String linkAcesso = getBaseUrl() + "/usuario/senha-alterar";
		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuarioRepository.save(usuario);
		String corpoEmail = "<html><body>" +
				"<p>Olá,</p>" +
				"<p>Você solicitou uma recuperação de senha. Aqui está sua nova senha: <strong>" + senhaNova
				+ "</strong></p>" +
				"<p>Recomandamos que altere a sua senha." +
				"<p>Para acessar sua conta, clique no link abaixo:</p>" +
				"<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
				"<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
				"<p>Obrigado.</p>" +
				"</body></html>";
		emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha", corpoEmail, true);
	}

	public void senhaAlterar(String senhaAtual, String senhaNova, String senhaNovaConfirmacao) {
		if (!senhaNova.equals(senhaNovaConfirmacao)) {
			throw new RuntimeException("As senhas novas informadas sao diferentes!");
		}
		String usuarioAutenticado = getUsuarioAutenticado();
		Optional<UsuarioModel> usuarioOptional = usuarioRepository.findByEmail(usuarioAutenticado);
		if (usuarioOptional == null) {
			throw new RuntimeException("Usuário '" + usuarioAutenticado + "' não encontrado!");
		}
		UsuarioModel usuario = usuarioOptional.get();
		if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
			throw new RuntimeException("Senha atual incorreta!");
		}
		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuario.setToken(null);
		usuarioRepository.save(usuario);
	}
}
