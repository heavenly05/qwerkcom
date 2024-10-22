package org.acj;

import org.Database;

public class Main {
  public static void main(String[] args) {
    Database.CreateConnection();
    Database.testCrudFunctionality();
  }
}
