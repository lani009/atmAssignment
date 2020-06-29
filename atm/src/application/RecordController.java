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
import javafx.scene.control.Label;
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
	@FXML 
	private Label message;
	private int i=0;
	private TransactionDAO dao = TransactionDAO.getInstance();
	
	//내용을 추가할 리스트 초기화
	ObservableList<TableRowModel> list=null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		list = FXCollections.observableArrayList();
		
		try {
			Transaction[] transactionList = dao.getTransactionList();

			if(transactionList == null) {
				message.setText("There is no transactional information");
				return;
				// 거래내역이 없을 경우임. 거래내역이 없을 경우의 예외처리
			}
			
			for (Transaction transaction : transactionList) {
				if(transaction.getTransactionType() == TransactionType.TRANSFER) {
					// 나 -> 타인일 때 리스트에 추가 
					list.add(new TableRowModel(String.format("%d", i), transaction.getTransactionType().toString(), transaction.getTo().getAccountNumber().toString(),
							transaction.getAmount().toString(), transaction.getFrom().getBalance().toString()));
				}
				// 그 밖의 상황일 때 리스트에 추가
				else
					list.add(new TableRowModel("0", transaction.getTransactionType().toString(),"",
						transaction.getAmount().toString(), transaction.getFrom().getBalance().toString()));
				//번호 증가
				i++;
				
			}
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		//fxml에 적용시키기
		num.setCellValueFactory(cellData -> cellData.getValue().getNum()); // 람다식 사용 
	    kind.setCellValueFactory(cellData -> cellData.getValue().getKind());
	    amount.setCellValueFactory(cellData -> cellData.getValue().getAmount());
	    subject.setCellValueFactory(cellData -> cellData.getValue().getSubject());
	    balance.setCellValueFactory(cellData -> cellData.getValue().getBalance());
	    table.setItems(list);
	    
	    //메인 화면으로 돌아가기
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
