package f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
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

        sb.append(String.format("%-30s %-30s %-30s %-24s %-20s %-30s %-16s %-20s\n", //format for alignment
        "First Name", "Last Name", "Title", "City", "Country", "Phone", "Active", "Supervisor"));   //headings
        sb.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

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
                sb.append(String.format("%-30s %-30s %-30s %-24s %-20s %-30s %-16s %-20s\n",//format for alignment
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

        sb.append(String.format("%-30s %-30s %-30s %-24s %-20s %-30s %-16s %-20s\n", //format for alignment
        "First Name", "Last Name", "Title", "City", "Country", "Phone", "Active", "Supervisor"));   //headings
        sb.append("----------------------------------------------------------------------------------------------------------\n");

        String query = "SELECT employee.FirstName, employee.LastName, employee.Title, employee.City, employee.Country, employee.Phone, IF(employee.Title = 'Sales Support Agent','Yes', 'No') AS Active, IFNULL(CONCAT(E.FirstName, ' ', E.LastName),'None') AS 'Supervisor' FROM employee LEFT JOIN employee AS E ON employee.ReportsTo = E.EmployeeId;";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()){
                sb.append(String.format("%-30s %-30s %-30s %-24s %-20s %-30s %-16s %-20s\n",//format for alignment
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
}
