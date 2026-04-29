package f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {

    // 1. Link the UI component
    @FXML private TextArea tabEmployee;
    @FXML private ComboBox cbField;
    @FXML private TextField filterValue;


    @FXML
    public void initialize() {
        cbField.getItems().addAll(
            "Name",
            "City"
        );

        initLoadEmployee();
        LoadTrack();
        tabEmployee.setStyle("-fx-tab-size: 30;");

        
    }


    // 2. Link the Button click event
    @FXML
    public void handleEmployeeData() {
        fetchEmployee();
    }
    
    //Employees tab
    @FXML
    private void fetchEmployee() {
        //stringbuilder is more efficient than using a string
        StringBuilder sb = new StringBuilder();

        sb.append("First Name\tLast Name\tTitle\tCity\tCountry\tPhone\tActive\tSupervisor\n");

        String val = filterValue.getText();

        String query = "";
        if (val == null || val.trim().isEmpty()){
            initLoadEmployee();
            return;
        } else {
            String type = (String) cbField.getValue();
            if ("Name".equals(type)){
                query = "SELECT employee.FirstName, employee.LastName, employee.Title, employee.City, employee.Country, employee.Phone, IF(employee.Title = 'Sales Support Agent','Yes', 'No') AS Active, IFNULL(CONCAT(E.FirstName, ' ', E.LastName),'None') AS 'Supervisor' FROM employee LEFT JOIN employee AS E ON employee.ReportsTo = E.EmployeeId WHERE employee.FirstName = '" + val + "';";
            } else {
                query = "SELECT employee.FirstName, employee.LastName, employee.Title, employee.City, employee.Country, employee.Phone, IF(employee.Title = 'Sales Support Agent','Yes', 'No') AS Active, IFNULL(CONCAT(E.FirstName, ' ', E.LastName),'None') AS 'Supervisor' FROM employee LEFT JOIN employee AS E ON employee.ReportsTo = E.EmployeeId WHERE employee.City = '" + val + "';";
            }
        }
        

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(rs.getString("FirstName"));
                sb.append('\t');
                sb.append(rs.getString("LastName"));
                sb.append('\t');
                sb.append(rs.getString("Title"));
                sb.append('\t');
                sb.append(rs.getString("City"));
                sb.append('\t');
                sb.append(rs.getString("Country"));
                sb.append('\t');
                sb.append(rs.getString("Phone"));
                sb.append('\t');
                sb.append(rs.getString("Active"));
                sb.append('\t');
                sb.append(rs.getString("Supervisor"));
                sb.append('\n');
            }
            
            // 3. Update the UI
            tabEmployee.setText(sb.toString());
            // tabEmployee.setText(query+'\n'+cbField.getValue()); //testing

        } catch (SQLException e) {
            tabEmployee.setText("Database error: " + e.getMessage());
        }
    }
    
    @FXML
    private void initLoadEmployee() {
        //stringbuilder is more efficient than using a string
        StringBuilder sb = new StringBuilder();

        sb.append("First Name\tLast Name\tTitle\tCity\tCountry\tPhone\tActive\tSupervisor\n");
        sb.append("----------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT employee.FirstName, employee.LastName, employee.Title, employee.City, employee.Country, employee.Phone, IF(employee.Title = 'Sales Support Agent','Yes', 'No') AS Active, IFNULL(CONCAT(E.FirstName, ' ', E.LastName),'None') AS 'Supervisor' FROM employee LEFT JOIN employee AS E ON employee.ReportsTo = E.EmployeeId;";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(rs.getString("FirstName"));
                sb.append('\t');
                sb.append(rs.getString("LastName"));
                sb.append('\t');
                sb.append(rs.getString("Title"));
                sb.append('\t');
                sb.append(rs.getString("City"));
                sb.append('\t');
                sb.append(rs.getString("Country"));
                sb.append('\t');
                sb.append(rs.getString("Phone"));
                sb.append('\t');
                sb.append(rs.getString("Active"));
                sb.append('\t');
                sb.append(rs.getString("Supervisor"));
                sb.append('\n');
            }
            
            // 3. Update the UI
            tabEmployee.setText(sb.toString());

        } catch (SQLException e) {
            tabEmployee.setText("Database error: " + e.getMessage());
        }
    }

    @FXML 
    public void ShowInsertPop(){
        try{
            //loads pop up
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insertPopUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.initModality(Modality.APPLICATION_MODAL); //makes other windows uninteractible with
            stage.setTitle("COS 221 PRAC 4");
            stage.setScene(scene);
            // stage.setResizable(false);
            stage.show();
            loadSpinners();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    //TRACKS
    @FXML private ComboBox cmbGenre;
    @FXML private ComboBox cmbAlbum;
    @FXML private ComboBox cmbMedia;

    public void loadSpinners(){
        try {
            //genre
            String query = "SELECT Name from genre;";                
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query); 

            while (rs.next()){
                cmbGenre.getItems().add(rs.getString("Name"));
            }
            
            //MediaType
            query = "SELECT Name from mediatype;";

            Connection conn2 = DatabaseManager.getConnection();
            Statement stmt2 = conn2.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);

            while (rs2.next()){
                cmbMedia.getItems().add(rs2.getString("Name"));
            }

            //Album
            query = "SELECT Title from album;";

            Connection conn3 = DatabaseManager.getConnection();
            Statement stmt3 = conn3.createStatement();
            ResultSet rs3 = stmt3.executeQuery(query);

            while (rs3.next()){
                cmbMedia.getItems().add(rs3.getString("Name"));
            }     

        } catch (SQLException e) {
            tabEmployee.setText("Database error: " + e.getMessage());
        }
    }

    // @FXML private ComboBox cmbGenre;
    // @FXML private ComboBox cmbAlbum;
    // @FXML private ComboBox cmbMedia;
    @FXML private TextField txtName;
    @FXML private TextField txtComposer;
    @FXML private Spinner spnBytes;

    @FXML
    public void insertion(){

    }

    @FXML private TextArea tabTracks;

    @FXML
    public void LoadTrack(){
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-30s %-30s %-30s %-24s %-20s %-30s %-16s %-20s\n", //format for alignment
        "Name", "Composer", "Album", "Genre", "Format", "Milliseconds", "Bytes", "UnitPrice"));   //headings
        sb.append("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT track.Name, track.Composer, album.Title AS Album, genre.Name AS Genre, mediatype.`Name` AS Format, track.Milliseconds, track.Bytes, track.UnitPrice FROM track LEFT JOIN album ON track.AlbumId = album.AlbumId LEFT JOIN genre ON track.GenreId = genre.GenreId LEFT JOIN mediatype ON track.MediaTypeId = mediatype.MediaTypeId;";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(String.format("%-30s %-30s %-30s %-24s %-20s %-30s %-16s %-20s\n",//format for alignment
                    rs.getString("Name"),
                    rs.getString("Composer"),
                    rs.getString("Album"),
                    rs.getString("Genre"),
                    rs.getString("Format"),
                    rs.getString("Milliseconds"),
                    rs.getString("Bytes"),
                    rs.getString("UnitPrice")
                ));
            }
            
            // 3. Update the UI
            tabTracks.setText(sb.toString());

        } catch (SQLException e) {
            tabTracks.setText("Database error: " + e.getMessage());
        }    
    }

    @FXML private TextArea tabGenre;
    @FXML
    public void LoadReport(){
        StringBuilder sb = new StringBuilder();

        // sb.append("Genre");   //headings
        // sb.append("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT T.Genre, SUM(T.Revenue) AS 'Total Revenue' FROM (SELECT genre.Name AS Genre, track.UnitPrice AS Revenue FROM invoiceline LEFT JOIN track ON invoiceline.TrackId = track.TrackId LEFT JOIN genre ON genre.GenreId = track.GenreId) AS T GROUP BY Genre;";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(rs.getString("Genre"));
                sb.append(": $");
                sb.append(rs.getFloat("Total Revenue"));
                sb.append('\n');
            }
            
            // 3. Update the UI
            tabGenre.setText(sb.toString());

        } catch (SQLException e) {
            tabGenre.setText("Database error: " + e.getMessage());
        }    
    }

    @FXML private TextArea tabInactive;
    @FXML
    public void findInactive(){
        StringBuilder sb = new StringBuilder();

        sb.append("FIRST NAME");
        sb.append("\t\t\t");
        sb.append("LAST NAME");
        sb.append('\n'); //headings
        sb.append("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT FirstName, Lastname FROM customer WHERE CustomerId IN (SELECT CustomerId FROM invoice GROUP BY CustomerId HAVING TIMESTAMPDIFF(year, MAX(InvoiceDate), NOW()) > 2);";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(rs.getString("FirstName"));
                sb.append("\t\t\t");
                sb.append(rs.getString("LastName"));
                sb.append('\n');
            }
            
            // 3. Update the UI
            tabInactive.setText(sb.toString());

        } catch (SQLException e) {
            tabInactive.setText("Database error: " + e.getMessage());
        }            
    }

    @FXML private TextField edttInactiveUsers;

    @FXML
    public void searchInactive(){
        StringBuilder sb = new StringBuilder();
        String value = edttInactiveUsers.getText();

        sb.append("FIRST NAME");
        sb.append("\t\t\t");
        sb.append("LAST NAME");
        sb.append('\n'); //headings
        sb.append("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT FirstName, Lastname FROM customer WHERE CustomerId IN (SELECT CustomerId FROM invoice GROUP BY CustomerId HAVING TIMESTAMPDIFF(year, MAX(InvoiceDate), NOW()) > 2) AND CONCAT(FirstName, ' ', LastNAME) = "+ value +";";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(rs.getString("FirstName"));
                sb.append("\t\t\t");
                sb.append(rs.getString("LastName"));
                sb.append('\n');
            }
            
            // 3. Update the UI
            tabInactive.setText(sb.toString());

        } catch (SQLException e) {
            tabInactive.setText("Database error: " + e.getMessage());
        }            
    }   

    //recommendations
    @FXML
    public void loadFromChoice(){
        
    }


}
