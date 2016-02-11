package jp.azw.kancolleague.util;

import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * 資源を表すのに用いる。
 * 
 * @author sayama
 *
 */
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Resource) {
			Resource res = (Resource) obj;
			return getFuel() == res.getFuel() && getBullet() == res.getBullet()&& getSteal() == res.getSteal() && getBauxite() == res.getBauxite();
		} else {
			return false;
		}
	}

	@Override
	protected Resource clone() {
		return new Resource(getFuel(), getBullet(), getSteal(), getBauxite());
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

	public static Resource fromJsonArray(JsonArray json) {
		Iterator<JsonElement> it = json.iterator();
		return new Resource(it.next().getAsInt(), it.next().getAsInt(), it.next().getAsInt(), it.next().getAsInt());
	}

	public static Resource fromIterable(Iterable<Integer> it) {
		int i = 0, fuel = 0, bullet = 0, steal = 0, bauxite = 0;
		for (Integer integer : it) {
			switch (i) {
			case 0:
				fuel = integer;
				break;
			case 1:
				bullet = integer;
				break;
			case 2:
				steal = integer;
				break;
			case 3:
				bauxite = integer;
				break;
			default:
				break;
			}
			i++;
		}
		return new Resource(fuel, bullet, steal, bauxite);
	}
}
