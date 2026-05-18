module org.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens org.example.tictactoe to javafx.fxml;
    exports org.example.tictactoe;
}