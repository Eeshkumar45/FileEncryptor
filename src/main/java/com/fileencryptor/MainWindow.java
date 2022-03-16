package com.fileencryptor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.*;
import javafx.stage.FileChooser;

public class MainWindow extends Application {


    public void start(Stage stage)
    {

        try {

            stage.setTitle("FILE ENCYPTOR/DECRYPTOR");
            FileChooser fil_chooser = new FileChooser();

            //title
            Label heading = new Label("FILE ENCYPTOR");
            heading.setTextFill(Color.color(0,0,0));
            heading.setFont(new Font("times new roman",40));
            heading.setAlignment(Pos.CENTER);
            //title

            //file
            Label selectFile = new Label("Select File : ");
            selectFile.setTextFill(Color.color(0,0,0));
            selectFile.setFont(new Font("times new roman",20));

            Button button = new Button("select file");
            button.setBackground(new Background(new BackgroundFill(Color.color(0.54,0.83,0.5),CornerRadii.EMPTY,Insets.EMPTY)));
            button.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

            HBox hBoxForFile = new HBox(20,selectFile,button);
            hBoxForFile.setAlignment(Pos.TOP_CENTER);
            //file
            Label fileSelected = new Label("No file selected");
            fileSelected.setTextFill(Color.color(0,0,0));


            //key
            Label enterKey = new Label("Enter Key : ");
            enterKey.setTextFill(Color.color(0,0,0));
            enterKey.setFont(new Font("times new roman",20));

            TextField keyArea = new TextField();
            keyArea.setDisable(true);

            Button encrypt = new Button();
            encrypt.setDisable(true);
            encrypt.setText("GO");
            encrypt.setFont(new Font("times new roman",15));
            encrypt.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

            HBox hboxForKey = new HBox(20,enterKey,keyArea,encrypt);
            hboxForKey.setAlignment(Pos.TOP_CENTER);
            //key

            //invalid key
            Label invalidKey = new Label("Use Only Digits");
            invalidKey.setFont(new Font("times new roman",20));
            invalidKey.setTextFill(Color.RED);
            invalidKey.setVisible(false);
            invalidKey.setAlignment(Pos.TOP_CENTER);
            //invalid key


            //Listener to validate key
            EventHandler<KeyEvent> onKeyEnter =
                    new EventHandler<KeyEvent>() {


                        @Override
                        public void handle(KeyEvent keyEvent) {


                            // get the file selected
                            String s = keyArea.getText();
                            boolean isNumeric = true;
                            for(char c : s.toCharArray()){
                                if(!Character.isDigit(c)){
                                    isNumeric = false;
                                    break;
                                }
                            }

                            if (!s.equals("") && isNumeric) {
                                System.out.println("valid key");
                                encrypt.setDisable(false);
                                invalidKey.setVisible(false);
                            }
                            else{
                                System.out.println("invalid key");
                                encrypt.setDisable(true);
                                invalidKey.setVisible(true);
                            }
                        }
                    };
            keyArea.setOnKeyReleased(onKeyEnter);

            final File[] file = new File[1];
            //Listener to validate file input
            EventHandler<ActionEvent> event =
                    new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent e)
                        {

                            // get the file selected
                            file[0] = fil_chooser.showOpenDialog(stage);

                            if (file[0] != null) {
                                fileSelected.setText(file[0].getAbsolutePath() + " selected");
                                keyArea.setDisable(false);
                                button.setText("SELECTED");
                            }
                            else{
                                fileSelected.setText("No file selected");
                                keyArea.setText("");
                                keyArea.setDisable(true);
                                encrypt.setDisable(true);
                                button.setText("select file");
                            }
                        }
                    };
            button.setOnAction(event);


            //Listener to GO button(encrypt/decrypt)
            EventHandler<MouseEvent> operation =
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try{

                                FileInputStream fis = new FileInputStream(file[0]);

                                byte[] data = new byte[fis.available()];


                                int total = (int)fis.read(data)/100;
                                if(data.length<1000000)
                                    total = 1;
                                int key = Integer.parseInt(keyArea.getText());
                                for(int i=0;i< data.length;i+=total){
                                    System.out.println(i+"/"+data.length);
                                    data[i] = (byte) (data[i]^key);

                                }
                                System.out.println("OK");

                                FileOutputStream fos = new FileOutputStream(file[0]);
                                fos.write(data);
                                fos.close();
                                fis.close();
                                file[0] = null;
                                fileSelected.setText("ENCRYPTED");
                                keyArea.setText("");
                                encrypt.setDisable(true);
                                keyArea.setDisable(true);
                                button.setText("select file");
                                System.out.println("Encrypted");

                            }
                            catch (Exception e){
                                System.out.println("error "+e.getMessage());
                            }

                        }
                    };
            encrypt.setOnMouseClicked(operation);


            VBox vbox = new VBox(20, heading,hBoxForFile,fileSelected,hboxForKey,invalidKey);
            vbox.setBackground(new Background(new BackgroundFill(Color.color(1,0.8,0),CornerRadii.EMPTY,Insets.EMPTY)));
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(30,0,0,20));

            Scene scene = new Scene(vbox, 600, 250);
            stage.setScene(scene);

            stage.show();
        }

        catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    // Main Method
    public void run()
    {

        // launch the application
        launch();
    }
}
