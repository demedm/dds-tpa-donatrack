package ar.edu.utn.frba.dds.main.templates;

import io.javalin.http.Context;
import io.javalin.rendering.FileRenderer;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class JavalinRenderer implements FileRenderer {
  private Map<String, FileRenderer> renderers = new HashMap<>();

  public JavalinRenderer register(String extension, FileRenderer renderer) {
    renderers.put(extension, renderer);
    return this;
  }

  @Override
  public String render(@NotNull String filePath, @NotNull Map<String, ? extends Object> model,
                       @NotNull Context context) {
    String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
    return renderers.get(extension).render(filePath, model, context);
  }

}
