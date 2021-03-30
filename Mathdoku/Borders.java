import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;

public class Borders {
	
	public AnchorPane[] setBorders(AnchorPane[] dokuGrid, ArrayList<ArrayList<AnchorPane>> allCages, ArrayList<ArrayList<AnchorPane>> allRows,
			ArrayList<ArrayList<AnchorPane>> allColumns, int gridDimensions) {
		
		
		for(ArrayList<AnchorPane> cage : allCages) {
			for(int i = 0; i < gridDimensions * gridDimensions; i++) {
				if(cage.contains(dokuGrid[i])) {
					if(allRows.get(0).contains(dokuGrid[i])) {
						if(allColumns.get(0).contains(dokuGrid[i])) {
							if(cage.contains(dokuGrid[i+1])) {
								if(cage.contains(dokuGrid[i+gridDimensions])) {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 1px 5px");
								} else {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 5px 5px");
								}
							} else {
								if(cage.contains(dokuGrid[i+gridDimensions])) {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 1px 5px");
								} else {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 5px");
								}
							}
						} else {
							if(cage.contains(dokuGrid[i-1])) {
								if(cage.contains(dokuGrid[i+1])) {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 1px 1px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 5px 1px");
									}
								} else {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 1px");
								}
							} else {
								if(cage.contains(dokuGrid[i+1])) {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 1px 5px");
									}
								} else {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 5px");
								}
							}
						}
					} else if(allRows.get(allRows.size() - 1).contains(dokuGrid[i])) {
						if(allColumns.get(allColumns.size() - 1).contains(dokuGrid[i])) {
							if(cage.contains(dokuGrid[i-1])) {
								if(cage.contains(dokuGrid[i-gridDimensions])) {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 5px 5px 1px");
								} else {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 1px");
								}
							} else {
								if(cage.contains(dokuGrid[i-gridDimensions])) {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 5px 5px 5px");
								} else {
									dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 5px");
								}
							}
						}
					} else {
						if(cage.contains(dokuGrid[i-1])) {
							if(cage.contains(dokuGrid[i-gridDimensions])) {
								if(cage.contains(dokuGrid[i+1])) {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 1px 1px 1px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 1px 5px 1px");
									}
								} else {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 5px 1px 1px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 5px 5px 1px");
									}
								}
							} else {
								if(cage.contains(dokuGrid[i+1])) {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 1px 1px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 5px 1px");
									}
								} else {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 1px 1px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 1px");
									}
								}
							}
						} else {
							if(cage.contains(dokuGrid[i-gridDimensions])) {
								if(cage.contains(dokuGrid[i+1])) {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 1px 1px 5px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 1px 5px 5px");
									}
								} else {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 5px 1px 5px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 1px 5px 5px 5px");
									}
								}
							} else {
								if(cage.contains(dokuGrid[i+1])) {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 1px 5px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 1px 5px 5px");
									}
								} else {
									if(cage.contains(dokuGrid[i+gridDimensions])) {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 1px 5px");
									} else {
										dokuGrid[i].setStyle("-fx-border-color: black; -fx-border-width: 5px 5px 5px 5px");
									}
								}
							}
						}
					}
				}
			}
		}
		return dokuGrid;
	}
}
