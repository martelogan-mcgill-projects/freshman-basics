public class TxtFile
{
  private String filename;
  private String data;
  
  // declare public getter and setter methods
  public String getName()
  {
    return filename;
  }
  public void setName(String newName)
  {
    filename = newName;
  }
  public String getData()
  {
    return data;
  }
  public void setData(String newData)
  {
    data = newData;
  }
  public void appendData(String newData)
  {
    data = data + newData;
  }
  
  // constructor for TxtFile object
  public TxtFile(String name, String text)
  {
    this.filename = name;
    this.data = text;
  }
}