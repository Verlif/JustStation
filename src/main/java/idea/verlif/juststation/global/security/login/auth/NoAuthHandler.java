package idea.verlif.juststation.global.security.login.auth;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/7 11:27
 */
public class NoAuthHandler implements AuthHandler {

    @Override
    public UserDetails auth(String id, String token) throws AuthenticationException {
        PrintUtils.print(Level.SEVERE, "no such AuthHandler can auth!");
        return null;
    }
}
