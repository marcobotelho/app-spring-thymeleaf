package com.projeto.appspringthymeleaf.service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.projeto.appspringthymeleaf.model.EstadoModel;
import com.projeto.appspringthymeleaf.model.MunicipioModel;
import com.projeto.appspringthymeleaf.repository.EstadoRepository;
import com.projeto.appspringthymeleaf.repository.MunicipioRepository;

@Service
public class CsvServiceSql {

    @Value("${csv.file.path.estados}")
    private String filePathEstados;

    @Value("${csv.file.path.municipios}")
    private String filePathMunicipios;

    private Connection connection;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    public CsvServiceSql(@Autowired DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void lerCsvEInserirDados() throws IOException, CsvValidationException, SQLException {
        long tempoInicio = System.currentTimeMillis();
        inserirEstados();
        inserirMunicipios();
        long tempoFim = System.currentTimeMillis();
        long tempoExecucao = tempoFim - tempoInicio;
        System.out.println("Tempo de execução: " + tempoExecucao + "ms");
    }

    public void inserirEstados()
            throws IOException, CsvValidationException, SQLException {
        String query = "INSERT INTO ESTADO (ID, NOME, SIGLA) VALUES (?, ?, ?)";
        try (CSVReader csvReader = new CSVReader(
                new FileReader(filePathEstados, StandardCharsets.UTF_8))) {
            Set<Long> estadoIdsExistentes = new HashSet<>(estadoRepository.findAll().stream()
                    .map(EstadoModel::getId)
                    .collect(Collectors.toList()));
            csvReader.skip(1); // Skip the first line
            String[] linha;
            while ((linha = csvReader.readNext()) != null) {
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    Long estadoId = Long.parseLong(linha[0]);
                    String nome = linha[1];
                    String sigla = linha[2];
                    if (!estadoIdsExistentes.contains(estadoId)) {
                        ps.setLong(1, estadoId);
                        ps.setString(2, nome);
                        ps.setString(3, sigla);
                        ps.executeUpdate();
                    }
                }
            }
        }
    }

    public void inserirMunicipios()
            throws IOException, CsvValidationException, SQLException {
        String query = "INSERT INTO MUNICIPIO (ESTADO_ID, ID, NOME) VALUES (?, ?, ?)";
        try (CSVReader csvReader = new CSVReader(
                new FileReader(filePathMunicipios, StandardCharsets.UTF_8))) {
            Set<Long> municipioIdsExistentes = new HashSet<>(municipioRepository.findAll().stream()
                    .map(MunicipioModel::getId)
                    .collect(Collectors.toList()));
            csvReader.skip(1); // Skip the first line
            String[] linha;
            while ((linha = csvReader.readNext()) != null) {
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    Long estadoId = Long.parseLong(linha[0]);
                    Long municipioId = Long.parseLong(linha[1]);
                    String nome = linha[2];
                    if (!municipioIdsExistentes.contains(municipioId)) {
                        ps.setLong(1, estadoId);
                        ps.setLong(2, municipioId);
                        ps.setString(3, nome);
                        ps.executeUpdate();
                    }
                }
            }
        }
    }

}
