# sh4ll
made by

https://github.com/ghost2173<br>
https://github.com/IpanaDev<br>
https://github.com/slowcheet4h<br>
<br>
Shell for minecraft<br><br>


https://youtu.be/bSLVQ7CsOuM (how to add shell to a client & how to add commands)<br>
https://youtu.be/PQj6K6YbKmc (how to add commands)<br>



## Usage

```java

Shell._self.setup("CLIENT_NAME", "USER_NAME");

// registering custom execs
Shell._self.registerExec(new TestScript());
Shell._self.registerExec(new InlineInput());
````

## Open/Close Shell

```java

Shell._self.open();
Shell._self.close(); // force close shell

```

## Theme Selection
shell comes with one built-in theme<br>

DarkThemeSh4, ....<br> 
```java

//note: the default theme is already DarkThemeSh4
Shell._self.setTheme(new DarkThemeSh4());

```
<br>

## Custom Textblocks (TODO)

<br>

## Exec System Examples 

```java

public class TestScript extends Exec {
    public TestScript() {
        // aliases, inline inputs, description, useThread
        super(new String[]{"testscript", "test_script"}, new String[]{}, "just a test", true /* if you are getting input from shell this parameter should be true (readChar, readLine)*/);
    }
    
    @Override
    public byte runExec(String fullText, String[] splitted) {
        Shell._self.writeLine(new NormalTextBlock("Enter your name"));
        String name = Shell._self.readLine();
        Shell._self.writeLine(new NormalTextBlock("Enter your lastname"));
        String last = Shell._self.readLine();
        
        Shell._self.writeLine(new NormalTextBlock("Name: " + name + " lastname: " + last);
        return StateResult.EXIT_ON_SUCCESS; /* EXIT_ON_ERROR, RUNNING_BACKGROUND, UNKNOWN_EXIT */
    }
 }
 
 public class InlineInput extends Exec{
    // aliases, inline inputs, description, useThread
    public InlineInput() {
            super(new String[]{"inline"}, new String[]{"testvar1", "testvar2"}, "inline input test", false);
    }
    
    @Override
    public byte runExec(String fullText, String[] splitted) {
         //inline hello there
         String testvar1 = getInput("testvar1"); // value: hello
         String testvar2 = getInput("testvar2"); // value: there
         
         // getInput returns null if empty
         // hasInput(var) - checks if its empty
         return StateResult.EXIT_ON_SUCCESS; 
    }
 }
 
```

<br>

## Built-in games (TODO)
