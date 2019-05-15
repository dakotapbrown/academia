package edu.vcu.eythirteenapp.math;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public final class ExpressionRandomizer {
	
	private ExpressionRandomizer() {}
	
	
	public static Expression newExpression(int difficulty) {
		int numberOfOperands = difficulty + 2;
		StringBuilder expression = new StringBuilder();
		Random random = new Random();
		Queue<Integer> operands = new ArrayDeque<>();
		Queue<Character> operators = new ArrayDeque<>();
		
		for (int i = 0; i < numberOfOperands; i++) {
			int singleDigitNum = Math.abs(random.nextInt() % 9);
			int opCode = Math.abs(random.nextInt() % numberOfOperands);
			char operator = newOperator(opCode);
			
			// increment singleDigitNum to eliminate 0 as a possible operand
			operands.offer(++singleDigitNum);
			if (i < numberOfOperands - 1) {
				operators.offer(operator);
			}
		}
		
		// add at least one operand to expression
		expression.append(operands.poll());
		while (!operands.isEmpty() || !operators.isEmpty()) {
			int rand = random.nextInt() % 2;
			if (rand == 0) {
				// add operand, if not empty
				if (!operands.isEmpty()) {
					expression.append(operands.poll());
				}
			} else {
				// add operator, if not empty; but not if operands.size() <= operators.size()
				if (!operators.isEmpty() && operators.size() > operands.size()) {
					expression.append(operators.poll());
				} else {
					expression.append(operands.poll());
				}
			}
		}
		
		return new Expression(expression.toString());
	}
	
	
	private static Character newOperator(int opCode) {
		switch (opCode) {
			case 1:
				return '-';
			case 2:
				return '*';
			case 3:
				return '/';
			default:
				return '+';
		}
	}
}
