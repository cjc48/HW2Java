package ub.cse.algo;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;

/**
 * For use in CSE 331 HW1.
 * This is the class you will be editing and turning in. It will be timed against our implementation
 * NOTE that if you declare this file to be in a package, it will not compile in Autolab
 */

public class Solution {
	private int _nHospital;
	private int _nStudent;

    // The following represent the preference list of hospitals and students.
    // The KEY represents the integer representation of a given hospital or student.
    // The VALUE is a list, from most preferred to least.
    // For hospital, first element of the list is number of available slots
	private HashMap<Integer, ArrayList<Integer>> _hospitalList;
	private HashMap<Integer, ArrayList<Integer>> _studentList;
    
    
    /**
     * The constructor simply sets up the necessary data structures.
     * The grader for the homework will first call this class and pass the necessary variables.
     * There is no need to edit this constructor.
     * @param m Number of hospitals
     * @param n Number of students
     * @param A map linking each hospital with its preference list
     * @param A map linking each student with their preference list
     * @return
     */
	public Solution(int m, int n, HashMap<Integer, ArrayList<Integer>> hospitalList, HashMap<Integer, ArrayList<Integer>> studentList) {
		_nHospital = m;
		_nStudent = n;
		_hospitalList = hospitalList;
		_studentList = studentList;
	}
    
    /**
     * This method must be filled in by you. You may add other methods and subclasses as you see fit,
     * but they must remain within the HW1_Student_Solution class.
     * @return Your stable matches
     */
	public Match getPreviousMatch(int student, ArrayList<Match> matches){
		for(Match match : matches){
			if(match.student == student){
				return match;
			}
		}
		return null;
	}

	public boolean studentPrefersNew(int student, int hospitalPrev, int hospitalNew){
		int prevIdx = 0;
		int newIdx = 0;
		//cycle til we find index of hospitalPrev
		for(int i = 0; i < _nHospital; i++){
			if(_studentList.get(student).get(i) == hospitalPrev){
				prevIdx = i;
			}
		}
		//cycle til we find index of hospitalNew
		for(int i = 0; i < _nHospital; i++){
			if(_studentList.get(student).get(i) == hospitalNew){
				newIdx = i;
			}
		}
		if(prevIdx < newIdx){
			return false;
		}
		return true;
	}
	public ArrayList<Match> getMatches() {
		//OUTPUT A STABLE MATCHING
        //Some students may be unassigned
		//No hospital is assigned more students than the number of openings
		//Gale-Shapley almost entirely
		/*
		Instability 1):
		There are students s and s′, and a hospital h, so that:
			s is assigned to h
			s′ is assigned to no hospital
			h prefers s′ to s
		Instability 2):
		There are students s and s′, and hospitals h and h′, so that:
			s is assigned to h
			s′ is assigned to h'
			h prefers s′ to s
			s′ prefers h to h′
		 */
		ArrayList<Match> matches = new ArrayList<>();
		//student and hospital lists are prebuilt, just need to pass to
		//our main algo
		//RUNNING MODIFIED GALE-SHAPLEY
		for(int hospital : _hospitalList.keySet()){
			//while there are empty slots at this hospital
			//number of slots located at _hospitalList.get(hospital).get(0);
			int numOfSlots = _hospitalList.get(hospital).get(0);
			while(numOfSlots != 0) {
				//the nature of this for loop will find the highest ranked free student
				//or otherwise the highest ranked student who'd also prefer this one
				for (int student = 1; student <= _nStudent; student++) {
					//hospital offers a position to the next student on its preference list
					//if that student is free
					if (getPreviousMatch(student,matches) == null){
						//that student accepts
						Match newMatch = new Match(hospital,student);
						matches.add(newMatch);
						_hospitalList.get(hospital).set(0,numOfSlots-1);
						numOfSlots--;
					}
					//else, that student is otherwise commited
					else {
						int prevHospital = getPreviousMatch(student,matches).hospital;
						if (studentPrefersNew(student,prevHospital,hospital)) {
							//number of slots for the new hospital decreases
							_hospitalList.get(hospital).set(0,numOfSlots-1);
							numOfSlots--;
							//number of slots for the old hospital increases
							//be careful not to use numOfSlots here
							_hospitalList.get(prevHospital).set(0,_hospitalList.get(prevHospital).get(0)+1);
						}
						//otherwise move on to the next most preferable student
					}
				}
			}
		}
        return matches;
	}
}
