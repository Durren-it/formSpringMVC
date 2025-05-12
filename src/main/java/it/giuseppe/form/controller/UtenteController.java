package it.giuseppe.form.controller;

import it.giuseppe.form.model.Utente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UtenteController {

    // Nomi bannati
    private final String[] nomiBannati = {"admin", "root", "superuser", "administrator", "webmaster", "moderator", "mod",
            "support", "helpdesk", "system", "sysadmin", "security", "staff", "team", "mail", "noreply",
            "postmaster", "owner", "manager", "api", "service", "developer", "dev", "console", "backend", "server"};

    // Lunghezza consentita per i nomi
    private final int MIN_NAME_LENGTH = 3;
    private final int MAX_NAME_LENGTH = 10;

    @GetMapping("/form")
    public String getForm() {
        return "form";
    }

    @GetMapping("/form2")
    public String getForm2(@ModelAttribute Utente utente) {
        return "form2";
    }

    @PostMapping("/submitForm")
    public String postForm(@ModelAttribute Utente utente, Model model) {
        List<String> errori = new ArrayList<>();

        // Check nomi bannati
        for (String nomeBannato : nomiBannati) {
            if (utente.getNome().equalsIgnoreCase(nomeBannato)) {
                errori.add("Errore. Il nome " + utente.getNome() + " non è permesso.");
            }
        }

        // Controllo lunghezza nome
        int nomeLength = utente.getNome().length();
        if (nomeLength < MIN_NAME_LENGTH || nomeLength > MAX_NAME_LENGTH) {
            errori.add("Errore. Il nome deve contenere tra " + MIN_NAME_LENGTH + " e " + MAX_NAME_LENGTH + " caratteri.");
        }

        String email = utente.getEmail();
        boolean emailNonValida = false;

        // Controllo validità email
        if (email.startsWith("@") || email.endsWith("@") ||
                email.indexOf("@") != email.lastIndexOf("@") ||
                email.startsWith(".") || email.endsWith(".") ||
                !email.contains(".")) {
            emailNonValida = true;
        }

        if (!emailNonValida) {
            String[] parts = email.split("@");
            if (parts.length < 2 || parts[1].startsWith(".") || parts[1].lastIndexOf(".") <= 0) {
                emailNonValida = true;
            }
        }

        if (emailNonValida) {
            errori.add("Errore. Formato email non valido.");
        }

        // Se ci sono errori, mostra la pagina error
        if (!errori.isEmpty()) {
            model.addAttribute("errori", errori);
            return "error";
        }

        model.addAttribute("utente", utente);
        return "result";
    }
}