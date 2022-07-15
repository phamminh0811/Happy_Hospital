package classes.window;

import classes.LambdaExpression;

public class Window {
	public static void setTimeout(LambdaExpression e, long millis) {
		new Thread(() -> {
			try {
				Thread.sleep(millis);
				e.expression();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}
}
