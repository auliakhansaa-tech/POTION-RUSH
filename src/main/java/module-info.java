module com.potionrush {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.potionrush to javafx.fxml;
    exports com.potionrush;
}