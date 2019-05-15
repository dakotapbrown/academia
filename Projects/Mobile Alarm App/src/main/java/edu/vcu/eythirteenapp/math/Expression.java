package edu.vcu.eythirteenapp.math;

import android.support.annotation.NonNull;

import java.util.Stack;

public class Expression {
	private static final float TOLERANCE = 0.1f;
	private final double mSolution;
	@SuppressWarnings("CanBeFinal")
	private String mExpression;
	
	
	public Expression(@NonNull String expression) {
		mExpression = expression;
		mSolution = evaluateExpression();
	}
	
	
	public String getInfixExpression() {
		Stack<String> infixExpression = new Stack<>();
		Stack<Character> compositeOperators = new Stack<>();
		
		for (char ch : mExpression.toCharArray()) {
			if (Character.isDigit(ch)) {
				infixExpression.push(String.valueOf(ch));
				compositeOperators.push(null);
			} else {
				String operand2 = infixExpression.pop();
				String operand1 = infixExpression.pop();
				Character compositeOperator2 = compositeOperators.pop();
				Character compositeOperator1 = compositeOperators.pop();
				
				if (compositeOperator1 != null && hasPrecedence(ch, compositeOperator1)) {
					operand1 = "(" + operand1 + ")";
					compositeOperators.push(ch);
				}
				if (compositeOperator2 != null && hasFilterPrecedence(ch, compositeOperator2)) {
					operand2 = "(" + operand2 + ")";
					infixExpression.push(operand1 + ch + operand2);
					compositeOperators.push(ch);
					continue;
				}
				
				infixExpression.push(operand1 + ch + operand2);
				compositeOperators.push(ch);
			}
		}
		
		return infixExpression.pop();
	}
	
	
// --Commented out by Inspection START (12/6/2018 12:22 AM):
//	public String getPostfixExpression() {
//		return mExpression;
//	}
// --Commented out by Inspection STOP (12/6/2018 12:22 AM)

	
	public boolean isSolution(double answer) {
		return (float) mSolution > answer ? (mSolution - answer) < TOLERANCE
				       : (answer - mSolution) < TOLERANCE;
	}
	
	
	private static double applyOperator(char operator, double operand1, double operand2) {
		switch (operator) {
			case '+':
				return operand1 + operand2;
			case '-':
				return operand1 - operand2;
			case '*':
				return operand1 * operand2;
			case '/':
				return operand1 / operand2;
		}
		
		return -1;
	}
	
	
	private double evaluateExpression() {
		Stack<Double> evaluation = new Stack<>();
		
		for (char ch : mExpression.toCharArray()) {
			if (Character.isDigit(ch)) {
				evaluation.push((double) (ch - '0'));
			} else {
				double operand2 = evaluation.pop();
				double operand1 = evaluation.pop();
				
				evaluation.push(applyOperator(ch, operand1, operand2));
			}
		}
		
		return evaluation.pop();
	}
	
	
	private boolean hasFilterPrecedence(char op, char other) {
		return hasPrecedence(op, other) || (op == other && op != '*');
	}
	
	
	private boolean hasPrecedence(char op, char other) {
		if (op == other) { return false; }
		
		switch (op) {
			case '/':
			case '*':
				return other == '+' || other == '-';
			default:
				return false;
		}
	}
}
