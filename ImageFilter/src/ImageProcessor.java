//package com.bham.pij.exercises.e2a;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;


/*
 * DO NOT IMPORT ANYTHING ELSE.
 */

public class ImageProcessor extends Application {

    // You can change these values if you want, to get a smaller or larger Window.
    private static final int STAGE_WIDTH = 400;
    private static final int STAGE_HEIGHT = 400;

    // These are the filters you must implement.
    private static final String[] filterTypes = {"IDENTITY","BLUR", "SHARPEN", "EMBOSS", "EDGE"};

    private Image image;
    private ImageView imgv;
    private VBox vbox;
    private Scene scene;
    private ArrayList<MenuItem> menuItems;
    private String currentFilename;

    public ImageProcessor() {

    }

    /*
     * You must complete the next four methods. You do not need to change
     * any other methods.
     */

    // You must complete this method.
    public Color[][] applyFilter(Color[][] pixels, float[][] filter) {
        Color[][] outputImage = new Color[ pixels.length-2 ][ pixels[0].length-2 ];
        double Red=0.0;
        double Green=0.0;
        double Blue=0.0;
        //first two loops are used to go through every single pixel of the image row-wise and column-wise of original image without considering the border.
        for (int row=1 ; row <pixels.length-1 ; row++) {
            for (int col=1 ; col <pixels[0].length-1 ; col++) {
                    /*last two for loops apply the filter to the image by changing the red,green and blue value of every pixels.
                    Made row and col equal k and l do to the calculation as stated in exercise 2 (page2).*/
                for (int i=0,k=row-1 ; i <filter.length ; i++, k++) {
                    for (int j=0, l=col-1 ; j <filter[0].length ; j++,l++ ) {
                        Red += (pixels[k][l].getRed() * filter[i][j]) ;
                        Green += ( pixels[k][l].getGreen() * filter[i][j]) ;
                        Blue += ( pixels[k][l].getBlue() * filter[i][j]) ;
                    }
                }

                if (Red>1) Red=1;
                else if ( Red<0) Red=0;
                if (Green>1) Green=1;
                else if (Green<0) Green=0;
                if (Blue>1) Blue=1;
                else if ( Blue<0) Blue=0;

                outputImage[row-1][col-1] = new Color(Red, Green, Blue, 1.0);

                Red=0.0;
                Green=0.0;
                Blue=0.0;
                //Reset variables Red,Green,Blue to avoid keeping adding the values again ad again.
            }
        }
        return outputImage;
    }

    // You must complete this method.
    public float[][] createFilter(String filterType) {
        float[][] Identity = {{0,0,0},{0,1,0},{0,0,0}};
        float[][] Blur = {{0.0625f,0.125f,0.0625f},{0.125f,0.25f,0.125f},{0.0625f,0.125f,0.0625f}};
        float[][] Sharpen = {{0,-1,0},{-1,5,-1},{0,-1,0}};
        float[][] Edge = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
        float[][] Emboss = {{-2,-1,0},{-1,0,1},{0,1,2}};

        switch (filterType) {
            case "IDENTITY":
                return Identity;
            case "BLUR":
                return Blur;
            case "SHARPEN":
                return Sharpen;
            case "EDGE":
                return Edge;
            case "EMBOSS":
                return Emboss;
            default:
                return null;
        }
    }

    // You must complete this method.
    public Color[][] applySepia(Color[][] pixels) {
        for (int i=0; i <pixels.length ; i++) {
            for(int j=0 ; j <pixels[0].length ; j++) {
                double newRed = (pixels[i][j].getRed() * 0.393 + pixels[i][j].getGreen() * 0.769 + pixels[i][j].getBlue() * 0.189);
                double newGreen = (pixels[i][j].getRed() * 0.349 + pixels[i][j].getGreen() * 0.686 + pixels[i][j].getBlue() * 0.168);
                double newBlue = (pixels[i][j].getRed() * 0.272 + pixels[i][j].getGreen() * 0.534 + pixels[i][j].getBlue() * 0.131);
                // Following the sepia equations 2,3 and 4 given in the exercise pdf

                if (newRed>1) newRed=1;
                else if ( newRed<0) newRed=0;
                if (newGreen>1) newGreen=1;
                else if (newGreen<0) newGreen=0;
                if (newBlue>1) newBlue=1;
                else if ( newBlue<0) newBlue=0;

                pixels [i][j] = new Color(newRed, newGreen, newBlue, 1.0);
            }
        }
        return pixels;
    }

    // You must complete this method.
    public Color[][] applyGreyscale(Color[][] pixels) {
        for (int y=0 ; y <pixels.length-1 ; y++ ) {
            for (int x=0; x <pixels[0].length-1 ;x++ ) {
                double red =(pixels[y][x].getRed());
                double green = (pixels[y][x].getGreen());
                double blue =(pixels[y][x].getBlue());
                double sum = ((red+green+blue) / 3);

                // Following equation 5 for grey scale filter in exercise 2 pdf.
                pixels[y][x] = new Color(sum,sum,sum,1.0);
            }
        }
        return pixels;
    }

    /*
     *
     * You can ignore the methods below.
     *
     */

    public void filterImage(String filterType) {

        Color[][] pixels = getPixelDataExtended();

        float[][] filter = createFilter(filterType);

        Color[][] filteredImage = applyFilter(pixels, filter);

        WritableImage wimg = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < wimg.getHeight(); i++) {
            for (int j = 0; j < wimg.getWidth(); j++) {
                pw.setColor(i, j, filteredImage[i][j]);
            }
        }

        File newFile = new File("filtered_" + filterType + "_" + this.currentFilename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wimg, null), "png", newFile);
        } catch (Exception s) {
        }

        initialiseVBox(false);

        image = wimg;
        imgv = new ImageView(wimg);
        vbox.getChildren().add(imgv);
    }

    private void sepia() {

        Color[][] pixels = getPixelData();

        Color[][] newPixels = applySepia(pixels);

        WritableImage wimg = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < wimg.getHeight(); i++) {
            for (int j = 0; j < wimg.getWidth(); j++) {
                pw.setColor(i, j, newPixels[i][j]);
            }
        }

        File newFile = new File("filtered_SEPIA_" + this.currentFilename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wimg, null), "png", newFile);
        } catch (Exception s) {
        }

        initialiseVBox(false);

        image = wimg;
        imgv = new ImageView(wimg);
        vbox.getChildren().add(imgv);
    }

    private void greyscale() {
        Color[][] pixels = getPixelData();

        Color[][] newPixels = applyGreyscale(pixels);

        WritableImage wimg = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < wimg.getHeight(); i++) {
            for (int j = 0; j < wimg.getWidth(); j++) {
                pw.setColor(i, j, newPixels[i][j]);
            }
        }

        File newFile = new File("filtered_GREYSCALE_" + this.currentFilename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wimg, null), "png", newFile);
        } catch (Exception s) {
        }

        initialiseVBox(false);

        image = wimg;
        imgv = new ImageView(wimg);
        vbox.getChildren().add(imgv);

    }

    private Color[][] getPixelData() {
        PixelReader pr = image.getPixelReader();
        Color[][] pixels = new Color[(int) image.getWidth()][(int) image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                pixels[i][j] = pr.getColor(i, j);
            }
        }

        return pixels;
    }

    private Color[][] getPixelDataExtended() {
        PixelReader pr = image.getPixelReader();
        Color[][] pixels = new Color[(int) image.getWidth() + 2][(int) image.getHeight() + 2];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                pixels[i][j] = new Color(1.0, 1.0, 1.0, 1.0);
            }
        }

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                pixels[i + 1][j + 1] = pr.getColor(i, j);
            }
        }

        return pixels;
    }

    private void initialiseStage(Stage stage) {
        stage.setTitle("Image Processor");
        scene = new Scene(new VBox(), STAGE_WIDTH, STAGE_HEIGHT);
        scene.setFill(Color.OLDLACE);
    }

    @Override
    public void start(Stage stage) {

        initialiseStage(stage);

        initialiseVBox(true);

        createMenuItems();

        enableMenuItem("open");

        createStage(stage);
    }

    private void createStage(Stage stage) {

        Menu menuFile = new Menu("File");

        MenuItem open = getMenuItem("open");

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image File");
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    enableAllMenuItems();
                    disableMenuItem("open");
                    openFile(file);
                }
            }
        });

        menuFile.getItems().add(open);

        MenuItem close = getMenuItem("close");

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                disableMenuItem("close");
                closeFile();
            }
        });

        menuFile.getItems().add(close);

        Menu menuTools = new Menu("Tools");

        MenuItem greyscale = getMenuItem("greyscale");

        greyscale.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                greyscale();
            }
        });

        menuTools.getItems().add(greyscale);

        MenuItem blur = getMenuItem("blur");

        blur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("BLUR");
            }
        });

        menuTools.getItems().add(blur);

        MenuItem sharpen = getMenuItem("sharpen");

        sharpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("SHARPEN");
            }
        });

        menuTools.getItems().add(sharpen);

        MenuItem edge = getMenuItem("edge");

        edge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("EDGE");
            }
        });

        menuTools.getItems().add(edge);

        MenuItem sepia = getMenuItem("sepia");

        sepia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                sepia();
            }
        });

        menuTools.getItems().add(sepia);

        MenuItem emboss = getMenuItem("emboss");

        emboss.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("EMBOSS");
            }
        });

        menuTools.getItems().add(emboss);

        MenuItem identity = getMenuItem("identity");

        identity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("IDENTITY");
            }
        });

        menuTools.getItems().add(identity);

        MenuItem reset = getMenuItem("reset");

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                reset();
            }
        });

        menuTools.getItems().add(reset);

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(menuFile, menuTools);

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, vbox);

        stage.setScene(scene);

        stage.show();
    }

    protected void reset() {
        initialiseVBox(false);
        openFile(new File(currentFilename));
    }

    private void initialiseVBox(boolean create) {

        final int LEFT = 10;
        final int RIGHT = 10;
        final int TOP = 10;
        final int BOTTOM = 10;


        if (create) {
            vbox = new VBox();
        }
        vbox.getChildren().clear();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(LEFT,TOP,RIGHT,BOTTOM));
    }

    private void createMenuItems() {
        menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem("Open"));
        menuItems.add(new MenuItem("Close"));
        menuItems.add(new MenuItem("Greyscale"));
        menuItems.add(new MenuItem("Blur"));
        menuItems.add(new MenuItem("Sharpen"));
        menuItems.add(new MenuItem("Sepia"));
        menuItems.add(new MenuItem("Emboss"));
        menuItems.add(new MenuItem("Edge"));
        menuItems.add(new MenuItem("Identity"));
        menuItems.add(new MenuItem("Reset"));
        disableAllMenuItems();
    }

    private void disableAllMenuItems() {
        for (MenuItem m: menuItems) {
            m.setDisable(true);
        }
    }

    private void enableAllMenuItems() {
        for (MenuItem m: menuItems) {
            m.setDisable(false);
        }
    }

    private void disableMenuItem(String item) {
        for (MenuItem m: menuItems) {
            if (m.getText().equalsIgnoreCase(item)) {
                m.setDisable(true);
            }
        }
    }

    private void enableMenuItem(String item) {
        for (MenuItem m: menuItems) {
            if (m.getText().equalsIgnoreCase(item)) {
                m.setDisable(false);
            }
        }
    }

    private MenuItem getMenuItem(String name) {
        for (MenuItem m: menuItems) {
            if (m.getText().equalsIgnoreCase(name)) {
                return m;
            }
        }

        return null;
    }

    private void closeFile() {
        enableMenuItem("open");
        initialiseVBox(false);
    }

    private void openFile(File file) {

        image = new Image("file:" + file.getPath());

        if (image.getWidth() != image.getHeight()) {
            Alert alert = new Alert(AlertType.ERROR, "Image is not square.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        imgv = new ImageView();
        imgv.setImage(image);
        vbox.getChildren().add(imgv);
        currentFilename = file.getName();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
