package com.projeto.appspringthymeleaf.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.appspringthymeleaf.dto.ViaCepDTO;

@Service
public class ViaCepService {

    public static ViaCepDTO buscaCep(String cep) {
        try {
            String urlStr = "https://viacep.com.br/ws/" + cep + "/json/";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));

            StringBuffer resultado = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                resultado.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            ViaCepDTO viaCepDTO = mapper.readValue(resultado.toString(), ViaCepDTO.class);

            return viaCepDTO;
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean validaCep(String cep) {
        if ((Pattern.matches(("[0-9]{5}-[0-9]{3}"), cep)) || (Pattern.matches(("[0-9]{5}[0-9]{3}"), cep))) {
            return true;
        } else {
            return false;
        }
    }
}