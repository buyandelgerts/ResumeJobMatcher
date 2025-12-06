package com.example.demo.Controller;

import com.example.demo.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class ResumeController {

    private final OllamaService ollamaService;
    private final PdfService pdfService;

    public ResumeController(OllamaService ollamaService, PdfService pdfService) {
        this.ollamaService = ollamaService;
        this.pdfService = pdfService;
    }

    @GetMapping("/")
    public String home() {
        return "index";   // return template name
    }

    @PostMapping(value = "/analyze")
    public ResponseEntity<Map<String, Object>> analyze(
            @RequestParam(required = false) MultipartFile resumeFile,
            @RequestParam(required = false, defaultValue = "") String resumeText,
            @RequestParam String jobDescription) throws IOException {

        String finalResumeText = resumeText;

        if (resumeFile != null && !resumeFile.isEmpty()) {
            finalResumeText = pdfService.extractText(resumeFile);
        }

        Map<String, Object> result = ollamaService.analyzeMatch(finalResumeText, jobDescription);
        return ResponseEntity.ok(result);
    }
}

