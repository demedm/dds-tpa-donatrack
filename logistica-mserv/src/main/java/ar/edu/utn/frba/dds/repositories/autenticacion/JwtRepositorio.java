package ar.edu.utn.frba.dds.repositories.autenticacion;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.autenticacion.UsuarioLogueado;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtRepositorio {
  public static final JwtRepositorio Instance = new JwtRepositorio();
  private static final Duration expiracion = Duration.ofMinutes(30);
  private static SecretKey secretKey;

  public JwtRepositorio() {
    String key = System.getenv("JWT_SECRET");

    if (key == null || key.isBlank()) {
      throw new IllegalStateException("No se configuró JWT_SECRET");
    }
    byte[] keyBytes = Decoders.BASE64.decode(key);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public UsuarioLogueado obtenerUsuario(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Long id = Long.valueOf(claims.getSubject());
    String rol = claims.get("rol", String.class);

    return new UsuarioLogueado(id, rol);
  }

  public boolean esAdmin(String token) {
    try {
      UsuarioLogueado usuario = obtenerUsuario(token);
      return "ADMIN".equals(usuario.getRol());
    } catch (Exception e) {
      return false;
    }
  }
}
