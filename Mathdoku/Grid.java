import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Grid {
	
	private int gridHeight;
	private int gridWidth;
	private int lines;
	public AnchorPane[] dokuGrid;
	public TextField[] gridText;
	private GridPane actGrid;
		
	private ArrayList<Integer> allCornerNum;
	private ArrayList<ArrayList<AnchorPane>> allCages;
	private ArrayList<ArrayList<TextField>> allCageText;
	private ArrayList<String> cornerTexts;
	private ArrayList<String> allCornerSymbols;
	
	Move move;
	Game game;
	
	ArrayList<ArrayList<TextField>> allRows;
	ArrayList<ArrayList<AnchorPane>> allAnchRows;
	ArrayList<ArrayList<TextField>> allColumns;
	ArrayList<ArrayList<AnchorPane>> allAnchColumns;
	
	private boolean showMistakesOn;
	
	private Font cornerFont = Font.font("Verdanna", FontWeight.BOLD, 10);
	 
	Grid(int gridWidth, int gridHeight, String filePath, Move move, Game game) {
		 this.game = game;
		 this.move = move;
		 this.gridHeight = gridHeight;
		 this.gridWidth = gridWidth;
		 TextField number;
		 Font verdanna = Font.font("Verdanna", FontWeight.BOLD, 20);
	     GridPane mainGrid = new GridPane();
	     mainGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	     mainGrid.setPadding(new Insets(20));
	     dokuGrid = new AnchorPane[gridHeight*gridWidth];
	     Border borders = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)));
	     int a = 0;   
	     gridText = new TextField[gridWidth*gridHeight];
	     for (int y = 0; y < gridWidth; y++) {
	    	 for (int x = 0; x < gridHeight; x++) {
	    		 AnchorPane anchPane = new AnchorPane();
	    		 anchPane.setMinSize(75, 75);
	    		 anchPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    		 anchPane.setBorder(borders);
	    		 number = new TextField();
	    		 gridText[a] = number;
	    		 number.setMaxWidth(75);
	    		 number.setFont(verdanna);
	    		 anchPane.getChildren().add(number);
	    		 anchPane.setTopAnchor(number, 0.0);
	    		 anchPane.setBottomAnchor(number, 0.0);
	    		 anchPane.setLeftAnchor(number, 0.0);
	    		 anchPane.setRightAnchor(number, 0.0);
	    		 mainGrid.add(anchPane, x, y);
	    		 dokuGrid[a] = anchPane;
	    		 a++;
	    	 }
	     }
	     
	     this.actGrid = mainGrid;
	     setTextListener(move);
	     rows();
	     columns();
	     cages(filePath);
	}
	
	private void setTextListener(Move move) {
		for(TextField txt : gridText) {
		 txt.textProperty().addListener((observable, oldValue, newValue) -> {
			    Check checker = new Check();
			    checker.checkWin(gridText, gridHeight, gridWidth, this);
			    
			    move.addMove(oldValue, Arrays.asList(gridText).indexOf(txt));
			    if(move.isRedoAble()) {
			    	game.getRedoButton().setDisable(false);
			    } else {
			    	game.getRedoButton().setDisable(true);
			    }
			    if(move.isUndoAble()) {
			    	game.getUndoButton().setDisable(false);
			    } else {
			    	game.getUndoButton().setDisable(true);
			    }
			    
			    if(checkIfEmpty()) {
			    	game.getClearButton().setDisable(true);
			    } else {
			    	game.getClearButton().setDisable(false);
			    }
			});
		}
	}
	
	private Boolean checkIfEmpty() {
		for(TextField txt : gridText) {
			if(!txt.getText().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	private void rows() {
		
		allRows = new ArrayList<ArrayList<TextField>>();
		allAnchRows = new ArrayList<ArrayList<AnchorPane>>();
		
		for(int i = 0; i < (gridWidth * gridHeight); i = (i + gridWidth)) {
			ArrayList<TextField> row = new ArrayList<TextField>();
			ArrayList<AnchorPane> anchRow = new ArrayList<AnchorPane>();
			for(int a = i; a < (i + gridWidth); a++) {
				row.add(gridText[a]);
				anchRow.add(dokuGrid[a]);
			}
			allRows.add(row);
			allAnchRows.add(anchRow);
		}
	}
	
	private void columns() {
		
		allColumns = new ArrayList<ArrayList<TextField>>();
		allAnchColumns = new ArrayList<ArrayList<AnchorPane>>();
		
		for(int i = 0; i < (gridHeight * gridWidth); i = (i + gridHeight)) {
			ArrayList<TextField> column = new ArrayList<TextField>();
			ArrayList<AnchorPane> anchColumn = new ArrayList<AnchorPane>();
			for(int a = i; a < (i + gridHeight); a++) {
				column.add(gridText[a]);
				anchColumn.add(dokuGrid[a]);
			}
			allColumns.add(column);
			allAnchColumns.add(anchColumn);
		}
	}
	
	private void cages(String filePath) {
		allCages = new ArrayList<ArrayList<AnchorPane>>();
	    allCageText = new ArrayList<ArrayList<TextField>>();
	    cornerTexts = new ArrayList<String>();
	    allCornerNum = new ArrayList<Integer>();
	    allCornerSymbols = new ArrayList<String>();
	    
		GameSaveReader readSave = new GameSaveReader(filePath);
		
		lines = readSave.countLines();
		System.out.println("the file has " + lines + " lines");
		
		GameSaveReader lineReader = new GameSaveReader(filePath);
		
		for(int x = 0; x < lines; x++) {
			ArrayList<AnchorPane> cage = new ArrayList<AnchorPane>();
			ArrayList<TextField> cageText = new ArrayList<TextField>();
			
			String line = lineReader.getLine();
			String[] splitLine = line.split("\\s+");
			
			cornerTexts.add(splitLine[0]);
			
			String[] cells = splitLine[1].split(",");
			for(String cell : cells) {
				int q = Integer.parseInt(cell) - 1;
				// System.out.println(q);
				cage.add(dokuGrid[q]);
				cageText.add(gridText[q]);
			}	
			allCages.add(cage);
			allCageText.add(cageText);
			setCageCorner(cage, splitLine[0]);
		}
		
		
		for(String cornerText : cornerTexts) {
			
			if(cornerText.length() == 1) {
				int num = Integer.parseInt(cornerText);
				allCornerNum.add(num);
				allCornerSymbols.add("+");
			}
			else if(cornerText.length() == 2) {
				int num = Integer.parseInt(cornerText.substring(0,1));
				allCornerNum.add(num);
				
				String string = cornerText.substring(1,2);
				allCornerSymbols.add(string);
			}
			else if(cornerText.length() == 3) {
				int num = Integer.parseInt(cornerText.substring(0,2));
				allCornerNum.add(num);
				
				String string = cornerText.substring(2,3);
				allCornerSymbols.add(string);
			}
			else if(cornerText.length() == 4) {
				int num = Integer.parseInt(cornerText.substring(0,3));
				allCornerNum.add(num);
				
				String string = cornerText.substring(3,4);
				allCornerSymbols.add(string);
			}
		}
	}
	
		
	public void showMistakes() {
		Background redBackground = new Background(new BackgroundFill(Color.RED, new CornerRadii(00), new Insets(0, 0, 0, 0)));
		Background normalBackground = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(00), new Insets(0, 0, 0, 0)));
		
		Check checker = new Check();
	    checker.checkWin(gridText, gridHeight, gridWidth, this);
	    
	    //block that checks rows and highlights them red if incorrect
	    if(showMistakesOn == false) {
	    	showMistakesOn = true;
			
	    	int rowIndex = 0;
	    	for(ArrayList<TextField> row : allRows) {
	    		if(!checker.getRowTrue(rowIndex)) {
	    			for(TextField txt : row) {
	    				txt.setBackground(redBackground);
	    			}
	    		}
	    		rowIndex++;
	    	}
	    	
	    	int columnIndex = 0;
	    	for(ArrayList<TextField> column : allColumns) {
	    		if(!checker.getColumnTrue(columnIndex)) {
	    			for(TextField txt : column) {
	    				txt.setBackground(redBackground);
	    			}
	    		}
	    		columnIndex++;
	    	}
	    	
			int cageIndex = 0;
			for(ArrayList<TextField> cage : allCageText) {
				if(!checker.getCageTrue(cageIndex)) {
					for(TextField txt : cage) {
						txt.setBackground(redBackground);
					}
				}
				cageIndex++;
			}
	    }
	    
	    else if (showMistakesOn == true) {
	    	showMistakesOn = false;
	    	for(TextField txt : gridText) {
	    		txt.setBackground(normalBackground);
	    	}
	    }
	}
	
	public void clear() {
		for(TextField txt : gridText) {
			txt.setText("");
		}
	}
	
	public void setCellText(String lastMove, int lastCell) {
		gridText[lastCell].setText(lastMove);
	}
	
	public void setGameGrid() {
		
		Borders gamegrid = new Borders();
		this.dokuGrid = gamegrid.setBorders(dokuGrid, allCages, allAnchRows, allAnchColumns, gridWidth);
		
	}
	
	public void loadGameFromText(String fileContents) {
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("LoadFromTextGameSave.txt"), "utf-8"));
		    writer.write(fileContents);
		} catch (IOException ex) {
		    // Report
		} finally {
		   try {writer.close();} catch (Exception ex) {/*ignore*/}
		}
		
	}
	
	private void setCageCorner(ArrayList<AnchorPane> cage, String corner) {
		Label cornerText = new Label(corner);
	    cornerText.setFont(cornerFont);
	    cage.get(0).getChildren().add(cornerText);
	    dokuGrid[0].setTopAnchor(cornerText, 0.0);
	    dokuGrid[0].setLeftAnchor(cornerText, 0.0);
	}
	
	
	public TextField[] getGridText() {
		return gridText;
	}
	
	public AnchorPane[] getDokuGrid() {
		return dokuGrid;
	}
	
	public GridPane getActGrid() {
		return actGrid;
	}
	
	public ArrayList<ArrayList<TextField>> getAllCageText() {
		return allCageText;
	}
	
	public ArrayList<Integer> getAllCornerNum() {
		return allCornerNum;
	}
	
	public ArrayList<String> getAllCornerSymbols() {
		return allCornerSymbols;
	}
	
	public int getLines() {
		return lines;
	}
}
