package de.hsos.swa.verwaltung.entity.security;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

/* 
 * Implementierung
 *
 * @author Julian Schramm
 * 
 * Kontrolle/Korrektur
 * 
 * @author Julian Schramm, Mick Hurrelbrink
 */
@Entity
@Table(name = "user_login")
@UserDefinition
public class UserLogin extends PanacheEntity {

    @Username
    public String email;

    @Password
    public String password;

    @Roles
    public String role;

    public UserLogin() {
    }

    public static void add(String email, String password, String role) {
        UserLogin user = new UserLogin();
        user.email = email;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();

    }

    public static boolean deleteUser(String email) {
        if (UserLogin.delete("email", email) == 1) {
            return true;
        }
        return false;
    }

    public static boolean userNotPresent(String email) {
        if (UserLogin.find("email", email).count() == 0) {
            return true;
        }
        return false;
    }
}
