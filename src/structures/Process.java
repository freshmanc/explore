package structures;
import java.util.*;

import utilities.Utilities;
public class Process {
	
	protected EventSet alphabet;
	protected Failures failures;
	
	public EventSet getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(EventSet alphabet) {
		for(Iterator it=alphabet.iterator();it.hasNext();)
		{
			String tmp=new String((String)it.next()); //create a new string
			this.alphabet.add(tmp);
		}
	}

	public Failures getFailures() {
		return failures;
	}

	public void setFailures(Failures failures) {
		for(Iterator<Failure> it=failures.iterator();it.hasNext();)
		{
			Failure failure=it.next();
			Failure tmp=new Failure(); //create a new object failure
			tmp.setTrace((Trace) failure.getTrace().clone());
			
			tmp.setRefusal((Refusal) failure.getRefusal().clone());
			this.failures.add(tmp);
		}
	}


	public Process() //initiate process
	{
		this.alphabet=new EventSet();
		this.failures=new Failures();
	}
	
	public Process(TransitionSystem ts) //generate process from transition system
	{	
	      this.alphabet=new EventSet();
	      this.failures=new Failures();
	      Trace trace=new Trace();
	      //System.out.println(Utilities.findInitialState(ts));
	      buildEventSet(Utilities.findInitialState(ts),ts);
	      buildFailures(Utilities.findInitialState(ts),trace,ts);
	}
	
	

	private void buildFailures(String from, Trace trace,TransitionSystem ts)
	{
		Transition tempTst;
	    EventSet psbEvt=new EventSet(); //set of possible next events
		Failure failure=new Failure();
		Refusal powerset;
		
		for(Iterator<Transition> it=ts.iterator();it.hasNext();)//check each path in the transition system
		{
			tempTst=it.next();
			if(tempTst.getFrom().equals(from))
			{
				Trace tmpTrace=new Trace(trace);
				psbEvt.add(tempTst.getLabel()); 
				tmpTrace.add(tempTst.getLabel());
				buildFailures(tempTst.getTo(),tmpTrace,ts);	
			}
		}

		powerset=Utilities.powerSet(Utilities.setDiff(psbEvt,alphabet));//find refusal set
		failure.setRefusal(powerset);
		failure.getRefusal().remove(new EventSet()); //remove {} empty event set
		failure.setTrace(trace);
		failures.add(failure);
		
		
	}
	
	private void buildEventSet(String from, TransitionSystem ts)//build alphabet of the process
	{
		Transition temp;

		for(Iterator<Transition> it=ts.iterator();it.hasNext();)
		{
			temp=(Transition)it.next();
			if(temp.getFrom().equals(from))
			{
				alphabet.add(temp.getLabel());
				buildEventSet(temp.getTo(),ts);
			}
		}
	}
	

	public static void main(String args[])
	{

		
		TransitionSystem ts1=new TransitionSystem();
		ts1.add(new Transition("0","coin","1"));
		ts1.add(new Transition("1","pepsi","2"));
		ts1.add(new Transition("1","coke","3"));
		ts1.add(new Transition("1","tea","6"));
		
		
		Process vmi1=new Process(ts1);
		Utilities.printProcess(vmi1);
	}
}
