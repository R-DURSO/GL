package process.game;

public class WinCondition {
	private int numberOfPower;
	/**
	 * Petit paragraphe
	 * Le nombre de joueurs va �tre stock� o� ?
	 * Pour comment, je pense � une HashMap<Int,Power> o� int est entre 1 et nombreJoueurMax
	 * Donc, cr�er une classe Jeu qui contiendrait la Map, et une HashMap des joueurs
	 */
	private int temple;
	/**
	 * Pareil Ici, doit-on stocker la valeur de la construction du temple ?
	 * On pourra la r�cup�rer directement depuis Case->Building->ConstructionTime
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
