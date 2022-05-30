package gelato;

import gelato.Kunden.Kunde;
import gelato.Kunden.KundenService;
import gelato.Mails.Mail;
import gelato.Mails.MailService;
import gelato.Wetter.Wetter;

import static java.lang.ProcessBuilder.Redirect.INHERIT;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.util.concurrent.TimeUnit.SECONDS;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class Marketing {

  private static final double CORONA_FACTOR = 0.13;
  public static final int SECHS_STUNDEN = 6 * 3600 * 1000;
  private final Connection connection;
  private static KundenService kundenservice;
  private static MailService mailservice;



  private Marketing(Connection connection) {
    this.connection = connection;
  }

  public static Marketing create() {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:kunden");
      return new Marketing(connection);
    } catch (SQLException throwables) {
      System.err.println("Datenbankverbindung konnte nicht hergestellt werden.");
      System.exit(-1);
    }
    return null; // unerreichbar, der Compiler merkt es nur nicht
  }

  public static void main(String[] args) throws InterruptedException, SQLException {


    System.out.println("Marketing Start");
    Marketing m = Marketing.create();

    while (true) {
      System.out.println("Starte Marketing.");
      List<Kunde> kunden = kundenservice.getKunden();


      for (Kunde k : kunden) {
        if (k.isReadyToReceiveMail() && Math.random() < CORONA_FACTOR) {
          mailservice.sendMarketingMail(k);
        }
      }
      System.out.println("Fertig! Schlafe für 6 Stunden");
      Thread.sleep(SECHS_STUNDEN);
    }
  }

}
