package com1032.cw;
import java.util.ArrayList;

public class SegmentTable {
	
	private ArrayList<Segment> table;
	
	public SegmentTable() {
		
		this.table = new ArrayList<Segment>();
		
	}

	public void add(Segment segment) {

		this.table.add(segment);
		
	}
	
	public ArrayList<Segment> getSegmentTable(){
		
		return table;
		
	}
	
	public String toString() {
		
		StringBuilder S = new StringBuilder(256);
		
		S.append(" SID | base | limit | validBit \n");
		
		for(Segment x : table) {
			
			S.append(String.format(" %d | %d | %d | %s\n", x.getSID(), x.getBaseAddress(), x.getSize(), x.isValid()));
			
		}
		
		return S.toString();
		
	}

}
