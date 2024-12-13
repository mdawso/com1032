package com1032.cw;

public class Main {
	
	public static void main(String[] args) {

		// Creation of memory
		System.out.println("\n - Start B.1.1\n");
		Memory mem = new Memory(256,2048);
		mem.printMemoryState(); // memory shown before operations

		System.out.println("\n - End B.1.1\n");

		// Creation of processes
		System.out.println("\n - Start B.1.2\n");

		Process p1 = new Process("1,100,200,300");
		Process p2 = new Process("2,10,20");
		Process p3 = new Process("3,50,300,250,30,20");
		Process p4 = new Process("4,10,20");
		Process p5 = new Process("5,15,30,25");
		p1.printProcessDetails();
		p2.printProcessDetails();
		p3.printProcessDetails();
		p4.printProcessDetails();
		p5.printProcessDetails();

		System.out.println("\n - End B.1.2\n");
		
		//Allocation of processes
		System.out.println("\n - Start B.1.3\n");
		System.out.println("Each of the newly created segments are allocated to memory, in the case of P1, segment 0 is allocated");
		System.out.println("first, then the rest of the process.\n");
		mem.allocate(p1,0); // allocating S0 of P1 to memory
		mem.allocate(p1); // allocating remaining segments of P1
		mem.allocate(p2); // allocating all segments of P2
		mem.allocate(p3,2); // allocating S2 OF P3
		mem.allocate(p3); // allocating remaining segments of P3
		mem.allocate(p4); // allocating all segments of P4
		mem.allocate(p5); // allocating all segments of P5
		System.out.println("Memory state after allocating 5 segments:");
		mem.printMemoryState();

		System.out.println("\n - End B.1.3\n");
		
		// De-allocation of processes
		System.out.println("\n - Start B.1.4\n");
		System.out.println("The 5 segments are now deallocated from memory.");
		mem.deallocate(p1,0); // deallocating S0 of P1 from memory
		mem.printMemoryState();
		mem.deallocate(p1); // deallocating remaining segments of P1
		mem.printMemoryState();
		mem.deallocate(p2,1); // deallocating S1 of P2
		mem.printMemoryState();
		mem.deallocate(p2); // deallocating remaining segments of P2
		mem.printMemoryState();
		mem.deallocate(p3); // deallocating P3
		mem.printMemoryState();
		mem.deallocate(p4); // deallocating P4
		mem.printMemoryState();
		mem.deallocate(p5); // deallocating P5
		mem.printMemoryState();

		System.out.println("\n - End B.1.4\n");
		
		// Resizing processes
		System.out.println("\n - Start B.1.5\n");
		System.out.println("P3 is allocated then resized.\n");
		mem.allocate(p3); // allocate p3
		System.out.println("Memory state before resizing:");
		mem.printMemoryState(); // memory state before resizing
		
		p3.resize("10,25,30,5"); // each segment in the process has the corresponding int subtracted from it
		System.out.println("Memory state after resizing:");
		mem.printMemoryState(); // memory state after resizing
		
		System.out.println("\n - End B.1.5\n");
		
		// Compaction
		System.out.println("\n - Start B.2.4\n");
		System.out.println("Memory state before compaction:");
		mem.printMemoryState();
		mem.compact();
		System.out.println("Memory state after compaction:");
		mem.printMemoryState();
		System.out.println("\n - End B.2.4\n");

	}

}