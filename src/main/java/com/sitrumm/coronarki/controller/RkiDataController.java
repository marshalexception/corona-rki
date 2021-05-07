package com.sitrumm.coronarki.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Controller
public class RkiDataController {

    @Value("${covid.basepath}")
    String basePath;

    @GetMapping("/rki")
    public String fetchData(Model model) throws IOException {
        URL url = new URL(basePath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        String rkiData = parseJson(con);

        model.addAttribute("rkiData", rkiData);
        return "data";
    }

    private String parseJson(HttpURLConnection con) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

        StringBuilder result = new StringBuilder();

        String output;
        log.info("Reading data from RKI");
        while ((output = br.readLine()) != null) {
            result.append(output);
        }

        return result.toString();
    }
}
