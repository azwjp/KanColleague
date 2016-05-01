package jp.azw.kancolleague.util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult.Result;

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
	
	public Resource() {
	}

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
			return getFuel() == res.getFuel() && getBullet() == res.getBullet() && getSteal() == res.getSteal()
					&& getBauxite() == res.getBauxite();
		} else {
			return false;
		}
	}

	@Override
	protected Resource clone() {
		return new Resource(getFuel(), getBullet(), getSteal(), getBauxite());
	}
	
	/**
	 * この <code>Resource</code> の値を増加させる。
	 * @see #sum(Resource)
	 * @see #subtract(Resource)
	 * @param adding 増加分
	 * @return this
	 */
	public Resource add(Resource adding) {
		fuel += adding.getFuel();
		bullet += adding.getBullet();
		steal += adding.getSteal();
		bauxite += adding.getBauxite();
		return this;
	}
	
	/**
	 * この <code>Resource</code> の値を増加させる。
	 * @see #sum(Resource)
	 * @see #subtract(Resource)
	 * @param adding 増加分
	 * @return this
	 */
	public Resource addAll(Collection<? extends Resource> addings) {
		addings.parallelStream().forEach(r -> add(r));
		return this;
	}
	
	/**
	 * この <code>Resource</code> の値を減少させる。
	 * @see #add(Resource)
	 * @see #sum(Resource)
	 * @param subtracting 減少分
	 * @return this
	 */
	public Resource subtract(Resource subtracting) {
		fuel -= subtracting.getFuel();
		bullet -= subtracting.getBullet();
		steal -= subtracting.getSteal();
		bauxite -= subtracting.getBauxite();
		return this;
	}
	
	/**
	 * このインスタンスのもつ資源の値と、引数で与えられた資源の値を足し合わせた値を持つインスタンスを新たに作って返す。
	 * このインスタンスと与えられたインスタンスは変化しない。 
	 * @param other
	 * @return
	 * @see #add(Resource)
	 */
	public Resource sum(Resource other) {
		return new Resource().add(this).add(other);
	}

	/* 以下 getter, setter */
	
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

	public static Resource fromJsonArray(JsonElement json) {
		Iterator<JsonElement> it = json.getAsJsonArray().iterator();
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
	
	public static class ResourceDeserializer implements JsonDeserializer<Resource> {

	    @Override
	    public Resource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	            throws JsonParseException {
	        return Resource.fromJsonArray(json);
	    }
	}
}
