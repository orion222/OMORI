
public class Player {
	private String playerName;
	private int HP;
	private int playerX;
	private int playerY;
	// attack list
	// item list
	// skin
	public Player(String name, int HP, int posX, int posY) {
		this.playerName = name;
		this.HP = HP;
		this.playerX = posX;
		this.playerY = posY;
	}
	
	public int getHP() {
		return HP;
	}
	public void setHP(int HP) {
		this.HP = HP;
	}

}
