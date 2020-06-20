package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class RecordController implements Initializable{
	@FXML
	private Button backtoMainmenu;
	@FXML private TableView<TableRowModel> table;
	@FXML private TableColumn<TableRowModel, String> num;
	@FXML private TableColumn<TableRowModel, String> kind;
	@FXML private TableColumn<TableRowModel, String> subject;
	@FXML private TableColumn<TableRowModel, String> amount;
	@FXML private TableColumn<TableRowModel, String> balance;
	
	ObservableList<TableRowModel> list=null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		list=FXCollections.observableArrayList();
		/*
		 * 리스트에 거래내역 출력하기
		 */
		/*
		 for(Transaction d: ) {
		 
			list.add(new TableRowModel(d.number,d.kind,d.subject,d.amount,d.balance);//거래내역 정보를 가져와 list에 저장하기
		}
		*/
		backtoMainmenu.setOnAction(e -> {
            Parent login;
            try {
                login = FXMLLoader.load(getClass().getResource("Mainmenu.fxml"));
                Scene scene = new Scene(login);
        
                Stage primaryStage = (Stage)backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기
            
                primaryStage.setScene(scene);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
		}
	
	

}
