In the following readme I'd like to explain some of the design decisisons I took in this assignment.
As always, you must be running Java 8 or higher to run this program-
it makes use of Java 8 streams, and will not compile under Java 7.

## CommandParser
Every command passes through the ```CommandParser``` class. 
This class does some string massaging, and separates the user input 
into a ```List``` of subcommands. It also throws exceptions all the way
Moreover, for non-redirect commands, it appends an empty string to the end of the list,
for the ```StreamFilter```.


## StreamFilter. 
This filter is always the last filter, even in the case of "cd"- in that case,
it prints nothing. Otherwise it prints to either standard out or to a file.
Importantly, it implements ```NotifyingFilter```, and passes a message back 
to the ```ProcessManager``` class when it is done.

## ProcessManager
This class makes a thread for each filter, then executes them. 
Importantly, it assigns each background command a process ID and maintains a mapping 
from process ID's to the command the user typed in to run the background job. Each 
command's ```StreamFilter``` receives an instance of this class, which when notified 
of the StreamFilter's completion, removes the command keyed by the pid from 
the mapping. 


## Other design decisions:   
   *    I did not use isDone. Rather, I used the poison pill approach.
        This approach has each filter place a special string, in this case a v4 UUID,
        in its output queue when it has gone through its input. 
        isDone has been removed from ```ConcurrentFilter``` and ```Filter```
        
   *    For handling input/output correctness, I override ```setNext```
        in each filter and throw a custom ```InvalidParamterException```
    
   *    I have also removed ```setPrevious``` from ```Filter``` and ```ConcurrentFilter```,
        because it is unused
          