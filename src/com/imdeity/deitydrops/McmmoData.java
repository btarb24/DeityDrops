package com.imdeity.deitydrops;

public class McmmoData 
{

	//the types of skills
	private int _taming, _mining, _woodcutting, _repair, _unarmed, _herbalism, _excavation, _archery, _swords, _axes, _acrobatics, _fishing;
	
	//*********Accessors**********
	public McmmoData()
	{
		//default construct assigns all values to 0. this should only be used if player record was missing .. ie almost never.
	}
	
	public McmmoData(int taming, int mining, int woodcutting, int repair, int unarmed, int herbalism, int excavation, int archery, int swords, int axes, int acrobatics, int fishing)
	{
		_taming = taming;
		_mining = mining;
		_woodcutting = woodcutting;
		_repair = repair;
		_unarmed = unarmed;
		_herbalism = herbalism;
		_excavation = excavation;
		_archery = archery;
		_swords = swords;
		_axes = axes;
		_acrobatics = acrobatics;
		_fishing = fishing;
	}

	//*********Mutators**********
	public void setTaming(int val)	{
		_taming = val;
	}
	
	public void setMining(int val)	{
		_mining = val;
	}
	
	public void setWoodcutting(int val)	{
		_woodcutting = val;
	}
	
	public void setRepair(int val)	{
		_repair = val;
	}
	
	public void setUnarmed(int val)	{
		_unarmed = val;
	}
	
	public void setHerbalism(int val)	{
		_herbalism = val;
	}
	
	public void setExcavation(int val)	{
		_excavation = val;
	}
	
	public void setArchery(int val)	{
		_archery = val;
	}
	
	public void setSwords(int val)	{
		_swords = val;
	}
	
	public void setAxes(int val) {
		_axes = val;
	}
	
	public void setAcrobatics(int val) {
		_acrobatics = val;
	}
	
	public void setFishing(int val) {
		_fishing = val;
	}
	
	//*********Accessors**********
	
	public int getTaming()	{
		return _taming;
	}
	
	public int getMining()	{
		return _mining;
	}
	
	public int getWoodcutting()	{
		return _woodcutting;
	}
	
	public int getRepair()	{
		return _repair;
	}
	
	public int getUnarmed()	{
		return _unarmed;
	}
	
	public int getHerbalism()	{
		return _herbalism;
	}
	
	public int getExcavation()	{
		return _excavation;
	}
	
	public int getArchery()	{
		return _archery;
	}
	
	public int getSwords()	{
		return _swords;
	}
	
	public int getAxes() {
		return _axes;
	}
	
	public int getAcrobatics() {
		return _acrobatics;
	}
	
	public int getFishing() {
		return _fishing;
	}
	
	//power level as defined by mcmmo is just the sum of all skills 
	public int getPowerLevel() {
		return _taming + _mining + _woodcutting + _repair + _unarmed + _herbalism + _excavation + _archery + _swords + _axes + _acrobatics + _fishing;
	}
}
