package pw.swordfish.poker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class YCombinator {
	private YCombinator() {}

	public static <T> UnaryOperator<T> ycombinator(final UnaryOperator<UnaryOperator<T>> operator) {
		// cannot use lambda because we need 'this' reference...
		return new UnaryOperator<T>() {
			@Override
			public T apply(T t) {
				return operator.apply(this).apply(t);
			}
		};
	}
	public static <T> UnaryOperator<T> memoizedYCombinator(final UnaryOperator<UnaryOperator<T>> operator) {
		final Map<T, T> memoize = new HashMap<>();
		return new UnaryOperator<T>() {
			@Override
			public T apply(T t) {
				if (! memoize.containsKey(t))
					memoize.put(t, operator.apply(this).apply(t));
				return memoize.get(t);
			}
		};
	}
}
