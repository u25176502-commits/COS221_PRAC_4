package f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;

public class InsertPopUpController {

    @FXML private ComboBox cmbGenre;
    @FXML private ComboBox cmbAlbum;
    @FXML private ComboBox cmbMedia;
    @FXML private TextField txtName;
    @FXML private TextField txtComposer;
    @FXML private Spinner spnBytes;
    @FXML private Spinner spnPrice;
    @FXML private Spinner spnLength;

    @FXML
    public void initialize() {
        loadSpinners();
    }

    public void loadSpinners(){
        try {
            // Genre
            String query = "SELECT Name from genre;";                
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query); 

            while (rs.next()){
                cmbGenre.getItems().add(rs.getString("Name"));
            }
            rs.close();
            stmt.close();
            
            // MediaType
            query = "SELECT Name from mediatype;";
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);

            while (rs2.next()){
                cmbMedia.getItems().add(rs2.getString("Name"));
            }
            rs2.close();
            stmt2.close();

            // Album - FIXED: was adding to cmbMedia instead of cmbAlbum
            query = "SELECT Title from album;";
            Statement stmt3 = conn.createStatement();
            ResultSet rs3 = stmt3.executeQuery(query);

            while (rs3.next()){
                cmbAlbum.getItems().add(rs3.getString("Title"));
            }
            rs3.close();
            stmt3.close();
            
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void insertion(){
        // TODO: Implement track insertion logic
        System.out.println("Insert button clicked!");
    }
}