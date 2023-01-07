module hr.algebra.airhockey {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.naming;
    requires org.apache.commons.lang3;


    opens hr.algebra.airhockey to javafx.fxml;
    exports hr.algebra.airhockey;
    exports hr.algebra.airhockey.models;
    exports hr.algebra.airhockey.rmiserver to java.rmi;
    opens hr.algebra.airhockey.models to javafx.fxml;
}