package com.projeto.appspringthymeleaf.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResourceLoader resourceLoader;

    public String getResourcePath(String resourceName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceName);
        return resource.getFile().getParent() + File.separator + resourceName + File.separator;
    }

    public void gerarRelatorio(String nomeRelatorio, Map<String, Object> parametros,
            HttpServletResponse response) throws Exception {

        String resourceDir = "relatorios";
        String resourceFile = "/" + resourceDir + "/" + nomeRelatorio + ".jasper";

        Connection connection = applicationContext.getBean(DataSource.class).getConnection();

        // Carregar o arquivo .jasper do relatório compilado
        InputStream inputStream = this.getClass().getResourceAsStream(resourceFile);

        // Passa o caminho do relatório para o parâmetro SUBREPORT_DIR
        parametros.put("SUBREPORT_DIR", getResourcePath(resourceDir));

        // Preencher o relatório com os dados
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, connection);

        // Renderizar o relatório em formato PDF (ou outro formato, conforme necessário)
        response.setContentType("application/pdf");
        // Configurar a resposta para download do relatório
        response.setHeader("Content-Disposition", "attachment; filename=relatorio.pdf");
        // Configurar a resposta para abrir o relatório no navegador
        // response.setHeader("Content-Disposition", "inline; filename=relatorio.pdf");
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    public JasperPrint getJaperReport(String nomeRelatorio, Map<String, Object> parametros) {

        String resourceDir = "relatorios";
        String resourceFile = "/" + resourceDir + "/" + nomeRelatorio + ".jasper";

        try {
            Connection connection = applicationContext.getBean(DataSource.class).getConnection();

            // Carregar o arquivo .jasper do relatório compilado
            InputStream inputStream = this.getClass().getResourceAsStream(resourceFile);

            // Passa o caminho do relatório para o parâmetro SUBREPORT_DIR
            parametros.put("SUBREPORT_DIR", getResourcePath(resourceDir));

            // Preencher o relatório com os dados
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, connection);

            return jasperPrint;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar relatório: " + e.getLocalizedMessage());
        }
    }
}
