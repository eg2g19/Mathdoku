import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class Game extends Application {
	
	Scene scene;
	
	Grid grid;
	
	BorderPane root;
	public int gridHeight;
	public int gridWidth;
	GridPane actGrid;
	
	String loadText;
	
	AnchorPane[] dokuGrid;
	
	GridPane rightSide;
	GridPane bottomButtons;
	StackPane stack;
    
    Button undoButton;
    Button redoButton;
    Button loadFromFileButton;
    Button clearButton;
    Button loadFromTextButton;
    Button mistakesButton;
    
    String filePath;
    
    Button[] buttons;
    int b = 0;
    
    TextField[] gridText;
    
    ObservableList<RowConstraints> rowCconstraints;   
    ObservableList<ColumnConstraints> colCconstraints;
	
	public static void main(String[] args) {
        launch(args);
    }
	
	public void start(Stage stage) {
		
		 stage.setTitle("Mathdoku!");
		 
	     root = new BorderPane();
	     root.setPadding(new Insets(10, 0, 10, 0));
	     
	     setTitle();
	     
	     addNumberButtons();
	     Move move = new Move();
	     addBottomButtons();
	     setBottomButtonEvents(stage, move);
	     
	     root.setRight(rightSide);
	     root.setBottom(bottomButtons);
	     root.setCenter(actGrid);
	     
	     scene = new Scene(root, 750, 750);
	     
	    	 
	     stage.setScene(scene);
	     stage.show();
	     
	}
	
	private void setGameGrid(Stage stage, Move move) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Open File to Load");
		ExtensionFilter txtFilter = new ExtensionFilter("Text files",
				"*.txt");
		
		
		fileChooser.getExtensionFilters().add(txtFilter);
		
		File file = fileChooser.showOpenDialog(stage);
		this.filePath = file.getAbsolutePath();

		if (file != null && file.exists() && file.canRead()) {
			if(checkInput(filePath)) {
				 grid = new Grid(findGridDimensions(filePath), findGridDimensions(filePath), filePath, move, this);
			     gridText = grid.getGridText();
			     actGrid = grid.getActGrid();
			     dokuGrid = grid.getDokuGrid();
			     setGridColumnConstraints();
				 grid.setGameGrid();
				 root.setCenter(actGrid);
			} else {
				
				Alert fileWrong = new Alert(AlertType.WARNING, "The file does not contain valid cages! Please select another file");
				fileWrong.setTitle("Incorrect File");				
				fileWrong.show();
			} 
		}
	}
	
	private void setGameGridFromText(Move move) {
		TextAreaInputDialog dialog = new TextAreaInputDialog();
		 
		 Writer writer = null;
        File file = new File("LoadFromTextGameSave.txt");
		 setFilePath(file);
		 Optional result = dialog.showAndWait();

           if (result.isPresent()) {
               String loadText = (String) result.get();
               
                String path = file.getAbsolutePath();
               
	       		try {
	       		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
	       		    writer.write(loadText);
	       		} catch (IOException ex) {
	       		    // Report
	       		} finally {
	       		   try {writer.close();} catch (Exception ex) {/*ignore*/}
	       		}
	       		
	       		if (file != null && file.exists() && file.canRead()) {
	       			if(checkInput(file.getAbsolutePath())) {
	       				 grid = new Grid(findGridDimensions(path), findGridDimensions(path),
	       						 path, move, this);
	       			     gridText = grid.getGridText();
	       			     actGrid = grid.getActGrid();
	       			     dokuGrid = grid.getDokuGrid();
	       			     setGridColumnConstraints();
	       				 grid.setGameGrid();
	       				 root.setCenter(actGrid);
	       			} else {
	       				Alert fileWrong = new Alert(AlertType.WARNING, "The file states that a cell(s) is part of more than one cage,"
	       						+ " please select a different file.");
	       				fileWrong.setTitle("Incorrect File");				
	       				fileWrong.show();
	       			} 
	       		} else {
	       			System.out.println("file = null or does not exit ot cannot read");
	       		}
           }
	}
	
	private Boolean checkInput(String filePath) {
		
		GameSaveReader reader = new GameSaveReader(filePath);
		int lines = reader.countLines();
		
		GameSaveReader lineReader = new GameSaveReader(filePath);
		
		ArrayList<Integer> allCageMembers = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> allCages = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < lines; i ++) {
			
			ArrayList<Integer> cage = new ArrayList<Integer>();
			
			String line = lineReader.getLine();
			String[] splitLine = line.split("\\s+");
			
			String[] cells = splitLine[1].split(",");
			for(String cell : cells) {
				int q = Integer.parseInt(cell) - 1;
				cage.add(q);
				allCageMembers.add(q);
			}
			allCages.add(cage);
		}
		
		for(int compareToIndex = 0; compareToIndex < allCageMembers.size(); compareToIndex++) {
			for(int a = compareToIndex + 1; a < allCageMembers.size(); a++) {
				if(allCageMembers.get(compareToIndex) == allCageMembers.get(a)) {
					return false;
				} 
			}
		}
		
		for(ArrayList<Integer> cage : allCages) {
			if(cage.size() < 1) {
				for(Integer cell : cage) {
					if((cage.contains(cell + 1)) || (cage.contains(cell - 1)) || 
							(cage.contains(cell + findGridDimensions(filePath))) || 
								(cage.contains(cell + findGridDimensions(filePath)))) {
						return false;
					}
				}
			}
		}
		return true;
		
	}
	
	// sets the title of the game
	private void setTitle() {
	     stack = new StackPane();
	     root.setTop(stack);
	     Label mathdoku = new Label("MATHDOKU");
	     Font titleFont = Font.font("Calibri", FontWeight.EXTRA_BOLD, 25);
	     mathdoku.setFont(titleFont);
	     mathdoku.setScaleX(1.5);
	     mathdoku.setScaleY(1.5);
	     mathdoku.setAlignment(Pos.TOP_CENTER);
	     stack.setAlignment(Pos.TOP_CENTER);
	     stack.getChildren().add(mathdoku);
	}
	
	// sets the column restraints of the central playing grid
	private void setGridColumnConstraints() {
		colCconstraints = actGrid.getColumnConstraints();
        colCconstraints.clear();
        for(int col = 0; col < findGridDimensions(filePath); col++){
             ColumnConstraints c = new ColumnConstraints();
             c.setHalignment(HPos.CENTER);
             c.setHgrow(Priority.ALWAYS);
             colCconstraints.add(c);
        }

        //Construct row constraints to resize vertically
        rowCconstraints = actGrid.getRowConstraints();
        rowCconstraints.clear();
        for(int row = 0; row < findGridDimensions(filePath); row++){
             RowConstraints c = new RowConstraints();
             c.setValignment(VPos.CENTER);
             c.setVgrow(Priority.ALWAYS);
             rowCconstraints.add(c);
        }
	}
	
	// adds the number buttons 
	private void addNumberButtons() {
		
		 Button addOne = new Button("1");
	     Button addTwo = new Button("2");
	     Button addThree = new Button("3");
	     Button addFour = new Button("4");
	     Button addFive = new Button("5");
	     Button addSix = new Button("6");
	     Button addSeven = new Button("7");
	     Button addEight = new Button("8");
	     
	     buttons = new Button[8];
	     buttons[0] = addOne;
	     buttons[1] = addTwo;
	     buttons[2] = addThree;
	     buttons[3] = addFour;
	     buttons[4] = addFive;
	     buttons[5] = addSix;
	     buttons[6] = addSeven;
	     buttons[7] = addEight;
	     
	     rightSide = new GridPane();
	     rightSide.setPadding(new Insets(10, 0, 10, 0));
	     rightSide.add(addOne, 1, 1);
	     rightSide.add(addTwo, 1, 2);
	     rightSide.add(addThree, 1, 3);
	     rightSide.add(addFour, 1, 4);
	     rightSide.add(addFive, 1, 5);
	     rightSide.add(addSix, 1, 6);
	     rightSide.add(addSeven, 1, 7);
	     rightSide.add(addEight, 1, 8);
	     
	     setNumberButtonEvents();
	}
	
	// adds the buttons to the bottom part of the borderPANE
	private void addBottomButtons() {
		// NEED TO ALLOW BUTTON SPACING TO EXPAND
	     bottomButtons = new GridPane();
	 		colCconstraints = bottomButtons.getColumnConstraints();
	        colCconstraints.clear();
	         for(int col = 0; col < 6; col++){
	              ColumnConstraints c = new ColumnConstraints();
	              c.setHalignment(HPos.CENTER);
	              c.setHgrow(Priority.ALWAYS);
	              colCconstraints.add(c);
	         }

	     ColumnConstraints col1 = new ColumnConstraints();
	     col1.setMaxWidth(Double.MAX_VALUE);
	     ColumnConstraints col2 = new ColumnConstraints();
	     col1.setMaxWidth(Double.MAX_VALUE);
	     ColumnConstraints col3 = new ColumnConstraints();
	     col1.setMaxWidth(Double.MAX_VALUE);
	     bottomButtons.getColumnConstraints().addAll(col1, col2, col3);
	     StackPane undoPane = new StackPane();
	     undoButton = new Button("UNDO");
	     undoPane.getChildren().add(undoButton);
	     undoButton.setDisable(true);
	     StackPane redoPane = new StackPane();
	     redoButton = new Button("REDO");
	     redoButton.setDisable(true);
	     redoPane.getChildren().add(redoButton);
	     StackPane clearPane = new StackPane();
	     clearButton = new Button("CLEAR");
	     clearButton.setDisable(true);
	     clearPane.getChildren().add(clearButton);
	     StackPane loadFromFilePane = new StackPane();
	     loadFromFileButton = new Button("LOAD FROM FILE");
	     loadFromFilePane.getChildren().add(loadFromFileButton);
	     StackPane loadFromTextPane = new StackPane();
	     loadFromTextButton = new Button("LOAD FROM TEXT");
	     loadFromTextPane.getChildren().add(loadFromTextButton);
	     StackPane mistakesPane = new StackPane();
	     mistakesButton = new Button("SHOW MISTAKES");
	     mistakesPane.getChildren().add(mistakesButton);
	     
	     bottomButtons.add(undoPane, 1, 1);
	     bottomButtons.add(redoPane, 2, 1);
	     bottomButtons.add(clearPane, 3, 1);
	     bottomButtons.add(loadFromFilePane, 1, 2);
	     bottomButtons.add(loadFromTextPane, 2, 2);
	     bottomButtons.add(mistakesPane, 3, 2);     
	}
	
	private void setBottomButtonEvents(Stage stage, Move move) {
		Button[] bottomButtons = new Button[6];
		
		bottomButtons[0] = undoButton;
		bottomButtons[1] = redoButton;
		bottomButtons[2] = clearButton;
		bottomButtons[3] = loadFromFileButton;
		bottomButtons[4] = loadFromTextButton;
		bottomButtons[5] = mistakesButton;
		
		mistakesButton.setOnAction(evt -> {
			grid.showMistakes();			
		});
		
		clearButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Are you sure you want to clear the game?");
				
				alert.setTitle("Clear game confirmation");
				alert.setHeaderText("Are you sure?");
				
				Optional<ButtonType> result = alert.showAndWait();
				
				
				if (result.isPresent() && result.get() == ButtonType.OK) {
					grid.clear();
				}
			}

		});
		
		loadFromTextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {				
				 setGameGridFromText(move);
			}
		});
		
		undoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {				
				grid = move.undo(grid);
				getRedoButton().setDisable(false);
			}
		});
		
		loadFromFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {				
				setGameGrid(stage, move);
			}
		});
		
		redoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {				
				grid = move.redo(grid);
			}
		});
		
	}
	public void setFilePath(File file) {
		this.filePath = file.getAbsolutePath();
	}
	
	private void setNumberButtonEvents() {
		for(Button button : buttons) {
	    	 button.setFocusTraversable(false); // prevent buttons from stealing focus
		        button.setOnAction(evt -> {
		            if (scene.getFocusOwner() instanceof TextInputControl) {
		            	TextField xText = new TextField();
		            	xText = (TextField) scene.getFocusOwner();
		            	int x;
		            	if (button.getText().equals("1")) {
		            		x = 1;
		            	}
		            	else if (button.getText().equals("2")) {
		            		x = 2;
		           		}
		            	else if (button.getText().equals("3")) {
		            		x = 3;
		           		}
		            	else if (button.getText().equals("4")) {
		            		x = 4;
		           		}
		            	else if (button.getText().equals("5")) {
		            		x = 5;
		            	}
		            	else if (button.getText().equals("6")) {
		            		x = 6;
		            	}
		            	else if (button.getText().equals("7")) {
		            		x = 7;
		            	}
		            	else{
		            		x = 8;
		            	}
						xText.setText(Integer.toString(x));
		            }
		        });
	     }
	}
	
	public int findGridDimensions(String filePath) {
		
		ArrayList<String> allCells = new ArrayList<String>();
		GameSaveReader lineCounter = new GameSaveReader(filePath);
		int lines = lineCounter.countLines();
		
		GameSaveReader lineReader = new GameSaveReader(filePath);
		for(int i = 0; i < lines; i++) {
			String line = lineReader.getLine();
			String[] splitLine = line.split("\\s+");
			String[] cells = splitLine[1].split(",");
			for(String cell : cells) {
				allCells.add(cell);
			}
		}
		
		if(allCells.size() == 4) {
			return 2;
		}
		
		if(allCells.size() == 9) {
			return 3;
		}
		
		if(allCells.size() == 16) {
			return 4;
		}
		
		if(allCells.size() == 25) {
			return 5;
		}
		
		if(allCells.size() == 36) {
			return 6;
		}
		
		if(allCells.size() == 49) {
			return 7;
		}
		
		else {
			return 8;
		}
	}
	
	public Button getRedoButton() {
		return redoButton;
	}
	public Button getUndoButton() {
		return undoButton;
	}
	public Button getClearButton() {
		return clearButton;
	}
}
		
	














