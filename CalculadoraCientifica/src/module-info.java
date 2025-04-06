module Salus {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.scripting;
	
	opens application to javafx.graphics, javafx.fxml;
}
