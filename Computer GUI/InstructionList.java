public class InstructionList
{
 private Instruction[] instructions;
 final int MAX_INSTRUCTIONS = 1000;
 private int nextFree;
 private int nextToExecute;
 
 public InstructionList()
 {
   this.instructions = new Instruction[MAX_INSTRUCTIONS];
   nextFree = 0;
   nextToExecute = 0;
 }

 //This method adds a command to instructions
 public String addCommand(String newCommand, String arguments)
 {
  if (arguments.startsWith("."))
  {
   return "Cannot modify a file that starts with . as this is a critical file!";
  }
 
  this.instructions[nextFree] = new Instruction(newCommand, arguments);
  nextFree++;
  return "Successfully added.";
 }

 //This checks to see if there are any instructions left to be executed.
 public boolean hasNext()
 {
  return nextToExecute < nextFree;
 }

 //This method should return the next instruction that is available.
 public String getNextInstruction()
 {
   String command = getInstruction(nextToExecute);
   nextToExecute++;
   return command;
 }
 
 public String getInstruction(int i)
 {
   return this.instructions[i].getCommand() + "(" + this.instructions[i].getArguments() + ")";
 }
 
 //This method returns a String[] of all the instructions left to be executed.
 public String[] getLoadedInstructions()
 {
  String[] loadedInstructions = new String[nextFree-nextToExecute];
  for(int i = nextToExecute; i < nextFree; i++)
  {
    loadedInstructions[i - nextToExecute] = this.getInstruction(i);
  }
  
  return loadedInstructions;
 }
}