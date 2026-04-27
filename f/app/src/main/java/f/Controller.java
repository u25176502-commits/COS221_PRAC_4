package f;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
}
