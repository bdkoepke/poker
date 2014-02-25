package pw.swordfish.poker;

import java.util.Arrays;
import java.util.function.BiFunction;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class App {

	private static final int NO_SOLUTION = -1;

	private static int min(int first, int second) {
		if (first == NO_SOLUTION)
			return second;
		if (second == NO_SOLUTION)
			return first;
		return Math.min(first, second);
	}

	private static BiFunction<PiggyBank, Double, Integer> minCoins(BiFunction<PiggyBank, Double, Integer> operator) {
		return (bank, remaining) -> {
			assert remaining >= 0;
			if (remaining == 0)
				return 0;
			if (bank.isEmpty())
				return NO_SOLUTION;
			double minCoin = bank.minCoin();
			if (remaining < minCoin)
				return NO_SOLUTION;
			PiggyBank newBank = bank.takeMinCoin();
			return min(operator.apply(newBank, remaining - minCoin), operator.apply(newBank, remaining));
		};
	}

	public static BiFunction<PiggyBank, Double, Integer> memoizedYCombinator(final UnaryOperator<BiFunction<PiggyBank, Double, Integer>> operator) {
		final Graph<CoinRoll, Integer> graph = new Graph<>();
		return new BiFunction<PiggyBank, Double, Integer>() {
			@Override
			public Integer apply(PiggyBank t, Double u) {
				Optional<Integer> result = graph.get(t);
				if (! result.isPresent()) {
					result = Optional.of(operator.apply(this).apply(t, u));
					graph.addPath(t, result.get());
				}
				return result.get();
			}
		};
	}

	public static void main(String... args) {
		//runFibonacci(BigInteger.valueOf(1000), System.out);
		PiggyBank bank = PiggyBank.newPiggyBank(
				CoinRoll.of(2.00, 5),
				CoinRoll.of(1.00, 4),
				CoinRoll.of(0.41, 5),
				CoinRoll.of(0.13, 4),
				CoinRoll.of(0.11, 4),
				CoinRoll.of(0.07, 2)
		);

		BiFunction<PiggyBank, Double, Integer> minCoins = memoizedYCombinator(t -> minCoins(t));
		System.out.println(minCoins.apply(bank, 17.5));
	}
}
