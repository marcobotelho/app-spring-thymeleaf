package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.record.RedefinirSenhaRecord;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;
import com.projeto.appspringthymeleaf.util.PasswordGeneratorService;

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

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordGeneratorService passwordGeneratorService;

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
		String senhaNova = passwordGeneratorService.generateRandomPassword();
		String token = jwtService.gerarToken(usuario.getEmail());
		String linkAcesso = baseURL + "/redefinir-senha/" + token;
		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuario.setToken(token);
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

	public void redefinirSenha(RedefinirSenhaRecord formSenha) {
		String usuarioEmail = jwtService.extrairUsuarioEmail(formSenha.token());
		UsuarioModel usuario = usuarioRepository.findByEmail(usuarioEmail).get();
		if (!formSenha.senhaNova().equals(formSenha.senhaNovaConfirmacao())) {
			throw new RuntimeException("As senhas novas informadas sao diferentes!");
		}
		if (!formSenha.token().equals(usuario.getToken())) {
			throw new RuntimeException("Recuperação de senha desatualizada! Gere nova recuperação de senha.");
		}
		if (!passwordEncoder.matches(formSenha.senhaAtual(), usuario.getSenha())) {
			throw new RuntimeException("Senha atual incorreta!");
		}
		usuario.setSenha(passwordEncoder.encode(formSenha.senhaNova()));
		usuario.setToken(null);
		usuarioRepository.save(usuario);
	}
}
