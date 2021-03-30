import java.util.Stack;

public class Move {
	
	Stack<String> moveStack;
	Stack<Integer> cellStack;
	
	Stack<String> redoMoveStack;
	Stack<Integer> redoCellStack;
	
	Move() {
		moveStack = new Stack<String>();
		cellStack = new Stack<Integer>();
		
		redoMoveStack = new Stack<String>();
		redoCellStack = new Stack<Integer>();
	}
	
	public void addMove(String move, int cell) {
		moveStack.push(move);
		cellStack.push(cell);
	} 
	
	public Grid undo(Grid grid) {
		if(!moveStack.isEmpty()) {
			grid.getGridText()[cellStack.pop()].setText(moveStack.pop());	
			redoMoveStack.push(moveStack.pop());
			redoCellStack.push(cellStack.pop());
			return grid;
		} else {
			return grid;
		}
	}
	
	public Grid redo(Grid grid) {
		if(!redoMoveStack.isEmpty()) {
			grid.getGridText()[redoCellStack.pop()].setText(redoMoveStack.pop());
			return grid;
		} else {
			return grid;
		}
	} 
	
	public Boolean isUndoAble() {
		if(!moveStack.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isRedoAble() {
		if(!redoMoveStack.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}




