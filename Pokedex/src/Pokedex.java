public class Pokedex{
	private int ID;
	private String name;
	private String form;
	private String type1;
	private String type2;
	private int totalstat;
	private int hp;
	private int atk;
	private int def;
	private int spatk;
	private int spdef;
	private int speed;
	private int generation;
	
	public Pokedex(int iD, String name, String form, String type1, String type2, int totalstat, int hp, int atk,
			int def, int spatk, int spdef, int speed, int generation) {
		super();
		ID = iD;
		this.name = name;
		this.form = form;
		this.type1 = type1;
		this.type2 = type2;
		this.totalstat = totalstat;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.spatk = spatk;
		this.spdef = spdef;
		this.speed = speed;
		this.generation = generation;
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getForm() {
		return form;
	}

	public String getType1() {
		return type1;
	}

	public String getType2() {
		return type2;
	}

	public int getTotalstat() {
		return totalstat;
	}

	public int getHp() {
		return hp;
	}

	public int getAtk() {
		return atk;
	}

	public int getDef() {
		return def;
	}

	public int getSpatk() {
		return spatk;
	}

	public int getSpdef() {
		return spdef;
	}

	public int getSpeed() {
		return speed;
	}

	public int getGeneration() {
		return generation;
	}
	
	
	

}