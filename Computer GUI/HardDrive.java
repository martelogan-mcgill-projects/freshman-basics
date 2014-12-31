public class HardDrive
{
  // declares drive as an array of TxtFiles
  private TxtFile[] drive;
  
  // declares counter for used space on drive
  private int numUsed;
  
  // maximum drive space: 1000 TxtFiles
  private final int DRIVE_SIZE = 1000;
  
  // constructor for a new HardDrive object
  public HardDrive()
  {
    this.drive = new TxtFile[DRIVE_SIZE];
    numUsed = 0;
  }
  
  // adds file to drive
  public boolean addFile(String filename, String data)
  {
    if(numUsed >= DRIVE_SIZE)
    return false;
    
    if(this.exists(filename))
    return false;
    
    int i = 0;
    while(this.drive[i] != null)
    {
      i++;
    }
    this.drive[i] = new TxtFile(filename, data);
    numUsed++;
    return true;         
  }
  
  // returns index of input file on drive
  public int indexOfFile(String filename)
  {
    for(int i = 0; i < DRIVE_SIZE; i++)
    {
      if(this.drive[i] != null && this.drive[i].getName().equals(filename))
      return i; 
    }
    return -1;
  }
  
  // returns true if file exists
  public boolean exists(String filename)
  {
    return this.indexOfFile(filename) != -1;
  }
  
  // returns data contained on input file
  public String getContent(String filename)
  {
    if(this.indexOfFile(filename) != -1)
    {
      return this.drive[indexOfFile(filename)].getData();
    }
    return null;
  }
  
  // deletes files if it exists (else returns false)
  public boolean deleteFile(String filename)
  {
    if(!exists(filename))
    return false;
    
    this.drive[indexOfFile(filename)] = null;
    numUsed--;
    return true;
  }
  
  // renames file from first argument to second argument
  public boolean renameFile(String from, String to)
  {
    if(!exists(from) || exists(to))
    return false;
    
    this.drive[indexOfFile(from)].setName(to);
    return true;
  }
  
  // appends data to data on input file
  public boolean appendFile(String filename, String extraData)
  {
    if(!exists(filename))
    return false;
    
    this.drive[indexOfFile(filename)].appendData(extraData);
    return true;
  }
  
  // returns a list of files on drive
  public String[] listFiles()
  {
    String[] files = new String[numUsed];
    
    for(int i = 0, j = 0; i < numUsed; i++, j++)
    {
      while(this.drive[j] == null && j < DRIVE_SIZE)
      j++;
      
      files[j] = this.drive[j].getName();
    }
    
    return files;
  }
}