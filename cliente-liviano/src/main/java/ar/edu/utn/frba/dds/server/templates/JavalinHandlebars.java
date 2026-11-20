package ar.edu.utn.frba.dds.server.templates;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.session.Rol;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.FileRenderer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JavalinHandlebars implements FileRenderer {

    Handlebars handlebars = new Handlebars();
    @NotNull
    @Override
    public String render(@NotNull String path, @NotNull Map<String, ?> model, @NotNull Context context) {

        Map<String, Object> modelo = new HashMap<>(model);
        UsuarioLogueado usuario = context.attribute("usuarioLogueado");

        Rol rol = usuario == null ? null : usuario.getRol();

        modelo.put("logueado", usuario != null);
        modelo.put("esDonante", rol == Rol.DONANTE);
        modelo.put("esBeneficiario", rol == Rol.BENEFICIARIO);
        modelo.put("esAdmin", rol == Rol.ADMIN);
        modelo.put("puedeDonar", usuario == null || rol == Rol.DONANTE);


        Template template = null;
        try {
            template = handlebars.compile("templates/" + path.replace(".hbs",""));
            return template.apply(modelo);
        } catch (IOException e) {
            e.printStackTrace();
            context.status(HttpStatus.NOT_FOUND);
            return "No se encuentra la página indicada...";
        }
    }
}
