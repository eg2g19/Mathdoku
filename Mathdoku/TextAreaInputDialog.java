import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

@SuppressWarnings("restriction")
public class TextAreaInputDialog extends Dialog<String> {

    private final GridPane grid;
    private final TextArea textArea;
    private final String defaultValue;

    public TextAreaInputDialog() {
        this("");
    }


    public TextAreaInputDialog(@NamedArg("defaultValue") String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        this.textArea = new TextArea(defaultValue);
        this.textArea.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setFillWidth(textArea, true);

        this.defaultValue = defaultValue;

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? textArea.getText() : null;
        });
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(textArea, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(() -> textArea.requestFocus());
    }
}