package com.projeto.appspringthymeleaf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.appspringthymeleaf.dto.ClienteDTO;
import com.projeto.appspringthymeleaf.dto.MunicipioDTO;
import com.projeto.appspringthymeleaf.dto.ViaCepDTO;
import com.projeto.appspringthymeleaf.record.AlertRecord;
import com.projeto.appspringthymeleaf.service.ClienteService;
import com.projeto.appspringthymeleaf.service.EstadoService;
import com.projeto.appspringthymeleaf.service.MunicipioService;
import com.projeto.appspringthymeleaf.service.RelatorioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private RelatorioService relatorioService;

	@GetMapping("")
	public String ini(@ModelAttribute("cliente") ClienteDTO cliente,
			@ModelAttribute("alertRecord") AlertRecord alertRecord, Model model,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "btnBusca", required = false, defaultValue = "") String btnBusca,
			@RequestParam(name = "nomeBusca", required = false, defaultValue = "") String nomeBusca,
			HttpServletResponse response) {
		model.addAttribute("cliente", cliente);
		model.addAttribute("estados", estadoService.getAll());

		List<MunicipioDTO> municipios = null;
		if (cliente.getMunicipio() != null && cliente.getMunicipio().getId() != null) {
			municipios = municipioService.getMunicipiosByEstadoId(cliente.getMunicipio().getEstado().getId());
		}
		model.addAttribute("municipios", municipios);

		if (btnBusca.equals("busca")) {
			model.addAttribute("nomeBusca", nomeBusca);
			model.addAttribute("listaPaginada", clienteService.getListaPaginadaByNomeEmail(nomeBusca, 0));
		} else {
			model.addAttribute("listaPaginada", clienteService.getListaPaginada(page));
		}
		return "cliente";
	}

	@GetMapping("/novo")
	public String novo(RedirectAttributes redirectAttributes) {
		return "redirect:/cliente";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		ClienteDTO cliente = clienteService.getById(id);
		redirectAttributes.addFlashAttribute("cliente", cliente);
		return "redirect:/cliente";
	}

	@PostMapping("")
	public String salvar(@Valid ClienteDTO cliente, RedirectAttributes redirectAttributes) {
		if (cliente.getId() == null) {
			clienteService.save(cliente);
		} else {
			clienteService.update(cliente.getId(), cliente);
		}
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Usuário salvo com sucesso!"));
		redirectAttributes.addFlashAttribute("cliente", new ClienteDTO());
		return "redirect:/cliente";
	}

	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		clienteService.deleteById(id);
		redirectAttributes.addFlashAttribute("alertRecord",
				new AlertRecord("success", "Sucesso!", "Usuário excluído com sucesso!"));
		return "redirect:/cliente";
	}

	@PostMapping("/viacep")
	@ResponseBody
	public ViaCepDTO viaCep(@RequestBody String cep, Model model) {
		try {
			ViaCepDTO viaCepDTO = clienteService.getViaCepMunicipios(cep);
			return viaCepDTO;
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/viewTelefone/{id}")
	public String viewTelefone(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("clienteId", id);
		return "redirect:/telefone";
	}

	@GetMapping("/imprimir")
	public void imprimir(@RequestParam(name = "btnBusca", required = false, defaultValue = "") String btnBusca,
			@RequestParam(name = "nomeBusca", required = false, defaultValue = "") String nomeBusca,
			HttpServletResponse response) {
		try {
			String nomeRelatorio = "RelClientes";
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("P_BUSCA", nomeBusca);
			// Chamar o serviço para gerar o relatório
			JasperPrint jasperPrint = relatorioService.getJaperReport(nomeRelatorio, parametros);

			// Renderizar o relatório em formato PDF (ou outro formato, conforme necessário)
			response.setContentType("application/pdf");
			// Configurar a resposta para download do relatório
			response.setHeader("Content-Disposition", "attachment; filename=relatorio.pdf");
			// Configurar a resposta para abrir o relatório no navegador
			// response.setHeader("Content-Disposition", "inline; filename=relatorio.pdf");
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar o relatório: " + e.getMessage());
		}
	}
}
