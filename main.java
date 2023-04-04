// import Person.*;
// import RecordAndTabels.*;
import MultiThreadingCalculation.src.*;
import javax.script.ScriptException;

class main {
  public static void main(String[] args)
      throws InterruptedException, ScriptException {
    MyTask main_task = new MyTask("1+(2+3)+(5+5)+(1+1)+(5+2)+(7+12)");
    main_task.setExpression("1+(2+3)+(5+5)+(1+1)+(5+2)+(7+12)");
    main_task.run();
    System.out.println("the final expression is " + MyTask.getExpression());
    System.out.println("the result is: " +
                       MyTask.getResult(MyTask.getExpression()));
  }
}
