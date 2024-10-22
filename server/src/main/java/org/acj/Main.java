package org.acj;

public class Main {
  public static void main(String[] args) {
    Database.CreateConnection();
    Database.testCrudFunctionality();

    Server.createServer();
  }
}
