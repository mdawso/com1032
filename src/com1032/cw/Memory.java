package com1032.cw;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Memory {
	
	private int os_size;
	private int user_size;
	private ArrayList<Segment> allocatedSegments;

	private int remaining_size;
	
	public Memory(int os_size, int user_size) throws InvalidParameterException {

		if(os_size < 0 || user_size < 0){ // if either parameters are negative throw an exception
			throw new InvalidParameterException("Cannot create memory with negative size");
		}

		this.os_size = os_size;
		this.user_size = user_size;
		this.allocatedSegments = new ArrayList<>();

		this.remaining_size = user_size;

		System.out.printf("New memory created with %d OS bytes and %d User bytes\n", os_size, user_size);
		
	}
	
	public void printMemoryState() {
		
		StringBuilder S = new StringBuilder(256);
		
		//memory state and OS size text
		S.append("[ Memory State ] \n");
		S.append(String.format("[ OS : %d ] ", this.os_size));
		
		//each segment and hole added
		
		for(int i = 0; i < this.allocatedSegments.toArray().length; i++) {
			
			Segment x  = allocatedSegments.get(i);
			S.append(String.format("[ P%d.S%d : %d ] ", x.getPID(), x.getSID(), x.getSize()));
			if(i == this.allocatedSegments.toArray().length - 1) { // in this case we know the current segment is the last, so it should not have its own hole text
				int holeSum = x.getHoleDiff() + this.remaining_size;
				S.append(String.format("[ HOLE : %d ] ", holeSum));
			} else if(x.getHoleDiff() > 0) { // in the case that the hole diff is not 0, the segment will get a hole text appended
				S.append(String.format("[ HOLE : %d ] ", x.getHoleDiff()));
			}
			
		}	
		
		if(this.allocatedSegments.toArray().length == 0) { // in this case there are no segments therefore it can just print the total size of user memory
			
			S.append(String.format("[ HOLE : %d ] ", this.remaining_size));
			
		}
		
		S.append("\n");
		
		System.out.println(S);
		
	}

	// allocates entire process to memory
	public int allocate(Process process) {
		
		// check size of new process
		int processSize = 0;
		
		for(Object currentSegment : process.getSegmentTable().getSegmentTable().toArray()) {
			
			Segment segment = (Segment)currentSegment;
			processSize = processSize + segment.getSize();
			
		}

		int addressCounter = this.os_size + user_size - remaining_size;
		
		// if sufficient space - allocate segments + base addresses + valid bits
		if(processSize < remaining_size) {
			
			System.out.println("----------------------------------------");
			System.out.println("Allocating P" + process.getPID() + "\n");
			System.out.println("Segment table before: ");
			System.out.println(process.getSegmentTable().toString());
			
			for(Object currentSegment : process.getSegmentTable().getSegmentTable().toArray()) {
				
				Segment segment = (Segment)currentSegment;

				if(!segment.isValid()) {
				
					segment.setValid(true);
				
					segment.setBaseAddress(addressCounter);
					addressCounter = addressCounter + segment.getSize();
				
					allocatedSegments.add(segment);
				
					remaining_size = remaining_size - segment.getSize();
				}
			}
			
//			System.out.println("P" + process.getPID() + " allocated. Allocated " + segmentCounter + " segments.\n");
			
			System.out.println("Segment table after: ");
			System.out.println(process.getSegmentTable().toString());
			System.out.println("----------------------------------------\n");

			recalculateSizes();
			
			return 1;
			
		} else {
			System.out.println("Not enough space, process not allocated.");
			return -1;
			}
		
		
	}
	// allocates specific segment to memory
	public int allocate(Process process, int segmentNumber) {
		
	// check size of new segment
		int processSize = 0;
				
			for(Object currentSegment : process.getSegmentTable().getSegmentTable().toArray()) {
				
				Segment segment = (Segment)currentSegment;
				
				if(segment.getSID() == segmentNumber) {
					
					processSize = processSize + segment.getSize();
					
				}
		
			}

			int addressCounter = this.os_size + user_size - remaining_size;
			
			if(processSize < remaining_size) {
				
				System.out.println("----------------------------------------");
				System.out.println("Allocating P" + process.getPID() +  ".S" + segmentNumber +"\n");
				System.out.println("Segment table before: ");
				System.out.println(process.getSegmentTable().toString());
				
					
				Segment segment = process.getSegmentTable().getSegmentTable().get(segmentNumber);
				
				if(!segment.isValid()) {
				
					segment.setValid(true);
					
					segment.setBaseAddress(addressCounter);
					
					allocatedSegments.add(segment);
					
					remaining_size = remaining_size - segment.getSize();
				}	
				
				
//				System.out.println("P" + process.getPID() + " allocated. Allocated " + segmentCounter + " segments.\n");
				
				System.out.println("Segment table after: ");
				System.out.println(process.getSegmentTable().toString());
				System.out.println("----------------------------------------\n");

				recalculateSizes();
				
				return 1;
				
			} else {
				System.out.println("Not enough space, segment not allocated.");
				return -1;
				}
		
	}
	// deallocates entire process from memory
	public int deallocate(Process process) {
		
		int processSize = 0;
		boolean processInMemory = false;
		
		// process size calc
		for(Object currentSegment : this.allocatedSegments) {
			
			Segment segment = (Segment)currentSegment;
			
			if(segment.isValid() && segment.getPID() == process.getPID()){
				processSize = processSize + segment.getSize();
			}
			
		}
		
		// check process is in memory
		
		for(Segment currentSegment : this.allocatedSegments) {
			
			if(currentSegment.getPID() == process.getPID()) {
				processInMemory = true;
				break;
			}
			
		}
		
		// deallocate each segment in the process from memory
		if(processInMemory) {
			
			System.out.println("----------------------------------------");
			System.out.println("Deallocating P" + process.getPID() + "\n");
			System.out.println("Segment table before: ");
			System.out.println(process.getSegmentTable().toString());
			
			for(Object currentSegment : allocatedSegments.toArray()) {
				
				Segment segment = (Segment)currentSegment;
				if(segment.getPID() == process.getPID()) {
					
					segment.setValid(false);
					segment.setBaseAddress(0);
					segment.setHoleDiff(0);
					allocatedSegments.remove(currentSegment);
					
				}
			}
			
			System.out.println("Segment table after: ");
			System.out.println(process.getSegmentTable().toString());
			System.out.println("----------------------------------------\n");
			
			// VERY IMPORTANT adds the memory back after de-allocation
			this.remaining_size = this.remaining_size + processSize;

			recalculateSizes();
			
			return 1;
			
		} else return -1;
		
	}
	// deallocates specific segment from memory
	public int deallocate(Process process, int segmentToDeallocate) {
		
		boolean segmentInMemory = false;
		int segmentSize = 0;
		
		// check segment is in memory
		
		for(Segment currentSegment : this.allocatedSegments) {
			
			if(currentSegment.getPID() == process.getPID() && currentSegment.getSID() == segmentToDeallocate) {
				segmentInMemory = true;
				segmentSize = currentSegment.getSize();
				break;
			}
			
		}
		
		// deallocate each segment in the process from memory
		if(segmentInMemory) {
			
			System.out.println("----------------------------------------");
			System.out.println("Deallocating P" + process.getPID() + ".S" + segmentToDeallocate + "\n");
			System.out.println("Segment table before: ");
			System.out.println(process.getSegmentTable().toString());
			
			for(Object currentSegment : allocatedSegments.toArray()) {
				
				Segment segment = (Segment)currentSegment;
				if(segment.getPID() == process.getPID() && segment.getSID() == segmentToDeallocate) {
					
					segment.setValid(false);
					segment.setBaseAddress(0);
					segment.setHoleDiff(0);
					allocatedSegments.remove(currentSegment);
					
				}
			}
			
			System.out.println("Segment table after: ");
			System.out.println(process.getSegmentTable().toString());
			System.out.println("----------------------------------------\n");
			
			// VERY IMPORTANT adds the memory back after de-allocation
			this.remaining_size = this.remaining_size + segmentSize;

			recalculateSizes();
			
			return 1; // returns 1 if successful
			
		} else return -1; // otherwise -1
		
	}
	
	// run after resizing an allocated process or future allocation and de-allocation memory calculations will be wrong
	private void recalculateSizes() {
		
		this.remaining_size = this.user_size;
		
		for(Segment x : this.allocatedSegments) {
			
			this.remaining_size = this.remaining_size - x.getSize();
			this.remaining_size = this.remaining_size - x.getHoleDiff();
			
		}
		
	}
	
	public void compact() {
		
		System.out.println("Compacting memory..."); //start text
		
		int sizeCounter = 0; //size counter to track how much memory has been freed
		
		for(int i = 1; i < this.allocatedSegments.toArray().length; i++) {
			
			Segment previousSegment = (Segment)this.allocatedSegments.toArray()[i-1]; //gets previous segment
			int previousSegmentHoleSize = previousSegment.getHoleDiff(); //gets hole size of previous segment
			Segment currentSegment = (Segment)this.allocatedSegments.toArray()[i]; //gets current segment
			currentSegment.setBaseAddress(currentSegment.getBaseAddress() - previousSegmentHoleSize); //shifts base address back by the hole size of previous segment
			sizeCounter = sizeCounter + previousSegment.getHoleDiff(); // increases size counter
			previousSegment.setHoleDiff(0); // sets hole diff to 0
			
		}

		recalculateSizes();
		
		System.out.println("Compaction complete - " + sizeCounter + " bytes freed.\n"); //success text
		
	}
	
}
