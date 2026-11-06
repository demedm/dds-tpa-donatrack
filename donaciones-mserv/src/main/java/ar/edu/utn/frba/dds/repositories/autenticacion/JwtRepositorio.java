package ar.edu.utn.frba.dds.repositories.autenticacion;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
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

  public String generarToken(Usuario usuario) {
    Instant expiracionToken = Instant.now().plus(expiracion);

    return Jwts.builder()
        .subject(usuario.getId().toString())
        .claim("rol", usuario.getRol().toString())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(expiracionToken))
        .signWith(secretKey)
        .compact();
  }
}
