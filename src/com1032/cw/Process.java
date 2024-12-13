package com1032.cw;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Process {

    private int PID;
    private final SegmentTable segmentTable;

    public Process(String processString) {

        Parser P = new Parser();
        segmentTable = new SegmentTable();

        ArrayList<String>[] parsedProcessString = P.parseInputString(processString);

        // check for 0s and negatives
        for (ArrayList<String> string : parsedProcessString){

            if(Integer.parseInt(string.get(0)) < 1) {

                throw new InvalidParameterException("Cannot create process with negative ID or segment size.");

            }

        }

        for (int i = 0; i < parsedProcessString.length; i++) {

            if (i == 0) {

                this.PID = Integer.parseInt(parsedProcessString[0].get(0));

            }

            if (i > 0) {

                Segment cSegment = new Segment(i - 1, Integer.parseInt(parsedProcessString[i].get(0)), Integer.parseInt(parsedProcessString[0].get(0)));
                segmentTable.add(cSegment);

            }

            // take each value extracted from string and assign it to a segment table
            // also check each value is valid - not a string

        }

        int numOfSegments = parsedProcessString.length - 1;
        System.out.printf("New process with ID %d created with %d segments\n", this.getPID(), numOfSegments);

    }

    public void printProcessDetails() {

        StringBuilder S = new StringBuilder(256);

        for (Segment x : this.segmentTable.getSegmentTable()) {
            S.append(String.format("[S%d : %d] ", x.getSID(), x.getSize()));
        }

        System.out.printf("[P%d] %s%n", this.PID, S);
    }

    public int getPID() {
        return PID;
    }

    public SegmentTable getSegmentTable() {
        return segmentTable;
    }

    public void resize(String resizeString) {

        Parser P = new Parser();
        ArrayList<String>[] parsedResizeString = P.parseInputString(resizeString);

        System.out.println("Resizing P" + this.getPID() + "...");

        int resizeSize = 0;

        // calculate total size of resize and check for negatives
        for (ArrayList<String> number : parsedResizeString) {

            if(Integer.parseInt(number.get(0)) < 1){
                throw new InvalidParameterException("Cannot use 0 or negative.");
            }
            resizeSize = resizeSize + Integer.parseInt(number.get(0));

        }

        // subtract
        for (int i = 0; i < parsedResizeString.length; i++) {

            this.getSegmentTable().getSegmentTable().get(i).setHoleDiff(Integer.parseInt(parsedResizeString[i].get(0)));
            this.getSegmentTable().getSegmentTable().get(i).setSize(this.getSegmentTable().getSegmentTable().get(i).getSize() - Integer.parseInt(parsedResizeString[i].get(0)));

        }

        // check for 0 size and invalidate
        for (Object currentSegment : this.getSegmentTable().getSegmentTable().toArray()) {

            Segment segment = (Segment) currentSegment;
            if (segment.getSize() == 0) {

                segment.setValid(false);

            }

        }

        System.out.println("Resize complete - " + resizeSize + " bytes deallocated\n");

    }


}
