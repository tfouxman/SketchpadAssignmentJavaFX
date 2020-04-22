package SketchpadAssignment;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;

import javax.imageio.ImageIO;
import java.io.File;

public class CanvasController {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker cpLine;

    @FXML
    private ColorPicker cpFill;

    @FXML
    private TextField brushSizeText;

    @FXML
    private Slider brushSize;

    @FXML
    private ToggleGroup tools;

    @FXML
    private ToggleButton select;

    @FXML
    private ToggleButton brush;

    @FXML
    private ToggleButton line;

    @FXML
    private ToggleButton rectangle;

    @FXML
    private ToggleButton square;

    @FXML
    private ToggleButton ellipse;

    @FXML
    private ToggleButton circle;

    @FXML
    private ToggleButton openPolygon;

    @FXML
    private ToggleButton closedPolygon;

    @FXML
    private ToggleButton eraser;

    private int size;
    private int mode;

    public void initialize() { // Automatic call to FXML Loader when controller class is initiated
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setLineWidth((int) Math.round(brushSize.getValue()));
        gc.setStroke(cpLine.getValue());
        gc.setFill(cpFill.getValue());


        select.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.LOCATION_ARROW));
        select.setTooltip(new Tooltip("Select"));
        select.setUserData(-1);

        brush.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.PENCIL));
        brush.setTooltip(new Tooltip("Scribble"));
        brush.setUserData(0);

        Node lineIcon = GlyphsDude.createIcon(FontAwesomeIcons.SQUARE);
        lineIcon.getTransforms().add(new Scale(0.2, 1, 0.5));
        lineIcon.setRotate(45);
        line.setGraphic(lineIcon);
        line.setTooltip(new Tooltip("Line"));
        line.setUserData(1);

        Node rectIcon = GlyphsDude.createIcon(FontAwesomeIcons.SQUARE_ALT);
        rectIcon.getTransforms().add(new Scale(1.3, 0.8, 0.5));
        rectangle.setGraphic(rectIcon);
        rectangle.setTooltip(new Tooltip("Rectangle"));
        rectangle.setUserData(2);

        square.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.SQUARE_ALT));
        square.setTooltip(new Tooltip("Square"));
        square.setUserData(3);

        Node ellIcon = GlyphsDude.createIcon(FontAwesomeIcons.CIRCLE_THIN);
        ellIcon.getTransforms().add(new Scale(1.3, 0.8, 0.5));
        ellipse.setGraphic(ellIcon);
        ellipse.setTooltip(new Tooltip("Ellipse"));
        ellipse.setUserData(4);

        circle.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.CIRCLE_THIN));
        circle.setTooltip(new Tooltip("Circle"));
        circle.setUserData(5);

        openPolygon.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.CIRCLE_THIN));
        openPolygon.setTooltip(new Tooltip("Circle"));
        openPolygon.setUserData(6);

        closedPolygon.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.CIRCLE_THIN));
        closedPolygon.setTooltip(new Tooltip("Circle"));
        closedPolygon.setUserData(7);

        eraser.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.ERASER));
        eraser.setTooltip(new Tooltip("Eraser"));
        eraser.setUserData(8);

        Scribble scribble = new Scribble(gc);
        Line line = new Line(gc);
        Rectangle rect = new Rectangle(gc);
        Square square = new Square(gc);
        Circle circle = new Circle(gc);
        Ellipse ellipse = new Ellipse(gc);
        OpenPolygon opPoly = new OpenPolygon(gc);
        ClosedPolygon clPoly = new ClosedPolygon(gc);
        Eraser eraser = new Eraser(gc);

        brushSizeText.setAlignment(Pos.CENTER);
        brushSizeText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}")) {
                    brushSizeText.setText(oldValue);
                } else {
                    if (newValue.equals("")) {
                        size = 1;
                        brushSize.setValue(1);
                    } else {
                        size = Integer.parseInt(brushSizeText.getText());
                        brushSize.setValue(size);
                    }
                }
                gc.setLineWidth(size);
            }
        });

        brushSizeText.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue,
                Boolean newValue) -> {
            if (!newValue && brushSizeText.getText().equals("")) brushSizeText.setText("1");
        });

        brushSize.valueProperty().addListener(e -> {
            size = (int) Math.round(brushSize.getValue());
            brushSizeText.setText("" + size);
            gc.setLineWidth(size);
        });

        cpLine.setOnAction(e -> {
            gc.setStroke(cpLine.getValue());
        });

        cpFill.setOnAction(e -> {
            gc.setFill(cpFill.getValue());
        });

        canvas.setOnMousePressed(e->{
            if(mode == 0) { // Scribble
                scribble.startPath();
                scribble.setPoint(e.getX(), e.getY());
            }
            else if(mode == 1) {
                line.setStart(e.getX(), e.getY()); // Line
            }
            else if(mode == 2) { // Rectangle
                rect.setStart(e.getX(), e.getY());
            }
            else if(mode == 3) { // Square

                square.setStart(e.getX(), e.getY());

            } else if(mode == 4) { // Ellipse

                ellipse.setStart(e.getX(), e.getY());

            }
            else if(mode == 5) { // Circle

                circle.setStart(e.getX(), e.getY());

            } else if(mode == 6) { // Open

                if (e.isPrimaryButtonDown()) {
                    opPoly.setPoint(e.getX(), e.getY());
                    opPoly.Draw();
                }
                else if (e.isSecondaryButtonDown()) {
                    opPoly.reset();
                }

            } else if(mode == 7) { // Closed

                if (e.isPrimaryButtonDown()) {
                    clPoly.setPoint(e.getX(), e.getY());
                    clPoly.Draw();
                }
                else if (e.isSecondaryButtonDown()) {
                    clPoly.reset();
                }

            }
            else if(mode == 8) { // Eraser
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        canvas.setOnMouseDragged(e->{
            if(mode == 0) { // Scribble
                scribble.setPoint(e.getX(),e.getY());
                scribble.Draw();
            }
            else if(mode == 8){ // Eraser
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        canvas.setOnMouseReleased(e-> {
                    if (mode == 0) { // Scribble

                        scribble.setPoint(e.getX(), e.getY());
                        scribble.Draw();
                        scribble.endPath();

                    } else if (mode == 1) { // Line

                        line.setEnd(e.getX(), e.getY());
                        line.Draw();

                    } else if (mode == 2) { // Rectangle

                        rect.setEnd(e.getX(), e.getY());
                        rect.Draw();

                    } else if (mode == 3) { // Square

                        square.setEnd(e.getX(), e.getY());
                        square.Draw();

                    } else if (mode == 4) { // Ellipse

                        ellipse.setEnd(e.getX(), e.getY());
                        ellipse.Draw();

                    } else if (mode == 5) { // Circle
//
                        circle.setEnd(e.getX(), e.getY());
                        circle.Draw();

                    } else if(mode == 8) {
                        double lineWidth = gc.getLineWidth();
                        gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
                    }
                });

        tools.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) {
                    select.setSelected(true);
                }
                else if (oldValue != newValue) {
                    mode = (int) tools.getSelectedToggle().getUserData();
                }
            }
        });

    }

    public void onSave() {
        try {
            Image snapshot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("sketch.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    public void onExit() {
        Platform.exit();
    }
}
