package de.hsos.swa.verwaltung.boundary.rs.DTOs;

public class AdministratorDTO {
    public long adminId;
    public String email;
    public String vorname;
    public String nachname;

    public AdministratorDTO() {}

    public AdministratorDTO(long id, String email, String vorname, String nachname) {
        this.adminId = id;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }
}
