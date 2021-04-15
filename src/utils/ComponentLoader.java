package utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.util.Pair;

public class ComponentLoader<T> {
  public ComponentLoader() {
  }

  public <U, S> Pair<U, S> loadComponent(T target, String path) throws IOException {
    FXMLLoader loader = new FXMLLoader(target.getClass().getResource(path));
    U componentView = loader.load();

    @SuppressWarnings("unchecked")
    S componentController = (S) loader.getController();
    return new Pair<U, S>(componentView, componentController);
  }
}
