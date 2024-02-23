package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;

import jakarta.mail.MessagingException;
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

	public void resetPassword(Long usuarioId, String baseURL) throws MessagingException {
		UsuarioModel usuario = getById(usuarioId);
		String senhaNova = "123";
		String linkAcesso = baseURL + "/login";
		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuarioRepository.save(usuario);
		String corpoEmail = "<html><body>" +
				"<p>Olá,</p>" +
				"<p>Você solicitou uma recuperação de senha. Aqui está sua nova senha: <strong>" + senhaNova
				+ "</strong></p>" +
				"<p>Para acessar sua conta, clique no link abaixo:</p>" +
				"<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
				"<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
				"<p>Obrigado.</p>" +
				"</body></html>";
		emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha", corpoEmail, true);
	}
}
