import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

public class Check {
	
	Boolean[] cagesCorrect;
	Boolean[] rowsCorrect;
	Boolean[] columnsCorrect;
	
	
	int gridWidth;
	int gridHeight;
	public TextField[] gridText;
	
	int compare;
	
	GridPane actGrid;
	Grid grid;
	AnchorPane[] dokuGrid;
	
	public void checkWin(TextField[] gridText, int gridWidth, int gridHeight, Grid grid) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		this.grid = grid;
		this.gridText = gridText;
		dokuGrid = grid.getDokuGrid();
		
		System.out.println("hi");
		checkCages();
		checkRows();
		checkColumns();
		if (((checkRows()) && (checkColumns()) && checkCages()) == true) {
			endGame();
		}
	}
	
	private boolean checkRows() {
		
		rowsCorrect = new Boolean[gridHeight];
		
		int p = 0;
		for(ArrayList<TextField> row : grid.allRows) {
			if(checkThis(row, p)) {
				rowsCorrect[p] = true;
			} else {
				rowsCorrect[p] = false;
			}
			p++;
		}
		
		if(areAllTrue(rowsCorrect)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkColumns() {
		
		columnsCorrect = new Boolean[gridWidth];
		
		int p = 0;
		for(ArrayList<TextField> column : grid.allColumns) {
			if(checkThis(column, p)) {
				columnsCorrect[p] = true;
			} else {
				columnsCorrect[p] = false;
			}
			p++;
		}
		
		if(areAllTrue(columnsCorrect)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	private Boolean checkThis(ArrayList<TextField> thisRow, int p) {
		for (int i = 0; i < thisRow.size(); i++) {
			if (!thisRow.get(i).getText().isEmpty()) {
				compare = Integer.parseInt(thisRow.get(i).getText());
				for(int a = i + 1; a < thisRow.size(); a++) {
					if(!thisRow.get(a).getText().isEmpty()) {
						if(compare == Integer.parseInt(thisRow.get(a).getText())) {
							return false;
						}
					}
				}
			}else{
				 return false;
			}
		}
		return true;
	}
	
	
	private boolean checkCages() {
		cagesCorrect = new Boolean[grid.getLines()];
		int i = 0;
		
		for(ArrayList<TextField> cage : grid.getAllCageText()) {
			int cornerNum = grid.getAllCornerNum().get(i);
			
			if(grid.getAllCornerSymbols().get(i).equals("x")) {
				int total = 1;
				for(TextField num : cage) {
					if(!num.getText().isEmpty()) {     // if it is not empty
						total = total * Integer.parseInt(num.getText());
					}
				}
				if(total == cornerNum) {
					cagesCorrect[i] = true;
				} else {
					cagesCorrect[i] = false;
				}
			}
			
			else if(grid.getAllCornerSymbols().get(i).equals("+")) {
				int total = 0;
				for(TextField num : cage) {
					if(!num.getText().isEmpty()) {     // if it is not empty
						total = total + Integer.parseInt(num.getText());
					}
				}
				if(total == cornerNum) {
					cagesCorrect[i] = true;
				} else {
					cagesCorrect[i] = false;
				}
			}
			
			else if(grid.getAllCornerSymbols().get(i).equals("-")) {
				ArrayList<Integer> order = new ArrayList<Integer>();
				for(TextField num : cage) {
					if(!num.getText().isEmpty()) {
						order.add(Integer.parseInt(num.getText()));
					}
				}
				Collections.sort(order, Collections.reverseOrder());
				int total = 0;
				for(int q = 0; q < order.size(); q++) {
					if(q == 0) {
						total = order.get(q); 
					} else {
						total = total - order.get(q);
					}
				}
				if(total == cornerNum) {
					cagesCorrect[i] = true;
				} else {
					cagesCorrect[i] = false;
				}
			}
			
			else if(grid.getAllCornerSymbols().get(i).equals("÷")) {
				ArrayList<Integer> order = new ArrayList<Integer>();
				for(TextField num : cage) {
					if(!num.getText().isEmpty()) {
						order.add(Integer.parseInt(num.getText()));
					}
				}
				Collections.sort(order, Collections.reverseOrder());
				int total = 0;
				for(int p = 0; (p < order.size() - 1); p++) {
					if(order.get(p) % order.get(p + 1) == 0) {
						total = order.get(p) / order.get(p + 1);
					} else {
						total = 0;
					}
				}
				
				if(total == cornerNum) {
					cagesCorrect[i] = true;
				} else {
					cagesCorrect[i] = false;
				}
			}
			i++;
		}
		
		if(areAllTrue(cagesCorrect)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private boolean areAllTrue(Boolean[] array) {
		if(Arrays.asList(array).contains(false)) {
			return false;
		} else {
			return true;
		}
	}
	
	private void endGame() {
		System.out.println("Winner winner chicken dinner");
		Alert alert = new Alert(AlertType.INFORMATION, "WINNER!");
		
		alert.setTitle("Congratulations!");
		alert.setHeaderText("You Won!!!");
		Optional<ButtonType> result = alert.showAndWait();
	}
	
	
	public Boolean getCageTrue(int index) {
		return cagesCorrect[index]; 
	}
	
	public Boolean getRowTrue(int index) {
		return rowsCorrect[index];
	}
	
	public Boolean getColumnTrue(int index) {
		return columnsCorrect[index];
	}
	 
}




