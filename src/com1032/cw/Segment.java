package com1032.cw;

import java.security.InvalidParameterException;

public class Segment {
	
	// PID = ID of the process to which the segment belongs
	
	private int SID;
	private int size;
	private int PID;
	private int baseAddress;
	private boolean valid;
	
	private int holeDiff = 0;

	public Segment(int SID, int size, int PID) throws InvalidParameterException {

		if(size < 1){
			throw new InvalidParameterException("Cannot create segment with negative or 0 size.");
		}

		this.SID = SID;
		this.size = size;
		this.PID = PID;
		
		
	}
	
	public String toString() {
		
		return String.format("[P%d.S%d : %d] ", this.PID, this.SID, this.size);
		
	}
	
	public void printSegmentDetails() {
		
		System.out.println("[P" + this.PID + ".S" + this.SID + " : " + this.size + "] ");
		
	}
	
	public void setSID(int SID) {
		
		this.SID = SID;
		
	}
	
	public void setSize(int size) {
		
		this.size = size;
		
	}

	public int getSID() {
		
		return this.SID;
		
	}

	public int getSize() {
		
		return this.size;
		
	}

	public int getPID() {
		return this.PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}
	
	public int getBaseAddress() {
		return baseAddress;
	}

	public void setBaseAddress(int baseAddress) {
		this.baseAddress = baseAddress;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getHoleDiff() {
		return holeDiff;
	}

	public void setHoleDiff(int holeDiff) {
		this.holeDiff = holeDiff;
	}
	
	

}
