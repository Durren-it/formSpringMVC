package it.giuseppe.form.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Utente {
    private String nome;
    private String email;
    private String password;
    private String confermaPassword;
    private LocalDate dataDiNascita;
    private String telefono;
}
