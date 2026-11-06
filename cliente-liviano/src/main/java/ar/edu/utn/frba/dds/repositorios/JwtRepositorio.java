package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtRepositorio {
  public static final JwtRepositorio Instance = new JwtRepositorio();
  private final SecretKey secretKey;

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

}
