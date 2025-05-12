package it.giuseppe.form.controller;

import it.giuseppe.form.model.Utente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        // Nomi bannati
        String[] nomiBannati = {"admin", "root", "superuser", "administrator", "webmaster", "moderator", "mod",
                "support", "helpdesk", "system", "sysadmin", "security", "staff", "team", "mail", "noreply",
                "postmaster", "owner", "manager", "api", "service", "developer", "dev", "console", "backend", "server"};

        // Check nomi bannati
        for (String nomeBannato : nomiBannati) {
            if (utente.getNome().equalsIgnoreCase(nomeBannato)) {
                model.addAttribute("errore", "Il nome \"" + utente.getNome() + "\" non è permesso.");
                return "error";
            }
        }

        model.addAttribute("utente", utente);
        return "result";
    }
}