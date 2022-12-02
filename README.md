# MOBILE PROGRAMMING

## Assignment 2

### goal
Make Service && Thread and control each

### problem
I need to stop & resume thread using .interrupt() method
but .interrupt() is not thread state reset
when I restart .start() method there are problem with IllegalThreadStateException

So I need make flag for stop running Thread on while loop,
and when restart button is clicked make new Thread instance

### additional function(Splash Page)
Splash Page for make feel modern theme

### REALIZATION
Thread make with class ThreadName implements Runnable { @Override public void run(){}}
Thread control with flag
Thread.interrupt(), .isAlive() is not good at Thread state check