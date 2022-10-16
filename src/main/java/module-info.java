module hr.algebra.airhockey {
    requires javafx.controls;
    requires javafx.fxml;


    opens hr.algebra.airhockey to javafx.fxml;
    exports hr.algebra.airhockey;
    exports hr.algebra.airhockey.models;
    opens hr.algebra.airhockey.models to javafx.fxml;
}