package MultiThreadingCalculation.src;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MyTask implements Runnable {
  private static String expression;
  private String sub_expression;
  private Pattern pattern;
  private Matcher matcher;
  private Integer eval;

  public MyTask(final String sub_expression) {
    this.pattern = Pattern.compile("\\((\\d+\\s*[+\\-*/]\\s*\\d+)\\)");
    this.sub_expression = sub_expression;
  }

  public void setExpression(final String expression) {
    MyTask.expression = expression;
  }

  public static Object getResult(final String operations)
      throws ScriptException {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine nashorn = manager.getEngineByName("nashorn");
    return nashorn.eval(operations);
  }
  public static String getExpression() { return expression; }

  @Override
  public void run() {
    ArrayList<String> list = new ArrayList<String>(); // Creating arraylist
    matcher = pattern.matcher(sub_expression);
    int count = 0;
    System.out.println(
        "this thread is handling the following sub_expression: " +
        sub_expression);

    while (matcher.find()) {
      list.add(matcher.group(matcher.groupCount()));
      ++count;
    }

    if (count > 0) {
      ExecutorService thread_pull = Executors.newFixedThreadPool(count);

      // submitting the task to the thread
      for (int i = 0; i < list.size(); i++)
        thread_pull.execute(new MyTask(list.get(i).replace("[()]", "")));

      // the thread pull cannot accept any more request
      thread_pull.shutdown();

      try {
        thread_pull.awaitTermination(60,
                                     TimeUnit.SECONDS); // wait for
        //                                termination
      } catch (InterruptedException e) {
        //   // handle the exception here
      }
    } else {
      ScriptEngineManager manager = new ScriptEngineManager();
      ScriptEngine nashorn = manager.getEngineByName("nashorn");
      try {
        eval = (Integer)nashorn.eval(sub_expression);
      } catch (ScriptException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (expression.contains(sub_expression))
        System.out.println("it contains: " + sub_expression);
      expression = expression.replace(sub_expression, eval.toString());
      System.out.println("the final expression right now is: " + expression);
    }
  }
}
