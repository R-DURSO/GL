package process.game;

public class WinCondition {
	private int numberOfPower;
	/**
	 * Petit paragraphe
	 * Le nombre de joueurs va être stocké où ?
	 * Pour comment, je pense à une HashMap<Int,Power> où int est entre 1 et nombreJoueurMax
	 * Donc, créer une classe Jeu qui contiendrait la Map, et une HashMap des joueurs
	 */
	private int temple;
	/**
	 * Pareil Ici, doit-on stocker la valeur de la construction du temple ?
	 * On pourra la récupérer directement depuis Case->Building->ConstructionTime
	 */

	public WinCondition(int numberOfPower, boolean hasArtifact) {
		this.numberOfPower = numberOfPower;
		if (hasArtifact) {
			temple = 0;
		}
	}

	public int howManyCapital() {
		int count = 0;
		
		
		return count;
	}
}
