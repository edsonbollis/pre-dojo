package entities;

import java.util.Date;

public class Kill {

	private User killer;
	private User killed;
	private Date date;
	private Weapon weapon;
	
	public Kill(){
		super();
	}
	
	

	public Kill(User killer, User killed, Date date, Weapon weapon) {
		super();
		this.killer = killer;
		this.killed = killed;
		this.date = date;
		this.weapon = weapon;
	}



	public User getKiller() {
		return killer;
	}

	public void setKiller(User killer) {
		this.killer = killer;
	}

	public User getKilled() {
		return killed;
	}

	public void setKilled(User killed) {
		this.killed = killed;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public String toString() {
		return "Kill [killer=" + killer + ", killed=" + killed + ", date=" + date + ", weapon=" + weapon + "]";
	}

}
