package application;

import java.io.IOException;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import form.Transaction;
import form.TransactionDAO;
import form.Enum.TransactionType;
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

	private TransactionDAO dao = TransactionDAO.getInstance();
	
	ObservableList<TableRowModel> list=null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		list = FXCollections.observableArrayList();
		
		try {
			Transaction[] transactionList = dao.getTransactionList();

			for (Transaction transaction : transactionList) {
				if(transaction.getTransactionType() == TransactionType.TRANSFER) {
					// 나 -> 타인
					transaction.getTo().getAccountNumber();
				}
				// list.add(new TableRowModel(num, kind, subject, amount, balance))
				//list.add(new TableRowModel(0, transaction.getTransactionType().toString(),
				//				"출금", amount, balance));
				// TODO 맞게 작성
			}
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}

		backtoMainmenu.setOnAction(e -> {
            Parent login;
            try {
                login = FXMLLoader.load(getClass().getResource("fxml/Mainmenu.fxml"));
                Scene scene = new Scene(login);
        
                Stage primaryStage = (Stage)backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기
            
                primaryStage.setScene(scene);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
		}
	
	

}
