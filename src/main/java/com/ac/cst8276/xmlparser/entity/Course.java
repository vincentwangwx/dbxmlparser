package com.ac.cst8276.xmlparser.entity;

public class Course {
	private int RegNum;
	private String Subj;
	private int crse;
	
	private String sect;
	private String title;
	private String units;
	private String instructor;
	private String days;
	private class Time{
		private String startTime;
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		private String endTime;
	}
	private class Place{
		private String building;
		private String room;
		public String getBuilding() {
			return building;
		}
		public void setBuilding(String building) {
			this.building = building;
		}
		public String getRoom() {
			return room;
		}
		public void setRoom(String room) {
			this.room = room;
		}
	}
	
	public int getRegNum() {
		return RegNum;
	}
	public void setRegNum(int regNum) {
		RegNum = regNum;
	}
	public String getSubj() {
		return Subj;
	}
	public void setSubj(String subj) {
		Subj = subj;
	}
	public int getCrse() {
		return crse;
	}
	public void setCrse(int crse) {
		this.crse = crse;
	}
	public String getSect() {
		return sect;
	}
	public void setSect(String sect) {
		this.sect = sect;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
}
