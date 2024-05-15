package com.projeto.appspringthymeleaf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.appspringthymeleaf.dto.UsuarioDTO;
import com.projeto.appspringthymeleaf.mapper.UsuarioMapper;
import com.projeto.appspringthymeleaf.model.PerfilModel;
import com.projeto.appspringthymeleaf.model.UsuarioModel;
import com.projeto.appspringthymeleaf.repository.PerfilRepository;
import com.projeto.appspringthymeleaf.repository.UsuarioRepository;

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

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordGeneratorService passwordGeneratorService;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private HttpServletRequest request;

	@Value("${page.size}")
	private Integer pageSize;

	public void save(UsuarioDTO dto) throws MessagingException {
		UsuarioModel model = UsuarioMapper.converterParaModel(dto);
		model.setSenha(passwordEncoder.encode(passwordGeneratorService.generateRandomPassword()));
		usuarioRepository.save(model);
		senhaRecuperar(model.getEmail());
	}

	public void delete(UsuarioDTO dto) {
		UsuarioModel model = UsuarioMapper.converterParaModel(dto);
		usuarioRepository.delete(model);
	}

	public void update(Long id, UsuarioDTO dto) {
		UsuarioModel model = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário com id " + id + " não encontrado!"));
		model.setNome(dto.getNome());
		model.setEmail(dto.getEmail());
		usuarioRepository.save(model);
	}

	public void deleteById(Long id) {
		if (usuarioRepository.existsById(id)) {
			throw new RuntimeException("Usuário com id " + id + " não encontrado!");
		}
		usuarioRepository.deleteById(id);
	}

	public UsuarioDTO getById(Long id) {
		UsuarioModel model = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário com id " + id + " não encontrado!"));
		return UsuarioMapper.converterParaDTO(model);
	}

	public List<UsuarioDTO> getAll() {
		return UsuarioMapper.converterParaDTOList(usuarioRepository.findAll());
	}

	public Long[] getSelectedPerfilIds(Long usuarioId) {
		UsuarioModel model = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuário com id " + usuarioId + " não encontrado!"));
		List<PerfilModel> perfis = model.getPerfis();
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

	private String getCorpoEmailRecuperacao(String token) {
		String linkAcesso = getBaseUrl() + "/public/senha/alterar/" + token;
		String corpoEmail = "<html><body>" +
				"<p>Olá,</p>" +
				"<p>Você solicitou uma recuperação de senha.</p>" +
				"<p>Para criar uma nova senha, clique no link abaixo:</p>" +
				"<p><a href=\"" + linkAcesso + "\">Alterar Senha</a></p>" +
				"<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
				"<p>Obrigado.</p>" +
				"</body></html>";
		return corpoEmail;
	}

	public void senhaRecuperar(String email) throws MessagingException {
		UsuarioModel usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuário com e-mail não encontrado."));
		String token = jwtService.gerarToken(email);
		usuario.setToken(token);
		usuarioRepository.save(usuario);
		emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha", getCorpoEmailRecuperacao(token), true);
	}

	public void senhaAlterar(String token, String senhaNova, String senhaNovaConfirmacao) {
		if (!senhaNova.equals(senhaNovaConfirmacao)) {
			throw new RuntimeException("As senhas informadas sao diferentes.");
		}
		if (!jwtService.validarToken(token)) {
			throw new RuntimeException(
					"Recuperação senha inválida ou expirada. Gere nova recuperação para alterar a senha.");
		}
		UsuarioModel usuario = usuarioRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Usuário com recuperação de senha não encontrado."));
		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuario.setToken(null);
		usuarioRepository.save(usuario);
	}

	public Page<UsuarioDTO> getListaPaginada(int page) {
		Page<UsuarioModel> listaPaginada = usuarioRepository.findAll(PageRequest.of(page, pageSize));
		return UsuarioMapper.converterParaDTOPage(listaPaginada);
	}
}
