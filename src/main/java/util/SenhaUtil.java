package util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    private SenhaUtil() {
    }

    public static String gerarHash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    public static boolean conferir(String senha, String hash) {
        if (senha == null || hash == null || hash.isBlank()) {
            return false;
        }
        try {
            return BCrypt.checkpw(senha, hash);
        } catch (IllegalArgumentException exception) {
            return senha.equals(hash);
        }
    }

    public static boolean pareceBCrypt(String senha) {
        return senha != null && (senha.startsWith("$2a$") || senha.startsWith("$2b$") || senha.startsWith("$2y$"));
    }
}
