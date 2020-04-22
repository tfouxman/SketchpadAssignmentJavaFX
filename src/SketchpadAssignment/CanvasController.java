package SketchpadAssignment;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;

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
    private ToggleButton eraser;

    private int size;
    private int mode;

    public void initialize() { // Automatic call to FXML Loader when controller class is initiated
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth((int) Math.round(brushSize.getValue()));
        select.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.LOCATION_ARROW));
        select.setTooltip(new Tooltip("Select"));
        select.setUserData(-1);

        brush.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.PENCIL));
        brush.setTooltip(new Tooltip("Scribble"));
        brush.setUserData(0);

        line.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.ILS));
        line.setTooltip(new Tooltip("Line"));
        line.setUserData(1);

        rectangle.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.PENCIL_SQUARE_ALT));
        rectangle.setTooltip(new Tooltip("Rectangle"));
        rectangle.setUserData(2);

        square.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.SQUARE_ALT));
        square.setTooltip(new Tooltip("Square"));
        square.setUserData(3);

        ellipse.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.ERASER));
        ellipse.setTooltip(new Tooltip("Ellipse"));
        ellipse.setUserData(4);

        circle.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.CIRCLE));
        circle.setTooltip(new Tooltip("Circle"));
        circle.setUserData(5);

        eraser.setGraphic(GlyphsDude.createIcon(FontAwesomeIcons.ERASER));
        eraser.setTooltip(new Tooltip("Eraser"));
        eraser.setUserData(6);

        Scribble scribble = new Scribble(gc);
        Line line = new Line(gc);
        Rectangle rect = new Rectangle(gc);
        Square square = new Square(gc);
        Circle circle = new Circle(gc);
        Ellipse ellipse = new Ellipse(gc);
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
            if(mode == 0) {
                scribble.startPath();
                scribble.setPoint(e.getX(), e.getY());
            }
            else if(mode == 1) {
                line.setStart(e.getX(), e.getY());
            }
            else if(mode == 2) {
//                gc.setStroke(cpLine.getValue());
//                gc.setFill(cpFill.getValue());
//                rect.setX(e.getX());
//                rect.setY(e.getY());
            }
            else if(mode == 4) {
//                gc.setStroke(cpLine.getValue());
//                gc.setFill(cpFill.getValue());
//                elps.setCenterX(e.getX());
//                elps.setCenterY(e.getY());
            }
            else if(mode == 5) {
//                gc.setStroke(cpLine.getValue());
//                gc.setFill(cpFill.getValue());
//                circ.setCenterX(e.getX());
//                circ.setCenterY(e.getY());
            }
            else if(mode == 6) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        canvas.setOnMouseDragged(e->{
            if(mode == 0) {
                scribble.setPoint(e.getX(),e.getY());
                scribble.Draw();
            }
            else if(mode == 6){
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        canvas.setOnMouseReleased(e-> {
                    if (mode == 0) {
                        scribble.setPoint(e.getX(), e.getY());
                        scribble.Draw();
                        scribble.endPath();
                    } else if (mode == 1) {
                        line.setEnd(e.getX(), e.getY());
                        line.Draw();
                    } else if (mode == 2) {
//                        rect.setWidth(Math.abs((e.getX() - rect.getX())));
//                        rect.setHeight(Math.abs((e.getY() - rect.getY())));
//                        //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
//                        if (rect.getX() > e.getX()) {
//                            rect.setX(e.getX());
//                        }
//                        //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
//                        if (rect.getY() > e.getY()) {
//                            rect.setY(e.getY());
//                        }
//
//                        gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
//                        gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                    } else if (mode == 4) {
//                        elps.setRadiusX(Math.abs(e.getX() - elps.getCenterX()));
//                        elps.setRadiusY(Math.abs(e.getY() - elps.getCenterY()));
//
//                        if (elps.getCenterX() > e.getX()) {
//                            elps.setCenterX(e.getX());
//                        }
//                        if (elps.getCenterY() > e.getY()) {
//                            elps.setCenterY(e.getY());
//                        }
//
//                        gc.strokeOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());
//                        gc.fillOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());

                    } else if (mode == 5) {
//                        circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);
//
//                        if (circ.getCenterX() > e.getX()) {
//                            circ.setCenterX(e.getX());
//                        }
//                        if (circ.getCenterY() > e.getY()) {
//                            circ.setCenterY(e.getY());
//                        }
//
//                        gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
//                        gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

                    } else if(mode == 6) {
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
