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

    // Prefissi telefonici italiani (parziale)
    private final String[] prefissiFissi = {"010", "011", "012", "013", "014", "015", "016", "017", "018", "019", "02",
            "030", "031", "032", "033", "034", "035", "036", "037", "038", "039", "040", "041", "042", "043", "044",
            "045", "046", "047", "048", "049", "050", "051", "052", "053", "054", "055", "056", "057", "058", "059",
            "06", "070", "071", "072", "073", "074", "075", "076", "077", "078", "079", "080", "081", "082", "083",
            "084", "085", "086", "087", "088", "089", "090", "091", "092", "093", "094", "095", "096", "097", "098", "099"};
    private final String[] prefissiCellulari = {"310", "311", "313", "320", "322", "323", "324", "327", "328",
            "329", "330", "331", "333", "334", "335", "336", "337", "338", "339", "340", "341", "342", "343",
            "345", "346", "347", "348", "349", "350", "351", "360", "361", "362", "363", "366", "368", "370",
            "371", "373", "377", "378", "379", "380", "388", "389", "391"};

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

        String telefono = utente.getTelefono();
        String numeroPulito = telefono.replaceAll("\\s+", ""); // rimuove spazi se vengono inseriti

        if (!numeroPulito.matches("\\d+")) {
            errori.add("Errore. Il numero di telefono deve contenere solo cifre.");
        } else {
            boolean numeroValido = false;

            if (numeroPulito.startsWith("0")) {
                // Numero fisso: deve iniziare con un prefisso fisso valido ed essere lungo 9 cifre
                for (String prefisso : prefissiFissi) {
                    if (numeroPulito.startsWith(prefisso) && numeroPulito.length() == 9) {
                        numeroValido = true;
                        break;
                    }
                }

                if (!numeroValido) {
                    errori.add("Errore. Numero fisso non valido. Deve avere un prefisso valido e/o contenere 9 cifre.");
                }
            } else {
                // Numero cellulare: deve iniziare con prefisso mobile valido ed essere lungo 10 cifre
                for (String prefisso : prefissiCellulari) {
                    if (numeroPulito.startsWith(prefisso) && numeroPulito.length() == 10) {
                        numeroValido = true;
                        break;
                    }
                }
                if (!numeroValido) {
                    errori.add("Errore. Numero cellulare non valido. Deve avere un prefisso valido e/o contenere 10 cifre.");
                }
            }
        }

        // Controllo delle password
        if (!utente.getPassword().equals(utente.getConfermaPassword())) {
            errori.add("Errore. Le due password non coincidono.");
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