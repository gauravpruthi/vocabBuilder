/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabbuilder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author gpruthi
 */
public class VocabBuilder extends Application {
    
    Connection con;
    public static final String vocabTable = "vocabRecords";
    TextField wordField = null;
    TextField meaningField = null;
    TextArea synArea = null;
    TextArea antArea = null;
    TextArea descArea = null;
    
    public VocabBuilder() throws SQLException {
        wordField = new TextField();
        meaningField = new TextField();
        synArea = new TextArea();
        antArea = new TextArea();
        descArea = new TextArea();
        this.con = DriverManager.getConnection("jdbc:sqlite:nbproject\\VocabData.sqlite");
    }
    
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        BaseData baseData = new BaseData(); // base data to share
        // ComboBox
        ObservableList<Integer> options
                = FXCollections.observableArrayList(
                        1,
                        2,
                        3,
                        4,
                        5
                );
        ComboBox comboBox = new ComboBox(options);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Scene scene = new Scene(grid, 450, 525);
        primaryStage.setScene(scene);

        Text scenetitle = new Text("Welcome to VocabBuilder");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(scenetitle, 0, 0);

        Label wordLabel = new Label("Word:");
        grid.add(wordLabel, 0, 1);

        grid.add(wordField, 1, 1);

        Label meanLabel = new Label("Meaning:");
        grid.add(meanLabel, 0, 2);
        grid.add(meaningField, 1, 2);

        Label synLabel = new Label("Synonyms:");
        grid.add(synLabel, 0, 3);

        grid.add(synArea, 1, 3); //, 2, 1);
        synArea.setWrapText(true);

        Label antLabel = new Label("Antonyms:");
        grid.add(antLabel, 0, 4);

        grid.add(antArea, 1, 4); //, 2, 1);
        antArea.setWrapText(true);

        Label descLabel = new Label("Description:");
        grid.add(descLabel, 0, 5);
        
        descArea.setWrapText(true);
        grid.add(descArea, 1, 5); //, 2, 1);

        Label levelLabel = new Label("Level");
        grid.add(levelLabel, 0, 6);

        comboBox.setItems(options);
        comboBox.setVisibleRowCount(5);
        grid.add(comboBox, 1, 6);
        comboBox.getSelectionModel().select(1);

        Button saveButton = new Button("Save");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn.getChildren().add(saveButton);
        grid.add(hbBtn, 0, 7);

        //Add cancel button
        Button cancelBtn = new Button("Cancel");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(cancelBtn);
        grid.add(hbBtn2, 1, 7);

        saveButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                if(wordField.getText().length() == 0 || meaningField.getText().length() == 0)
                {
                    JOptionPane.showMessageDialog(null, "Word and Meaning fields are mandatory", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                baseData.word = wordField.getText();
                baseData.meaning = meaningField.getText();
                baseData.synonyms = synArea.getText();
                baseData.antonyms = antArea.getText();
                baseData.description = descArea.getText();
                baseData.level = (int) comboBox.getValue();
 
                try {
                    //Call the function to save the data
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VocabBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    
                    if (con != null) {
                        DatabaseMetaData meta = con.getMetaData();
                        System.out.println("The driver name is " + meta.getDriverName());
                        System.out.println("A new database has been created.");
                        saveData(baseData);
                    }
                    System.out.println("Connection successfully established");
                } catch (SQLException ex) {
                    Logger.getLogger(VocabBuilder.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
                }
            }

        });

        cancelBtn.setOnAction (new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Platform.exit();
            }
        });
            

        Button fetchBtn = new Button("Fetch");
        HBox hbBtn3 = new HBox(10);
        hbBtn3.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn3.getChildren().add(fetchBtn);
        grid.add(hbBtn3, 0, 9);

        fetchBtn.setOnAction (new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    try {
                        boolean isExists = checkExistenceOfWord(wordField.getText());
                        if(!isExists) {
                            JOptionPane.showMessageDialog(null, "Word doesn't exist", "alert", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(VocabBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                    
            }
        });
        
        Button updateBtn = new Button("Update");
        HBox hbBtn4 = new HBox(10);
        hbBtn4.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn4.getChildren().add(updateBtn);
        grid.add(hbBtn4, 1, 8);

        primaryStage.show ();
    }
            
    private void saveData(BaseData baseData)  {
        
        String sql = "SELECT word, meaning FROM vocabRecords";
        String word = baseData.word;
        String meaning = baseData.meaning;
        String synonyms = baseData.synonyms;
        String antonyms = baseData.antonyms;
        String description = baseData.description;
        int level = baseData.level;
        
        boolean doesWordExists = false;
        String query = "INSERT INTO vocabTable (word, meaning, synonyms, antonyms, description, level) VALUES(?,?,?,?,?,?);";
        // Replace variable with actual table name...
        String sqlQuery  = query.replace("vocabTable",vocabTable);
        
        try {
        doesWordExists = checkExistenceOfWord(baseData.word);
        if(doesWordExists) {
            JOptionPane.showMessageDialog(null, "Word already exists", "alert", JOptionPane.ERROR_MESSAGE);
            return;
            
        }
            PreparedStatement statement = con.prepareStatement(sqlQuery);
            statement.setString(1,word);
            statement.setString(2,meaning);
            statement.setString(3,synonyms);
            statement.setString(4,antonyms);
            statement.setString(5,description);
            statement.setInt(6,level);

            statement.executeUpdate();

            con.commit();
            JOptionPane.showMessageDialog(null, "Word added successfully", "alert", JOptionPane.ERROR_MESSAGE);

        }
        catch(Exception sqe) {
            JOptionPane.showMessageDialog(null, "Word addition failed", "alert", JOptionPane.ERROR_MESSAGE);
        }
        // End insert into
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery(sql);

        // loop through the result set
//        while (rs.next()) {
//            System.out.println(rs.getString("word") + "\t"
//            + rs.getString("meaning"));
//        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean checkExistenceOfWord(String word) throws java.sql.SQLException {
        
        //Check if given word exists
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT count(*) FROM vocabTable WHERE word='wordToCheck'";
        String sqlQuery_1  = query.replace("vocabTable",vocabTable);
        String sqlQuery_2  = sqlQuery_1.replace("wordToCheck",word);
        
        int rowCount = -1;
        try {
          stmt = con.createStatement();
          rs = stmt.executeQuery(sqlQuery_2);
          // get the number of rows from the result set
          rs.next();
          rowCount = rs.getInt(1);
          
        } finally {
          rs.close();
          stmt.close();
        }
        if(rowCount == 0)
            return false;
        else
            return true;
    
    }

}
