package pw.swordfish.poker;

import com.github.krukow.clj_ds.PersistentSortedSet;
import com.github.krukow.clj_ds.Persistents;
import com.github.krukow.clj_lang.ISeq;
import java.util.Iterator;

class PiggyBank implements Iterable<CoinRoll> {
	private final PersistentSortedSet<CoinRoll> coins;
	private final double total;

	private PiggyBank(PersistentSortedSet<CoinRoll> coins, double total) {
		this.coins = coins;
		this.total = total;
	}

	public static PiggyBank newPiggyBank(CoinRoll... coins) {
		PersistentSortedSet<CoinRoll> set = Persistents.treeSet();
		double total = 0;
		for (CoinRoll coin : coins) {
			set = set.plus(coin);
			total += coin.total();
		}
		return new PiggyBank(set, total);
	}

	public double minCoin() {
		return minCoinRoll().denomination();
	}

	private CoinRoll minCoinRoll() {
		final boolean ascending = true;
		ISeq<CoinRoll> sequence = coins.seq(ascending);
		return coins.seq(ascending).first();
	}

	boolean isEmpty() {
		assert this.total >= 0.0;
		return this.total == 0.0;
	}

	PiggyBank takeMinCoin() {
		CoinRoll minCoin = minCoinRoll();
		if (minCoin.coins() == 1)
			return new PiggyBank(coins.minus(minCoin), this.total - minCoin.denomination());
		return new PiggyBank(coins.minus(minCoin).plus(CoinRoll.takeCoin(minCoin)), this.total - minCoin.denomination());
	}

	@Override
	public Iterator<CoinRoll> iterator() {
		return coins.iterator();
	}
}
