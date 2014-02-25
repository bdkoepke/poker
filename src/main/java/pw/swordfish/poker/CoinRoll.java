package pw.swordfish.poker;

/**
 * @author brandon
 */
class CoinRoll implements Comparable<CoinRoll> {

	public static CoinRoll of(double denomination, int coins) {
		return new CoinRoll(denomination, coins);
	}

	public static CoinRoll of(double denomination) {
		return of(denomination, 0);
	}

	public static CoinRoll takeCoin(CoinRoll roll) {
		if (roll.isEmpty())
			throw new IllegalStateException("No coins to take");
		return of(roll.denomination(), roll.coins() - 1);
	}

	private int coins;
	private final double denomination;

	private CoinRoll(double denomination, int coins) {
		if (denomination <= 0) {
			throw new IllegalArgumentException("A coin roll cannot contain coins with negative or zero value");
		}
		if (coins < 0) {
			throw new IllegalArgumentException("A coin roll cannot have negative coins");
		}
		this.denomination = denomination;
		this.coins = coins;
	}

	public double denomination() {
		return this.denomination;
	}

	public int coins() {
		return this.coins;
	}

	public double total() {
		return this.denomination * this.coins;
	}

	public boolean isEmpty() {
		return this.coins == 0;
	}

	public double takeCoin() {
		if (isEmpty())
			throw new IllegalStateException("No coins to take");
		this.coins--;
		return this.denomination;
	}

	@Override
	public String toString() {
		return String.format("Chip roll contains %d coins; $%f value", coins, denomination);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CoinRoll other = (CoinRoll) obj;
		if (coins != other.coins())
			return false;
		return Double.doubleToLongBits(denomination) == Double.doubleToLongBits(other.denomination());
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + coins;
		hash = 37 * hash + (int) (Double.doubleToLongBits(denomination) ^ (Double.doubleToLongBits(denomination) >>> 32));
		return hash;
	}

	@Override
	public int compareTo(CoinRoll o) {
		return (int) (denomination - o.denomination());
	}
}
