package jp.azw.kancolleague.data;

import java.util.List;

public class Resource {
	private int fuel;
	private int bullet;
	private int steal;
	private int bauxite;
	
	public Resource(int fuel, int bullet, int steal, int bauxite) {
		this.fuel = fuel;
		this.bullet = bullet;
		this.steal = steal;
		this.bauxite = bauxite;
	}
	
	public Resource(int[] resources) {
		this.fuel = resources[0];
		this.bullet = resources[1];
		this.steal = resources[2];
		this.bauxite = resources[3];
	}
	
	public Resource(List<Integer> resources) {
		this.fuel = resources.get(0);
		this.bullet = resources.get(1);
		this.steal = resources.get(2);
		this.bauxite = resources.get(3);
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getBullet() {
		return bullet;
	}

	public void setBullet(int bullet) {
		this.bullet = bullet;
	}

	public int getSteal() {
		return steal;
	}

	public void setSteal(int steal) {
		this.steal = steal;
	}

	public int getBauxite() {
		return bauxite;
	}

	public void setBauxite(int bauxite) {
		this.bauxite = bauxite;
	}
	
	
}
