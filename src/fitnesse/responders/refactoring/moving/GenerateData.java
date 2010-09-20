package fitnesse.responders.refactoring.moving;

import java.util.Arrays;
import java.util.Random;

public class GenerateData {
  
  double[] patientIns = new double[]{100, 150, 150, 150, 80, 80, 100};
  int[] patientTurns = new int[]{4, 4, 3, 2, 2, 4, 5};
  Random randomGenerator = new Random(1000);
  
  double[] occupancies =  new double[1000];
  double[] patientsIn = new double[1000];
  double[] patientsOut = new double[1000];

  public GenerateData() {
    Arrays.fill(occupancies, 0);
    Arrays.fill(patientsIn, 0);
    Arrays.fill(patientsOut, 0);
    
    for (int dayCount = 0; dayCount < 100 ; dayCount++ ) {
      int patients = (int)(((randomGenerator.nextFloat())*.4 + .8) * patientIns[dayCount % 7]) ;
      int patientTurn = patientTurns[dayCount % 7] ;
      for (int patient = 0; patient < patients ; patient++) {
        writePatient(dayCount, getCheckinTime(), getStayLenth(patientTurn));
      }
    }
    printData();
  }
  
  private void printData() {
    for (int i = 0; i < 110; i++) {
      System.out.println(i + "," + occupancies[i] + "," + patientsIn[i] + "," + patientsOut[i]);
    }
  }

  private void writePatient(int dayCount, double checkinTime, double stayLength) {
    double checkin = dayCount + checkinTime;
    double checkout = checkin + stayLength;
    
    patientsIn[(int)(Math.floor(checkin))]++;
    patientsOut[(int)Math.floor(checkout)]++;
    
    occupancies[dayCount] += 1-checkinTime;
    stayLength = stayLength - (1-checkinTime);
    while (stayLength > 0) {
      dayCount++;
      if (stayLength >= 1) {
        occupancies[dayCount] += 1;
      }
      else
      {
        occupancies[dayCount] += stayLength; 
      }
      stayLength = stayLength - 1;
    }
  }

  public static void main(String[] args) {
    new GenerateData();
  }
  
  double getStayLenth(int patientTurn) {
    double random = randomGenerator.nextFloat();
    random = random - .5;
    if (random < -0.3) {
      random = random * 2;
    }
    else if (random > 0.25) {
      random = random * 2;
    }
    else if (random > 0.4) {
      random = random * 3;
    }
    return patientTurn + patientTurn * random;
  }
  
  double getCheckinTime()
  {
    double random = randomGenerator.nextFloat();
    if (random < 0.2) {
      return (random)/ 0.2f * 0.3f;
    }
    else if (random < 0.5) {
      return 0.3f + ((random - 0.2)/0.3) * 0.3f;
    }
    else {
      return 0.6f + ((random - 0.5)/0.5) * 0.4f;
    }
  }
  
}
