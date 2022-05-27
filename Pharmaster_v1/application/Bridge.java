package application;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Bridge {
	
	VBox vb = new VBox();
	ObservableList<Node> list;
	
	void setCartItems(Node item) {
		vb.getChildren().add(item);
		list = vb.getChildren();
	}
	
	void initCartItems() {
		vb.getChildren().add(new Label("QNT       Name"));
		list =  vb.getChildren();
	}
}
