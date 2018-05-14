package model;

import java.io.Serializable;

public class Tag implements Serializable
{
	private String name;
	private String value;
	
	public Tag(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String newValue)
	{
		value = newValue;
	}
	
	public int compareName(String n)
	{
		return name.compareTo(n);
	}
	
	public int compareValue(String v)
	{
		return value.compareTo(v);
	}
	
	public boolean isEqual(String n, String v)
	{
		return name.compareTo(n) == 0 && value.compareTo(v) == 0;
	}
	
	public String toString()
	{
		return getName() + "~" + getValue();
	}
	
}