package SketchpadAssignment;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

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
    private Button eraser;

    private int size;
    private int mode;

    private ArrayList<Graphic> graphics = new ArrayList<>();

    private Graphic selected;
    private Graphic clipBoard;
    private Stack<ArrayList<Graphic>> undoHistory = new Stack<>();
    private Stack<ArrayList<Graphic>> redoHistory = new Stack<>();

    private Point ctxMenu = new Point(0, 0);

    private GraphicsContext gracon;

    public void initialize() { // Automatic call to FXML Loader when controller class is initiated
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gracon = gc;
        gc.setLineWidth((int) Math.round(brushSize.getValue()));
        gc.setStroke(cpLine.getValue());
        gc.setFill(cpFill.getValue());

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        contextMenu.getItems().addAll(cut, copy, paste, redo, undo);
        cut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Cut...");
                try {
                    clipBoard = (Graphic)selected.clone();
                    undoHistory.push(cloneGraphics());
                    graphics.remove(selected);
                    clear();
                    redraw(gc);
                } catch (CloneNotSupportedException e) {
                    System.out.println("Clone failed!");
                }

            }
        });

        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Copy...");
                try {
                    clipBoard = (Graphic)selected.clone();
                } catch (CloneNotSupportedException e) {
                    System.out.println("Clone failed!");
                }

            }
        });

        paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Paste...");
                try {
                    Graphic newG = (Graphic)clipBoard.clone();
                    if (newG.getClass() == Line.class) {

                        Line temp = (Line) newG;
                        temp.setEnd(temp.getEnd().x + (ctxMenu.x - temp.getStart().x),
                                temp.getEnd().y + (ctxMenu.y - temp.getStart().y));
                        newG = temp;
                        newG.setStart(ctxMenu.x, ctxMenu.y);

                    } else if (newG.getClass() == OpenPolygon.class) {

                        OpenPolygon temp = (OpenPolygon) newG;
                        double xDiff, yDiff;
                        xDiff = ctxMenu.x - temp.lines.get(0).getStart().x;
                        yDiff = ctxMenu.y - temp.lines.get(0).getStart().y;
                        for ( Line l : temp.lines
                        ) {
                            l.setStart(l.getStart().x + xDiff, l.getStart().y + yDiff);
                            l.setEnd(l.getEnd().x + xDiff,
                                    l.getEnd().y + yDiff);
                        }
                        newG = temp;

                    } else if (newG.getClass() == ClosedPolygon.class) {

                        ClosedPolygon temp = (ClosedPolygon) newG;
                        double xDiff, yDiff;
                        xDiff = ctxMenu.x - temp.lines.get(0).getStart().x;
                        yDiff = ctxMenu.y - temp.lines.get(0).getStart().y;
                        for ( Line l : temp.lines
                        ) {
                            l.setStart(l.getStart().x + xDiff, l.getStart().y + yDiff);
                            l.setEnd(l.getEnd().x + xDiff,
                                    l.getEnd().y + yDiff);
                        }
                        newG = temp;

                    } else newG.setStart(ctxMenu.x, ctxMenu.y);

                    undoHistory.push(cloneGraphics());
                    graphics.add(newG);

                    clear();
                    redraw(gc);
                } catch (CloneNotSupportedException e) {
                    System.out.println("Clone failed!");
                }
            }
        });

        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphics.clear();
                redoHistory.push(cloneGraphics());
                graphics = undoHistory.pop();
                clear();
                redraw(gc);
            }
        });

        redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Redo...");
                graphics = redoHistory.pop();
            }
        });

        canvas.setOnContextMenuRequested( e-> {
            if (mode == -1) {
                if (clipBoard == null) {
                    paste.setDisable(true);
                } else paste.setDisable(false);
                if (selected == null) {
                    cut.setDisable(true);
                    copy.setDisable(true);
                } else {
                    cut.setDisable(false);
                    copy.setDisable(false);
                }
                if (undoHistory == null || undoHistory.isEmpty()) {
                    undo.setDisable(true);
                } else undo.setDisable(false);
                if (redoHistory == null || redoHistory.isEmpty()) {
                    redo.setDisable(true);
                } else redo.setDisable(false);
                ctxMenu.x = e.getX();
                ctxMenu.y = e.getY();
                System.out.println("x: " + ctxMenu.x + " y: " + ctxMenu.y);
                contextMenu.show(canvas.getScene().getWindow(), e.getScreenX(), e.getScreenY());
            }
        });

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

        openPolygon.setText("OP");
        openPolygon.setTooltip(new Tooltip("Open Polygon"));
        openPolygon.setUserData(6);

        closedPolygon.setText("CP");
        closedPolygon.setTooltip(new Tooltip("Closed Polygon"));
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
            gc.setStroke(cpLine.getValue());
            gc.setFill(cpFill.getValue());
            scribble.setColour();
            line.setColour();
            rect.setColour();
            square.setColour();
            ellipse.setColour();
            circle.setColour();
            opPoly.setColour();
            clPoly.setColour();
            if (mode == -1) { // Select
                ListIterator<Graphic> li = graphics.listIterator(graphics.size());
                while(li.hasPrevious()) {
                    Graphic temp = li.previous();
                    if (temp.isWithinPoint(new Point(e.getX(), e.getY()))) {
                        System.out.println("Within!");
                        selected = temp;
                        break;
                    }
                    else {
                        System.out.println("Not in!");
                        selected = null;
                    }
                }
            }
            else if(mode == 0) { // Scribble
                if (e.isPrimaryButtonDown()) {
                    scribble.setStart(e.getX(), e.getY());
                    scribble.startPath();
                    scribble.setPoint(e.getX(), e.getY());
                }
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

                    try {
                        opPoly.setClicks(-1);
                        Graphic opPolyclone = (Graphic)opPoly.clone();
                        undoHistory.push(cloneGraphics());
                        graphics.add(opPolyclone);
                    } catch (CloneNotSupportedException cnse) {
                        System.out.println("Cannot clone");
                    }

                    opPoly.reset();
                }

            } else if(mode == 7) { // Closed

                if (e.isPrimaryButtonDown()) {
                    clPoly.setPoint(e.getX(), e.getY());
                    clPoly.Draw();
                }
                else if (e.isSecondaryButtonDown()) {

                    try {
                        clPoly.setClicks(-2);
                        clPoly.Draw();
                        clPoly.setClicks(-1);
                        Graphic clPolyClone = (Graphic) clPoly.clone();
                        undoHistory.push(cloneGraphics());
                        graphics.add(clPolyClone);
                    } catch (CloneNotSupportedException cnse) {
                        System.out.println("Cannot clone");
                    }

                    clPoly.reset();

                }

            }
            else if(mode == 8) { // Eraser
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
            System.out.println(graphics.size());
        });

        canvas.setOnMouseDragged(e->{
            if(mode == 0) { // Scribble
                if (e.isPrimaryButtonDown()) {
                    scribble.setPoint(e.getX(),e.getY());
                }
            }
            else if(mode == 8){ // Eraser
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        canvas.setOnMouseReleased(e -> {
                    if (mode == -1) { // Select
                        if (e.getButton() == MouseButton.PRIMARY) {
                            if (selected != null) {

                                undoHistory.push(cloneGraphics());

                                graphics.remove(selected);
                                if (selected.getClass() == Scribble.class) {
                                    Scribble temp = (Scribble) selected;
                                    double xDiff, yDiff;
                                    xDiff = e.getX() - temp.getPoints().get(0).x;
                                    yDiff = e.getY() - temp.getPoints().get(0).y;
                                    for ( Point p : temp.getPoints()
                                         ) {
                                        p.x = p.x + xDiff;
                                        p.y = p.y + yDiff;
                                    }

                                    selected = temp;
                                    selected.setStart(e.getX(), e.getY());
                                }
                                else if (selected.getClass() == Line.class) {
                                    Line temp = (Line) selected;
                                    temp.setEnd(temp.getEnd().x + (e.getX() - temp.getStart().x),
                                            temp.getEnd().y + (e.getY() - temp.getStart().y));
                                    selected = temp;
                                    selected.setStart(e.getX(), e.getY());

                                } else if (selected.getClass() == OpenPolygon.class) {

                                    OpenPolygon temp = (OpenPolygon) selected;
                                    double xDiff, yDiff;
                                    xDiff = e.getX() - temp.lines.get(0).getStart().x;
                                    yDiff = e.getY() - temp.lines.get(0).getStart().y;
                                    for ( Line l : temp.lines
                                    ) {
                                        l.setStart(l.getStart().x + xDiff, l.getStart().y + yDiff);
                                        l.setEnd(l.getEnd().x + xDiff,
                                                l.getEnd().y + yDiff);
                                    }
                                    selected = temp;

                                } else if (selected.getClass() == ClosedPolygon.class) {

                                    ClosedPolygon temp = (ClosedPolygon) selected;
                                    double xDiff, yDiff;
                                    xDiff = e.getX() - temp.lines.get(0).getStart().x;
                                    yDiff = e.getY() - temp.lines.get(0).getStart().y;
                                    for ( Line l : temp.lines
                                    ) {
                                        l.setStart(l.getStart().x + xDiff, l.getStart().y + yDiff);
                                        l.setEnd(l.getEnd().x + xDiff,
                                                l.getEnd().y + yDiff);
                                    }
                                    selected = temp;

                                } else selected.setStart(e.getX(), e.getY());

                                graphics.add(selected);

                                clear();
                                redraw(gc);

                                selected = null;

                            }
                        }

                    }
                    else if (mode == 0) { // Scribble

                        scribble.setPoint(e.getX(), e.getY());
                        scribble.endPath();

                        try {
                            Graphic scribbleClone = (Graphic) scribble.clone();
                            undoHistory.push(cloneGraphics());
                            graphics.add(scribbleClone);

                        } catch (CloneNotSupportedException cnse) {
                            System.out.println("Cannot clone");
                        }

                    } else if (mode == 1) { // Line

                        line.setEnd(e.getX(), e.getY());
                        line.Draw();

                        try {
                            Graphic lineClone = (Graphic) line.clone();
                            undoHistory.push(cloneGraphics());
                            graphics.add(lineClone);
                        } catch (CloneNotSupportedException cnse) {
                            System.out.println("Cannot clone");
                        }

                    } else if (mode == 2) { // Rectangle

                        rect.setEnd(e.getX(), e.getY());
                        rect.Draw();

                        try {
                            Graphic rectClone = (Graphic) rect.clone();
                            undoHistory.push(cloneGraphics());
                            graphics.add(rectClone);
                        } catch (CloneNotSupportedException cnse) {
                            System.out.println("Cannot clone");
                        }

                    } else if (mode == 3) { // Square

                        square.setEnd(e.getX(), e.getY());
                        square.Draw();

                        try {
                            Graphic sqClone = (Graphic) square.clone();
                            undoHistory.push(cloneGraphics());
                            graphics.add(sqClone);
                        } catch (CloneNotSupportedException cnse) {
                            System.out.println("Cannot clone");
                        }

                    } else if (mode == 4) { // Ellipse

                        ellipse.setEnd(e.getX(), e.getY());
                        ellipse.Draw();

                        try {
                            Graphic ellClone = (Graphic) ellipse.clone();
                            undoHistory.push(cloneGraphics());
                            graphics.add(ellClone);
                        } catch (CloneNotSupportedException cnse) {
                            System.out.println("Cannot clone");
                        }

                    } else if (mode == 5) { // Circle
//
                        circle.setEnd(e.getX(), e.getY());
                        circle.Draw();

                        try {
                            Graphic circClone = (Graphic) circle.clone();
                            undoHistory.push(cloneGraphics());
                            graphics.add(circClone);
                        } catch (CloneNotSupportedException cnse) {
                            System.out.println("Cannot clone");
                        }

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

    public ArrayList<Graphic> cloneGraphics() {
        ArrayList<Graphic> newGraphics = new ArrayList<>();
        for ( Graphic g : graphics
             ) {
            try {
                newGraphics.add((Graphic)g.clone());
            } catch (CloneNotSupportedException e) {

            }

        }
        return newGraphics;
    }

    public void clear() {
        gracon.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void erase() {
        graphics.clear();
        undoHistory.clear();
        redoHistory.clear();
        clear();
    }

    public void redraw(GraphicsContext gc) {
        for ( Graphic g : graphics
             ) {
            g.Draw();
        }
    }

    public void onOpen() {
        FileChooser openFile = new FileChooser();
        openFile.setTitle("Open File");
        File file = openFile.showOpenDialog(Sketchpad.stage);

        if (file != null) {
            try {
                InputStream io = new FileInputStream(file);
                Image img = new Image(io);
                gracon.drawImage(img, 0, 0);
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }

    public void onSave() {

        FileChooser savefile = new FileChooser();
        savefile.setTitle("Save File");
        File file = savefile.showSaveDialog(Sketchpad.stage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(1500, 1000);
                canvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                System.out.println("Error!");
            }
        }
    }

    public void onExit() {
        Platform.exit();
    }
}
