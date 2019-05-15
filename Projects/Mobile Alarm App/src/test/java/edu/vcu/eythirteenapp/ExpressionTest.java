package edu.vcu.eythirteenapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.vcu.eythirteenapp.math.Expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {
	
	private static Method hasPrecedence;
	private static Method hasFilterPrecedence;
	
	
	@AfterClass
	public static void afterClass() {
		hasPrecedence.setAccessible(false);
		hasFilterPrecedence.setAccessible(false);
		hasPrecedence = null;
		hasFilterPrecedence = null;
	}
	
	
	@BeforeClass
	public static void beforeClass() {
		hasPrecedence = null;
		hasFilterPrecedence = null;
		try {
			hasPrecedence = Expression.class.getDeclaredMethod("hasPrecedence", char.class, char.class);
			hasFilterPrecedence = Expression.class.getDeclaredMethod("hasFilterPrecedence", char.class,
			                                                         char.class);
			hasFilterPrecedence.setAccessible(true);
			hasPrecedence.setAccessible(true);
		} catch (NoSuchMethodException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getInfixExpressionTest1() {
		Expression expression = new Expression("63183///-");
		assertEquals("6-3/(1/(8/3))", expression.getInfixExpression());
	}
	
	
	@Test
	public void getInfixExpressionTest2() {
		Expression expression = new Expression("678-+99/21*-+");
		assertEquals("6+7-8+9/9-2*1", expression.getInfixExpression());
	}
	
	
	@Test
	public void getInfixExpressionTest3() {
		Expression expression = new Expression("47/68++99*8+*");
		assertEquals("(4/7+(6+8))*(9*9+8)", expression.getInfixExpression());
	}
	
	
	@Test
	public void hasFilterPrecedenceTest1() {
		boolean result = false;
		
		try {
			result = (Boolean) hasFilterPrecedence.invoke(new Expression("99+"), '/', '/');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertTrue(result);
	}
	
	
	@Test
	public void hasFilterPrecedenceTest2() {
		boolean result = false;
		
		try {
			result = (Boolean) hasFilterPrecedence.invoke(new Expression("99+"), '*', '+');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertTrue(result);
	}
	
	
	@Test
	public void hasFilterPrecedenceTest3() {
		boolean result = true;
		
		try {
			result = (Boolean) hasFilterPrecedence.invoke(new Expression("99+"), '+', '/');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertFalse(result);
	}
	
	
	@Test
	public void hasFilterPrecedenceTest4() {
		boolean result = false;
		
		try {
			result = (Boolean) hasFilterPrecedence.invoke(new Expression("99+"), '-', '-');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertTrue(result);
	}
	
	
	@Test
	public void hasPrecedenceTest1() {
		boolean result = true;
		
		try {
			result = (Boolean) hasPrecedence.invoke(new Expression("99+"), '+', '-');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertFalse(result);
	}
	
	
	@Test
	public void hasPrecedenceTest2() {
		boolean result = true;
		
		try {
			result = (Boolean) hasPrecedence.invoke(new Expression("99+"), '-', '-');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertFalse(result);
	}
	
	
	@Test
	public void hasPrecedenceTest3() {
		boolean result = false;
		
		try {
			result = (Boolean) hasPrecedence.invoke(new Expression("99+"), '/', '-');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertTrue(result);
	}
	
	
	@Test
	public void hasPrecedenceTest4() {
		boolean result = false;
		
		try {
			result = (Boolean) hasPrecedence.invoke(new Expression("99+"), '*', '+');
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		assertTrue(result);
	}
	
	
	@Test
	public void isSolutionTest1() {
		Expression expression = new Expression("63183///-");
		assertTrue(expression.isSolution(-2));
	}
	
	
	@Test
	public void isSolutionTest2() {
		Expression expression = new Expression("63183///-");
		assertFalse(expression.isSolution(-5));
	}
	
	
	@Test
	public void isSolutionTest3() {
		Expression expression = new Expression("678-+99/21*-+");
		assertTrue(expression.isSolution(4));
	}
	
	
	@Test
	public void isSolutionTest4() {
		Expression expression = new Expression("47/68++99*8+*");
		assertTrue(expression.isSolution(1296.857143));
	}
	
	
	@Test
	public void isSolutionTest5() {
		Expression expression = new Expression("47/68++99*8+*");
		assertTrue(expression.isSolution(1296.9));
	}
	
}
