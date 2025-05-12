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

        // Lunghezza consentita per i nomi
        final int MIN_NAME_LENGTH = 3;
        final int MAX_NAME_LENGTH = 10;


        // Nomi bannati
        String[] nomiBannati = {"admin", "root", "superuser", "administrator", "webmaster", "moderator", "mod",
                "support", "helpdesk", "system", "sysadmin", "security", "staff", "team", "mail", "noreply",
                "postmaster", "owner", "manager", "api", "service", "developer", "dev", "console", "backend", "server"};

        // Check nomi bannati
        for (String nomeBannato : nomiBannati) {
            if (utente.getNome().equalsIgnoreCase(nomeBannato)) {
                errori.add("Il nome " + utente.getNome() + " non è permesso.");
            }
        }

        // Controllo lunghezza nome
        int nomeLength = utente.getNome().length();
        if (nomeLength < MIN_NAME_LENGTH || nomeLength > MAX_NAME_LENGTH) {
            errori.add("Il nome deve contenere tra " + MIN_NAME_LENGTH + " e " + MAX_NAME_LENGTH + " caratteri.");
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