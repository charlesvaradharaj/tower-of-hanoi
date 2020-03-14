package com.charles.towerofhanoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class TowerOfHanoi {

	private Stack<Integer> leftStackPole = new HanoiStack();
	private Stack<Integer> middleStackPole = new HanoiStack();
	private Stack<Integer> rightStackPole = new HanoiStack();

	private int totalNumberOfMoves = 0;
	
	public static void main(String[] args) {
		TowerOfHanoi obj = new TowerOfHanoi();
		int totalDisks = 5; 
		obj.solve(totalDisks);
		System.out.println("Number of Moves : " + obj.totalNumberOfMoves + " Optimal is : " + (int)(Math.pow(2, totalDisks) - 1) );
	}

	private Stack<Integer> getStackPoleAt(Integer position) {
		switch (position) {
		case 0:
			return leftStackPole;
		case 1:
			return middleStackPole;
		case 2:
			return rightStackPole;
		default:
			return null;
		}
	}

	private void solve(int numberOfDisks) {
		Stack<Integer> stack = new HanoiStack();
		for (int i = numberOfDisks; i >= 1; i--) {
			leftStackPole.push(i);
			stack.push(i);
		}
		LogicalDisk disk = new LogicalDisk(stack, 2, 0);
		splitAndMove(disk);
	}

	private void splitAndMove(LogicalDisk disk) {
		if (disk.getStack().size() == 1) {
			moveDisk(disk);
		} else {
			LogicalDisk bottomDisk = new LogicalDisk(disk.copyLastStackElemet(), disk.getTargetPosition(),
					disk.getCurrentPosition());
			LogicalDisk topDisk = new LogicalDisk(disk.copyStackElemetExceptLast(), disk.findTopDiskTargetPosition(),
					disk.getCurrentPosition());

			splitAndMove(topDisk);
			topDisk.setCurrentPosition(topDisk.getTargetPosition());

			splitAndMove(bottomDisk);
			bottomDisk.setCurrentPosition(bottomDisk.getTargetPosition());

			topDisk.setTargetPosition(disk.getTargetPosition());
			splitAndMove(topDisk);
			topDisk.setCurrentPosition(topDisk.getTargetPosition());
		}
	}

	private void moveDisk(LogicalDisk disk) {
		System.out.println(disk.getStack().peek() + " ---->" + disk.getTargetPosition());
		getStackPoleAt(disk.getCurrentPosition()).pop();
		getStackPoleAt(disk.getTargetPosition()).push(disk.getStack().peek());
		totalNumberOfMoves++;
	}

	@SuppressWarnings("serial")
	public class HanoiStack extends Stack<Integer> {
		@Override
		public Integer push(Integer item) {
			if (!this.isEmpty() && this.peek() <= item) {
				throw new RuntimeException("Not allowed to push " + item + " on top of " + this.peek());
			}
			return super.push(item);
		}
	}

	public class LogicalDisk {
		private Stack<Integer> stack = new HanoiStack();
		private Integer targetPosition;
		private Integer currentPosition;

		public LogicalDisk() {
		}

		public LogicalDisk(Stack<Integer> stack, Integer targetPosition, Integer currentPosition) {
			this.stack = stack;
			this.targetPosition = targetPosition;
			this.currentPosition = currentPosition;
		}

		public Stack<Integer> getStack() {
			return stack;
		}

		public void setStack(Stack<Integer> stack) {
			this.stack = stack;
		}

		public Integer getTargetPosition() {
			return targetPosition;
		}

		public void setTargetPosition(Integer targetPosition) {
			this.targetPosition = targetPosition;
		}

		public Integer getCurrentPosition() {
			return currentPosition;
		}

		public void setCurrentPosition(Integer currentPosition) {
			this.currentPosition = currentPosition;
		}

		public Stack<Integer> copyStackElemetExceptLast() {
			Stack<Integer> result = new HanoiStack();
			if (stack.size() == 1) {
				result.push(stack.get(0));
			} else {
				for (int i = 1; i <= stack.size() - 1; i++) {
					result.push(stack.elementAt(i));
				}
			}
			return result;
		}

		public Stack<Integer> copyLastStackElemet() {
			Stack<Integer> result = new HanoiStack();
			result.push(stack.firstElement());
			return result;
		}

		public Integer findTopDiskTargetPosition() {
			List<Integer> position = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
			position.removeAll(Arrays.asList(getCurrentPosition(), getTargetPosition()));
			if (position.size() != 1) {
				throw new RuntimeException("Something Wrong with positions in " + this);
			}
			return position.get(0);
		}

		@Override
		public String toString() {
			return "LogicalDisk [stack=" + stack + ", targetPosition=" + targetPosition + ", currentPosition="
					+ currentPosition + "]";
		}

	}

}
