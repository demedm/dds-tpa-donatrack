package ar.edu.utn.frba.dds.main;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class ConfiguracionBaseDeDatos {

  private ConfiguracionBaseDeDatos(){
  }

  public static void aplicar(){
    String usuario = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");

    if(password == null){
      throw new IllegalStateException("No se configuró DB_PASSWORD (ver .env.example en la raiz del repo)");
    }

    WithSimplePersistenceUnit.configure(propiedades ->{
     if(usuario != null && !usuario.isBlank()){
       propiedades.set("hibernate.connection.username", usuario);

     }
      propiedades.set("hibernate.connection.password", password);
    });

  }
}
