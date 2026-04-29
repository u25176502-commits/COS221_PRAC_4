package f;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
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
        loadCustomerPicker();

        
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

        sb.append(String.format("%-15s %-15s %-20s %-15s %-15s %-20s %-10s %-20s\n", "First Name", "Last Name", "Title", "City", "Country", "Phone", "Active", "Supervisor"));
        sb.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

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
                sb.append(String.format("%-15s %-15s %-20s %-15s %-15s %-20s %-10s %-20s\n",
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Title"),
                    rs.getString("City"),
                    rs.getString("Country"),
                    rs.getString("Phone"),
                    rs.getString("Active"),
                    rs.getString("Supervisor")
                ));
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

        sb.append(String.format("%-15s %-15s %-20s %-15s %-15s %-20s %-10s %-20s\n", "First Name", "Last Name", "Title", "City", "Country", "Phone", "Active", "Supervisor"));
        sb.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT employee.FirstName, employee.LastName, employee.Title, employee.City, employee.Country, employee.Phone, IF(employee.Title = 'Sales Support Agent','Yes', 'No') AS Active, IFNULL(CONCAT(E.FirstName, ' ', E.LastName),'None') AS 'Supervisor' FROM employee LEFT JOIN employee AS E ON employee.ReportsTo = E.EmployeeId;";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(String.format("%-15s %-15s %-20s %-15s %-15s %-20s %-10s %-20s\n",
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Title"),
                    rs.getString("City"),
                    rs.getString("Country"),
                    rs.getString("Phone"),
                    rs.getString("Active"),
                    rs.getString("Supervisor")
                ));
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

        sb.append(String.format("%-50s %-50s %-50s %-50s %-50s %-50s %-50s %-50s\n", //format for alignment
        "Name", "UnitPrice", "Album", "Genre", "Format", "Milliseconds", "Bytes", "Composer"));   //headings
        sb.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT track.Name, track.UnitPrice, album.Title AS Album, genre.Name AS Genre, mediatype.`Name` AS Format, track.Milliseconds, track.Bytes, track.Composer FROM track LEFT JOIN album ON track.AlbumId = album.AlbumId LEFT JOIN genre ON track.GenreId = genre.GenreId LEFT JOIN mediatype ON track.MediaTypeId = mediatype.MediaTypeId;";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(String.format("%-50s %-50s %-50s %-50s %-50s %-50s %-50s %-100s\n",//format for alignment
                    rs.getString("Name"),
                    rs.getString("UnitPrice"),
                    rs.getString("Album"),
                    rs.getString("Genre"),
                    rs.getString("Format"),
                    rs.getString("Milliseconds"),
                    rs.getString("Bytes"),
                    rs.getString("Composer")
                    
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
                sb.append("\t\t\t\t\t\t");
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
    @FXML private ComboBox drpCustomers;
    @FXML private Text txtTotalAmout;
    @FXML private Text txtNrPurchases;
    @FXML private Text txtMostRecent;
    @FXML private Text txtFavGenre;

    public void loadFromChoice(){
        String input = drpCustomers.getSelectionModel().getSelectedItem().toString();

        // Clear existing text first
        txtTotalAmout.setText("");
        txtNrPurchases.setText("");
        txtMostRecent.setText("");
        txtFavGenre.setText("");

        String query;
        try {Connection conn = DatabaseManager.getConnection();
            //total
            query =  "SELECT SUM(Total) AS Total FROM customer RIGHT JOIN invoice ON invoice.CustomerId = customer.CustomerId WHERE CONCAT(FirstName, ' ',LastName) = '" + input +"';";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query); 

            if (rs.next()) {
                txtTotalAmout.setText(rs.getString("Total"));
            }
            
            //nr of purchases
            query =  "SELECT COUNT(Total) AS Count FROM customer RIGHT JOIN invoice ON invoice.CustomerId = customer.CustomerId WHERE CONCAT(FirstName, ' ',LastName) = '" + input +"';";
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query); 

            if (rs2.next()) {
                txtNrPurchases.setText(rs2.getString("Count"));
            }

            //most recent purcahse
            query =  "SELECT MAX(InvoiceDate) AS MostRecentDate FROM customer RIGHT JOIN invoice ON invoice.CustomerId = customer.CustomerId WHERE CONCAT(FirstName, ' ',LastName) = '" + input + "';";
            Statement stmt3 = conn.createStatement();
            ResultSet rs3 = stmt3.executeQuery(query); 

            if (rs3.next()) {
                txtMostRecent.setText(rs3.getString("MostRecentDate"));   
            }

            //FAV GENRE - Get customer ID first, then use it in the query
            query = "SELECT CustomerId FROM customer WHERE CONCAT(FirstName, ' ', LastName) = '" + input + "'";
            Statement stmt4 = conn.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query);
            
            if (rs4.next()) {
                int customerId = rs4.getInt("CustomerId");
                
                // Now get the favorite genre for this customer
                query = "SELECT genre.`Name` FROM (SELECT t.TrackId, track.GenreId FROM (SELECT invoiceline.TrackId, invoice.CustomerId, invoiceline.Quantity FROM invoiceline JOIN invoice ON invoiceline.InvoiceId = invoice.InvoiceId WHERE invoice.CustomerId = " + customerId + ") AS t JOIN track ON track.TrackId = t.TrackId) AS r JOIN genre ON genre.GenreId = r.GenreId GROUP BY genre.`Name` ORDER BY COUNT(*) DESC LIMIT 1";
                Statement stmt5 = conn.createStatement();
                ResultSet rs5 = stmt5.executeQuery(query); 

                if (rs5.next()) {
                    txtFavGenre.setText(rs5.getString("Name"));   
                }
            }

        } catch (SQLException e) {
            tabInactive.setText("Database error: " + e.getMessage());
        }

    }

    @FXML public void loadCustomerPicker(){

        String query = "SELECT CONCAT(FirstName, ' ', LastNAME) AS L FROM customer";
        try{
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                drpCustomers.getItems().add(rs.getString("L"));
            }
            

        } catch (SQLException e) {
            tabInactive.setText("Database error: " + e.getMessage());
        }           
    }

    @FXML
    public void handleDelete() {
        // 1. Create the Pop-up Dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Record");
        dialog.setHeaderText("Enter the ID of the record you want to delete:");
        dialog.setContentText("ID:");

        // 2. Get the result
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(id -> {
            // Confirm before deleting
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText("Are you sure you want to delete ID: " + id + "?");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                String query = "DELETE FROM customer WHERE CustomerId = "+id+";";

                try (Connection conn = DatabaseManager.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(query)) {
                    
                    pstmt.setString(1, id);
                    int affectedRows = pstmt.executeUpdate();

                    if (affectedRows > 0) {
                        System.out.println("Successfully deleted record " + id);
                        // Refresh your UI table/TextArea here if needed
                    } else {
                        System.out.println("No record found with ID: " + id);
                    }

                } catch (SQLException e) {
                    System.out.println("Database Error: " + e.getMessage());
                }
            }
        });
    }
}
