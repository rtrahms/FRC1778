package canStateMachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import canStateMachine.events.Event;
import canStateMachine.states.AutoState;


public class ReadAutoScript {
	
	private ArrayList <Phase> _phases = new ArrayList<Phase>();
	
	//public ArrayList<Phase> getPhases()
	//{
	//	return _phases
	//}
	
	//TODO return type should public ArrayList<Phase> ReadScript (String ...
	public void ReadScript (String filename) throws Exception
	{
		
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(filename))) 
		{
			
			stream.forEach((line) -> {
				// debug
				System.out.println("Content: " + line);
				
				line = line.trim();
				
				if (!line.endsWith(";")){
					line = line.concat(";");
				}
			    String newline = System.lineSeparator();
				line.replace(newline, "");
				
				
				// ignore if we have a comment line or empty line
				if ((line.charAt(0) != '/') && (line.length() != 0) )
				{
					if(IntegrityCheck(line))
					{
						try {
							ParseLine(line);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					//throw new RuntimeException("sample");
				}

			});

		}
		catch (IOException e) {
			e.printStackTrace();
			throw e; //re-throw example
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// catch all exception types at the end
			// of the catch blocks.  Think stacking
			// catch blocks as if using a switch statement
		}
		
		return ;
		// return _phases;
	}
	
	public boolean IntegrityCheck(String line){
		boolean allgood = false;
		
		// TODO write the guts
		if (line.contains("|"))
		{
			
		}
		allgood = true;
		
		return allgood;
	}
	
	
	public void ParseLine(String line) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Phase phase = new Phase();
		
		String section;
		
		int sectionEndPos = line.indexOf(";")+1;
		int sectionStartPos = 0;
		while (sectionEndPos > 0)
		{
			section = line.substring(sectionStartPos, sectionEndPos);

			Action action = new Action(section);
			phase.Actions.add(action);
			
			sectionStartPos = sectionEndPos;
			
			sectionEndPos = line.substring(sectionStartPos).indexOf(";")+1;
		}
		
		_phases.add(phase);
	}
}
